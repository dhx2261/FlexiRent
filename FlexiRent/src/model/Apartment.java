package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Apartment extends RentalProperty {
	private int bednum;
	private Connection con;
	private Statement stmt;

	public Apartment(String iD, int snum, String sname, String suburb, int bednum, String type, String status, String desc, String image) throws SQLException {
		super(iD, snum, sname, suburb, type,status,desc,image);
		this.bednum = bednum;
		this.con=DriverManager.getConnection("jdbc:hsqldb:file:database/FlexiRent", "SA", "");
		this.stmt=con.createStatement();
	}

	public int getbednum() {
		return this.bednum;
	}
}
