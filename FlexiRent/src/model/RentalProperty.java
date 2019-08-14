package model;

import java.util.*;

import exception.PropertyExistException;
import exception.ManagePropertyException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class RentalProperty {
	private String ID;
	private int snum;
	private String sname;
	private String suburb;
	private String status;
	private String type;
	private String desc;
	private String image;
	private Map<String, Record> rec;

	public RentalProperty(String iD, int snum, String sname, String suburb, String type, String status, String desc,
			String image) throws SQLException {
		ID = iD;
		this.snum = snum;
		this.sname = sname;
		this.suburb = suburb;
		this.status = status;
		this.type = type;
		this.desc = desc;
		this.image = image;
		this.rec = new HashMap<String, Record>();
	}

	public void rent(String customerId, DateTime rentDate, int numOfRentDay, DateTime retu) throws Exception {
		Connection con = DriverManager.getConnection("jdbc:hsqldb:file:database/FlexiRent", "SA", "");
		String sqlrentdate = DateTime.StringToSQLDate(rentDate.toString());
		String sqlesretu = DateTime.StringToSQLDate(retu.toString());
		Record rec = new Record(this.getID(), customerId, rentDate, retu);
		PreparedStatement addrentrec = con.prepareStatement("Insert into RENTAL_RECORD values (" + "'" + rec.getrecID()
				+ "'" + "," + "'" + this.getID() + "'" + "," + "'" + customerId + "'" + "," + "'" + sqlrentdate + "'"
				+ "," + "'" + sqlesretu + "'" + "," + "null,0,0 )");
		PreparedStatement setrenting = con.prepareStatement(
				"UPDATE rental_property set status='renting' where propertyID=" + "'" + this.getID() + "'");
		addrentrec.execute();
		setrenting.execute();
		con.close();
		FlexiRentSystem.refresh();
	}

	public static void addapart(int snum, String sname, String suburb, String type, int bednum, String description,
			String image) throws Exception {
		Connection con = DriverManager.getConnection("jdbc:hsqldb:file:database/FlexiRent", "SA", "");
		PreparedStatement addapart;
		String ID = RentalProperty.generateID(snum, sname, suburb, type);
		if (FlexiRentSystem.getprop().containsKey(ID)) {
			throw new PropertyExistException();
		} else {
			addapart = con.prepareStatement("insert into rental_property values(" + "'" + ID + "'" + "," + snum + ","
					+ "'" + sname + "'" + "," + "'" + suburb + "'" + "," + "'" + type + "'" + "," + bednum + ","
					+ "'available',null" + "," + "'" + description + "'" + "," + "'" + image + "')");
			addapart.execute();
		}
		con.close();
		FlexiRentSystem.refresh();
	}

	public static void addpre(int snum, String sname, String suburb, String type, String lastmain, String description,
			String image) throws Exception {
		Connection con = DriverManager.getConnection("jdbc:hsqldb:file:database/FlexiRent", "SA", "");
		PreparedStatement addpre;
		String ID = RentalProperty.generateID(snum, sname, suburb, type);
		if (FlexiRentSystem.getprop().containsKey(ID)) {
			con.close();
			throw new PropertyExistException();
		} else {
			addpre = con.prepareStatement("insert into rental_property values(" + "'" + ID + "'" + "," + snum + ","
					+ "'" + sname + "'" + "," + "'" + suburb + "'" + "," + "'" + type + "'" + ",3," + "'available'"
					+ "," + "'" + lastmain + "'" + "," + "'" + description + "'" + "," + "'" + image + "')");
			addpre.execute();
//			System.out.println(addpre);
			con.close();
		}
		FlexiRentSystem.refresh();
	}

	public void addpre(int snum, String sname, String suburb, String type, DateTime lastmain, String description) {

	}

//	public static boolean returnproperty(String propertyID, String cusID, String date) throws SQLException {
//		con= DriverManager.getConnection("jdbc:hsqldb:file:database/FlexiRent", "SA", "");
//		PreparedStatement returnprop,getID;
//		getID=con.prepareStatement("select recordID,customerID from rental_record where ardate is null and propertyID="+"'"+propertyID+"'");
//		ResultSet IDres=getID.executeQuery();
//		if (IDres.next()) {
//		String recID=IDres.getString(1);
//		String cID=IDres.getString(2);
//		if (cusID.equals(cID)) {
//			returnprop = con.prepareStatement("Update rental_record set ardate="+"'"+date+"'"+"where recordID="+"'"+recID+"'");
//			returnprop.execute();
//			con.close();
//			return true;
//		}
//		}
//			con.close();
//			return false;
//	}

	public void returnProperty(String cusID, DateTime returnDate) throws Exception {
		Connection con = DriverManager.getConnection("jdbc:hsqldb:file:database/FlexiRent", "SA", "");
		PreparedStatement recreturn, setreturn;
		PreparedStatement getrecID;
		getrecID = con
				.prepareStatement("select recordID,customerID from rental_record where ardate is null and propertyID="
						+ "'" + this.ID + "'");
		ResultSet IDres = getrecID.executeQuery();
		if (IDres.next()) {
			String recID = IDres.getString(1);
			String cID = IDres.getString(2);
			Record rentingrec = this.rec.get(recID);
//			if (DateTime.diffDays(returnDate, rentingrec.getrentdate()) > 0 && cID.equals(cusID)) {
//				rentingrec.setardate(returnDate); // Add actual return date to record
//				this.status = "available";
//				rentingrec.setRentalfees(); // Add rental fees to record
			if (DateTime.diffDays(returnDate, rentingrec.geterdate()) <= 0)
				throw new ManagePropertyException("return date is invalid");
			else if (!cID.equals(cusID))
				throw new ManagePropertyException("customer ID is invalid");
			else if (DateTime.diffDays(returnDate, rentingrec.geterdate()) > 0 && cID.equals(cusID)) {
				rentingrec.setRentalfees();
				if((DateTime.diffDays(returnDate, rentingrec.geterdate())>0) )
					rentingrec.setLatefees(returnDate);
					String sqlardate = DateTime.StringToSQLDate(returnDate.toString());
					recreturn = con.prepareStatement("UPDATE rental_record set ardate= " + "'" + sqlardate + "'"
							+ " where recordID=" + "'" + rentingrec.getrecID() + "'");
					setreturn = con.prepareStatement("UPDATE rental_property set status= 'available' where propertyID="
							+ "'" + this.getID() + "'");
					recreturn.execute();
					setreturn.execute();
			}
		}
		 con.close();
		 FlexiRentSystem.refresh();
	}

	public void performMaintenance() throws Exception {
		Connection con = DriverManager.getConnection("jdbc:hsqldb:file:database/FlexiRent", "SA", "");
		PreparedStatement setmaint;
			setmaint = con.prepareStatement(
					"update rental_property set status='maintenance' where propertyID='" + this.ID + "'");
			setmaint.execute();
			con.close();
			this.status="maintenance";
			FlexiRentSystem.refresh();
	}

	public String getID() {
		return this.ID;
	}

	public void completeMaintenance(String completionDate) throws Exception {
		Connection con = DriverManager.getConnection("jdbc:hsqldb:file:database/FlexiRent", "SA", "");
			if (this instanceof PremiumSuite) {
				PreparedStatement setmaindate = con.prepareStatement("update rental_property set lastmaintance='" + completionDate + "'"+ " where propertyID='" + this.ID + "'");
				setmaindate.execute();
			}
			this.setstatus("available");
			con.close();
			FlexiRentSystem.refresh();
	}

	public abstract int getbednum();

	public String toString() {
		if (this instanceof Apartment) // Check property type
			return this.ID + ":" + this.snum + ":" + this.sname + ":" + this.suburb + ":" + this.type + ":"
					+ this.getbednum() + ":" + this.status;
		else {
			PremiumSuite pre = (PremiumSuite) this;
			return this.ID + ":" + this.snum + ":" + this.sname + ":" + this.suburb + ":" + this.type + ":"
					+ this.getbednum() + ":" + this.status + ":" + pre.getmaintance().getFormattedDate();
		}
	}

//	public String getDetails() {
//		String get1;
//		String get2;
//		get1 = "Property ID: " + this.ID + "\n" + "Address: " + this.snum + " " + this.sname + "\n" + "Type: "
//				+ this.type + "\n" + "Bedroom: " + this.getbednum() + "\n" + "Status: " + this.status + "\n" + "\r\n"; // Get
//																														// property
//																														// details
//		if (this instanceof PremiumSuite) {
//			PremiumSuite pre = (PremiumSuite) this;
//			get2 = "Last maintenance: " + pre.getmaintance().toString() + "\n" + "\r\n";
//			get1 = get1 + get2;
//		}
//		if (this.rec.isEmpty())
//			get1 = get1 + "RENTAL RECORD: empty";
//		else {
//			get1 = get1 + "RENTAL RECORD" + "\n"; // Get records details for the property
//			for (int i = 0; i < rec.size(); i++)
//				if (this.rec.get(i) != null) {
//					get1 = get1 + this.rec.get(i).getDetails() + "\n" + "\r\n";
//				}
//		}
//		return get1;
//	}

	public int getSnum() {
		return snum;
	}

	public String getSname() {
		return sname;
	}

	public String getSuburb() {
		return suburb;
	}

	public String getStatus() {
		return status;
	}

	public String getType() {
		return type;
	}

//	public void addnewrec(Record record) {
//		this.rec.put(record);
//	}

	public Map<String, Record> getRec() {
		return rec;
	}

	public String getDesc() {
		return desc;
	}

	public String getImage() {
		return image;
	}

	public void setstatus(String st) throws Exception {
		Connection con = DriverManager.getConnection("jdbc:hsqldb:file:database/FlexiRent", "SA", "");
		PreparedStatement setsta;
		setsta = con.prepareStatement("UPDATE rental_property set status=" + "'" + st + "'" + " where propertyID=" + "'"
				+ this.getID() + "'");
		setsta.execute();
		con.close();
		FlexiRentSystem.refresh();
	}

	public static String generateID(int snum, String sname, String surbub, String type) {
		String[] sname2 = sname.split("( )"); // Split String to two strings by space
		String sparts1 = sname2[0].toUpperCase();
		String sparts2 = sname2[1].toUpperCase();
		char s1 = sparts1.charAt(0);
		char s2 = sparts2.charAt(0);
		String sur = surbub.substring(0, 3).toUpperCase();
		char ty = type.toUpperCase().charAt(0);
		String auto = ty + "_" + snum + s1 + s2 + sur;
		return auto;
	}

}
