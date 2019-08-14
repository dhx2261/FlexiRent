package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PremiumSuite extends RentalProperty{
	private DateTime lastmaintance;
	private int bednum;

	public PremiumSuite(String iD, int snum, String sname, String suburb, String type, String status, String desc, String image,
			DateTime lastmaintance) throws SQLException {
		super(iD, snum, sname, suburb, type,status,desc,image);
		this.lastmaintance = lastmaintance;
		this.bednum=3;
	}
		
	public DateTime getmaintance() {
		return this.lastmaintance;
	}

	public void setLastmaintance(String lastmaintance) throws Exception {
		Connection con= DriverManager.getConnection("jdbc:hsqldb:file:database/FlexiRent", "SA", "");
		PreparedStatement setmain;
		setmain= con.prepareStatement("Update rental_property set lastmaintance="+"'"+DateTime.StringToSQLDate(lastmaintance)+"'"+" where propertyID="+"'"+this.getID()+"'");
		con.close();
		setmain.close();
	}
	
	public int getbednum() {
		return this.bednum;
	}
	
}
