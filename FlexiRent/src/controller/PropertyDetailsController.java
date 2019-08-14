package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exception.InvalidInputException;
import exception.ManagePropertyException;
import javafx.scene.control.Alert;
import model.DateTime;
import model.Record;
import model.RentalProperty;

public class PropertyDetailsController {

//	RentalProperty RP;
	
	public static void commain(String date,RentalProperty RP) throws Exception {
		if (RP.getStatus().equals("maintenance")) {	//Property can be returned only if the status is renting
//			DateTime returndate=DateTime.StringToDateTime(date);
			RP.completeMaintenance(date);
		}
		else
			throw new ManagePropertyException("this property is not under maintenance");
	}
	
	public static void returnproperty(RentalProperty RP,String cusID,String date) throws Exception {
		if (RP.getStatus().equals("renting")) {	//Property can be returned only if the status is renting
			DateTime returndate=DateTime.StringToDateTime(date);
			RP.returnProperty(cusID, returndate);
		}
		else
			throw new ManagePropertyException("this property is not rent by anyone");
	}
	
	public static void maintenance(RentalProperty RP) throws Exception {
		if (RP.getStatus().equals("available")) {	//Property can be returned only if the status is renting
			RP.performMaintenance();
		}
		else
			throw new ManagePropertyException("this property is not available");
	}
		
}
