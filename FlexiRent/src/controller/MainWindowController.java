package controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import model.*;

public class MainWindowController {

	public static void importdata(File file) {
		try {
			RentalProperty rp;
			Scanner sc=new Scanner(new FileReader(file));
			String input=sc.nextLine();
			while (sc.hasNextLine()) {
				String[] in=input.split(":");
				String propertyID=in[0];
				int snum=Integer.parseInt(in[1]);
				String sname=in[2];
				String suburb=in[3];
				String type=in[4];
				int bednum=Integer.parseInt(in[5]);
				String status=in[6];

				if (type.equals("Apartment")) {
					String image=in[7];
					String desc=in[8];
					rp=new Apartment(propertyID,snum,sname,suburb,bednum,type,status,desc,image);
				}
				else {
					DateTime lastmain=DateTime.StringToDateTime(in[7]);
					String image=in[8];
					String desc=in[9];
					rp=new PremiumSuite(propertyID,snum,sname,suburb,type,status,desc,image,lastmain);
				}
			}
		} catch (IOException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	

}
