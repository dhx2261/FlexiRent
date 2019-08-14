package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Record {

	private String propertyID;
	private String recordID;
	private String customerID;
	private DateTime rentdate;
	private DateTime erdate;
	private DateTime ardate;
	private double rentalfees;
	private double latefees;
		
	//Constractor for creating new record
	public Record(String propertyID, String customerID, DateTime rentdate, DateTime erdate) {	//If the property hasn't been returned
		this.customerID=customerID;
		this.recordID=propertyID+"_"+customerID+"_"+rentdate.getEightDigitDate();
		this.rentdate = rentdate;
		this.erdate = erdate;
		this.ardate = null;
		this.rentalfees = 0.0;
		this.latefees = 0.0;
		
	}

	public Record(String recordID, String propertyID,String customerID, DateTime rentdate, DateTime erdate) {//If the property hasn't been returned
		this.recordID=recordID;
		this.propertyID=propertyID;
		this.customerID=customerID;
		this.rentdate = rentdate;
		this.erdate = erdate;
		
	}
	
	public Record(String recordID, String propertyID, String customerID, DateTime rentdate, DateTime erdate, DateTime ardate, double rentalfees, double latefees) {	//If the property has been returned
		this.recordID=recordID;
		this.propertyID=propertyID;
		this.customerID=customerID;
		this.rentdate = rentdate;
		this.erdate = erdate;
		this.ardate = ardate;
		this.rentalfees = rentalfees;
		this.latefees = latefees;
		
	}

	public String toString() {
		if (this.ardate==null)	//If actual return date is null that means the property is being rented.
		return this.recordID+": "+this.rentdate.getFormattedDate()+":"+this.erdate.getFormattedDate()+":"+"none"+":"+"none"+":"+"none";
		return this.recordID+": "+this.rentdate.getFormattedDate()+":"+this.erdate.getFormattedDate()+":"+this.ardate.getFormattedDate()+":"+this.rentalfees+":"+this.latefees;
		
	}
	
	public String getDetails() {
		if (this.ardate==null)	//If actual return date is null that means the property hasn't been returned
		return "Record ID: "+this.recordID+"\n"+"Rent Date: "+this.rentdate.getFormattedDate()+"\n"+"Estimated Return Date: "+this.erdate.getFormattedDate()+"\n";
		else
		return "Record ID: "+this.recordID+"\n"+"Rent Date: "+this.rentdate.getFormattedDate()+"\n"+"Estimated Return Date: "+this.erdate.getFormattedDate()+
				"\n"+"Actual Return Date: "+this.ardate.getFormattedDate()+"\n"+"Rental Fee: "+this.rentalfees+"\n"+"Late Fee: "+this.latefees+"\n";
	}
	

	
	
	public DateTime getrentdate() {
		return this.rentdate;
	}
	
	public void setardate(DateTime setardate) {
		this.ardate=setardate;
	}
	
	public String getrecID() {
		return this.recordID;
	}
	
	public DateTime geterdate() {
		return this.erdate;
	}

	public double getRentalfees() {
		return rentalfees;
	}


	public void setRentalfees() throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:hsqldb:file:database/FlexiRent", "SA", "");
		PreparedStatement sql;
		if (FlexiRentSystem.getprop().get(propertyID).getType().equals("Apartment")) {	//Apartment rental fees
			if (FlexiRentSystem.getprop().get(propertyID).getbednum()==1)
				sql=con.prepareStatement("Update rental_record set rentalfees="+DateTime.diffDays(erdate, rentdate)*143+" where recordID='"+this.recordID+"'");
			else if (FlexiRentSystem.getprop().get(propertyID).getbednum()==2)
				sql=con.prepareStatement("Update rental_record set rentalfees="+DateTime.diffDays(erdate, rentdate)*210+" where recordID='"+this.recordID+"'");
			else
				sql=con.prepareStatement("Update rental_record set rentalfees="+DateTime.diffDays(erdate, rentdate)*319+" where recordID='"+this.recordID+"'");
		}
		else
			sql=con.prepareStatement("Update rental_record set rentalfees="+DateTime.diffDays(erdate, rentdate)*554+" where recordID='"+this.recordID+"'");
		sql.execute();
	}


	public double getLatefees() {
		return latefees;
	}


	public void setLatefees(DateTime returndate) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:hsqldb:file:database/FlexiRent", "SA", "");
		PreparedStatement sql;
		if (FlexiRentSystem.getprop().get(propertyID).getType().equals("PremiumSuite"))	//PremiumSuite late fees
			sql=con.prepareStatement("Update rental_record set latefees="+DateTime.diffDays(returndate, erdate)*662+" where recordID='"+this.recordID+"'");
		else {	// Apartment rental fees
			if (FlexiRentSystem.getprop().get(propertyID).getbednum()==1)
				sql=con.prepareStatement("Update rental_record set latefees="+DateTime.diffDays(returndate, erdate)*164.45+" where recordID='"+this.recordID+"'");
			else if (FlexiRentSystem.getprop().get(propertyID).getbednum()==2)
				sql=con.prepareStatement("Update rental_record set latefees="+DateTime.diffDays(returndate, erdate)*241.5+" where recordID='"+this.recordID+"'");
			else
				sql=con.prepareStatement("Update rental_record set latefees="+DateTime.diffDays(returndate, erdate)*366.85+" where recordID='"+this.recordID+"'");
		}
		sql.execute();
	}


	public DateTime getArdate() {
		return ardate;
	}
	
	public String getcustomerID() {
		return this.customerID;
	}
	
}
