package controller;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import exception.InvalidInputException;
import exception.RentPropertyException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Apartment;
import model.DateTime;
import model.PremiumSuite;
import model.RentalProperty;
import view.MainWindow;

public class RentPropertyController extends Application{
	private RentalProperty pro;
	
	@FXML
	private TextField customerfi;

	public void customer(ActionEvent customer) {
		String customerID=customerfi.getText();
	}
	
	@FXML
	private DatePicker rentdatefi;
	
	@FXML
	Label status;
	
	
	@FXML
	private TextField daysfi;
	
	public void numofday(ActionEvent numofday) {
		String numofdaystring=daysfi.getText();
//		int numofdays=Integer.parseInt(numofdaystring);
	}
	
	@FXML
	private Button submit;
	
	@FXML
	Button cancel;
	
	public void submit(ActionEvent submit) throws InvalidInputException{
		try {
			String cusID=customerfi.getText();
			String datesql=rentdatefi.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			DateTime date=DateTime.StringToDateTime(datesql);
			int days=Integer.parseInt(daysfi.getText());
			this.rent(cusID, date, days, pro);
		} catch (Throwable e) {
			e=new InvalidInputException();
		}
	}
	
	public void rent(String customerId, DateTime rentDate,int numOfRentDay,RentalProperty RP) throws Exception {
		try {
			DateTime retu=new DateTime(rentDate,numOfRentDay);//Calculate the estimate return date
			if (RP.getStatus().equals("renting"))
				throw new RentPropertyException("this property is already rented");
			else if(RP.getStatus().equals("maintenance"))
				throw new RentPropertyException("this property is under maintenance");
			else {
			if (RP instanceof Apartment) {
					String d1=rentDate.toString();	//Convert rentDate to String, then convert String to Date type
					SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
					Calendar calendar = Calendar.getInstance();
					Date d2=sdf.parse(d1);
			        calendar.setTime(d2);
			        int d3=calendar.get(Calendar.DAY_OF_WEEK);	//Convert Date to the number of days of a week
			        if ((d3!=5 && d3!=6) && numOfRentDay<2)	//Check minimum stay
			        	throw new RentPropertyException("Mininum stay 2 days between Sunday and Thursday");
			        else if ((d3==5||d3==6)&&numOfRentDay<3)
			        	throw new RentPropertyException("Mininum stay 3 days between Friday and Saturday");
			        else {
			        	RP.rent(customerId, rentDate, numOfRentDay,retu);
			        	status.setText("Rent property succefully");
			        	status.setFont(Font.font(25));
			        	status.setTextFill(Color.RED);
			        }
			}
			else {
				PremiumSuite ps=(PremiumSuite) RP;
				if (DateTime.diffDays(retu, ps.getmaintance()) < 10)	//Check maintenance
					ps.rent(customerId, rentDate, numOfRentDay,retu);
				else {
				throw new RentPropertyException("Exceed maintenance interval");
				}
			}
			}
		} 
		catch (Throwable e) {
		}
	}
	
	public void cancel(ActionEvent action) throws Exception {
		MainWindow maintest=new MainWindow();
		maintest.init();
		maintest.showWindow();
		Stage stage = (Stage)cancel.getScene().getWindow();
        stage.close();
	}
	
	@FXML
	private Label propID;
	public String proID(ActionEvent proID){
		return propID.getText();
	}
	
	public void passpro(RentalProperty RP) {
		this.pro=RP;
	}
	
	public static void main(String[] args) {
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Rent Property");
		Parent root=FXMLLoader.load(getClass().getResource("/view/RentProperty.fxml"));
		Scene sc1=new Scene(root);
		primaryStage.setScene(sc1);
		primaryStage.show();
	}

}
