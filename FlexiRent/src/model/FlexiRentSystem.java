package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import exception.InvalidInputException;

public class FlexiRentSystem {

	private static Map<String, RentalProperty> prop = new HashMap<String, RentalProperty>(50);
	private static Record record;
	private static ResultSet result;
	static String sql2 = "";

	// Constructor
	public FlexiRentSystem() throws Exception {

	}

	public static void getproperty() throws Exception {
		Connection con = DriverManager.getConnection("jdbc:hsqldb:file:database/FlexiRent", "SA", "");
		PreparedStatement getdata = con.prepareStatement("select * from RENTAL_PROPERTY" + sql2);
		RentalProperty rp;
		result = getdata.executeQuery();
		while (result.next()) {
			String iD = result.getString(1);
			int snum = result.getInt(2);
			String sname = result.getString(3);
			String suburb = result.getString(4);
			String type = result.getString(5);
			int bednum = result.getInt(6);
			String status = result.getString(7);
			String description = result.getString(9);
			String image = result.getString(10);
			if (type.equals("Apartment"))
				rp = new Apartment(iD, snum, sname, suburb, bednum, type, status, description, image);
			else {
				String sqllastmaintance = result.getDate(8).toString();
				DateTime mainDate = DateTime.SQLDateToDateTime(sqllastmaintance);
				rp = new PremiumSuite(iD, snum, sname, suburb, type, status, description, image, mainDate);
			}
			prop.put(iD, rp);
		}
		con.close();
	}

	public static void getrecord() throws Exception {
		Connection con = DriverManager.getConnection("jdbc:hsqldb:file:database/FlexiRent", "SA", "");
		// select * from rental_record where propertyID='123_ASSMEL' select * from
		// rental_record where propertyID='123_ASSMEL' order by rentdate desc limit 0,10
		for (String keys : FlexiRentSystem.getprop().keySet()) {
			PreparedStatement getdata = con.prepareStatement("select * from rental_record where propertyID='"
					+ FlexiRentSystem.getprop().get(keys).getID() + "'" + " order by rentdate desc limit 0,10");
			try {
				result = getdata.executeQuery();
				if (result != null) {
					while (result.next()) {
						String recordID = result.getString(1);
						String propertyID = result.getString(2);
						String cusID = result.getString(3);
						String sqlrentdate = result.getDate(4).toString();
						DateTime rentdate = DateTime.SQLDateToDateTime(sqlrentdate);
						String sqlerdate = result.getString(5);
						DateTime erdate = DateTime.SQLDateToDateTime(sqlerdate);
//				RentalProperty pro=prop.get(propertyID);
						if (result.getDate(6) == null) {
							record = new Record(recordID, propertyID, cusID, rentdate, erdate);
						} else {
							String sqlardate = result.getDate(6).toString();
							double rentalfees = result.getDouble(7);
							double latefees = result.getDouble(8);
							DateTime ardate = DateTime.SQLDateToDateTime(sqlardate);
							record = new Record(recordID, propertyID, cusID, rentdate, erdate, ardate, rentalfees,
									latefees);
						}
						if (prop.containsKey(propertyID))
							prop.get(propertyID).getRec().put(recordID, record);
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		con.close();
	}

	public static void setsql2(String sq) {
		sql2 = sq;
	}

	public static Map<String, RentalProperty> getprop() {
		return prop;
	}

	public static void refresh() throws Exception {
		prop.clear();
		getproperty();
		getrecord();
	}

}
