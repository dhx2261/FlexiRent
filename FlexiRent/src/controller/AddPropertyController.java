package controller;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import exception.InvalidInputException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.DateTime;
import model.RentalProperty;
import view.MainWindow;

public class AddPropertyController extends Application implements Initializable{

	@FXML
	ComboBox<String> addpro;
	ObservableList<String> typechoice = FXCollections.observableArrayList("Apartment","PremiumSuite");
	
	@FXML
	private Label roomnum;
	
	@FXML
	private ComboBox<Integer> bednum;
	ObservableList<Integer> bdnumchoice = FXCollections.observableArrayList(1,2,3);
	
	@FXML
	private DatePicker last;
	
	@FXML
	private Button submit;
	
	@FXML
	private Button cancel;
	
	@FXML
	private TextField snum;
	
	@FXML
	private TextField sname;
	
	@FXML
	private TextField surburb;
	
	@FXML
	private TextArea desc;
	
	@FXML
	private TextField path;
	
	@FXML
	private Label success;
	
	public void goback(ActionEvent Event) throws Exception {
		MainWindow maintest=new MainWindow();
		maintest.init();
		maintest.showWindow();
		Stage stage = (Stage)cancel.getScene().getWindow();
        stage.close();
	}
	
	public void submitdata(ActionEvent event) throws Exception {
		try {
		String type=addpro.getValue();
		int streetnum=Integer.parseInt(snum.getText());
		String streetname=sname.getText();
		String sur=surburb.getText();
		String desctest=desc.getText();
		String description=desctest.replaceAll("\'", "\"");
		String image;
		if (path.getText().isEmpty()){
			image="Image/Noimage.jpg";
		}
		else {
			image=path.getText();
		}
		if (type.equals("PremiumSuite")) {
		String lastmain=last.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		RentalProperty.addpre(streetnum, streetname, sur, type,lastmain, description,image);
		}
		else if (type.equals("Apartment")) {
			int bednumber=bednum.getValue();
			RentalProperty.addapart(streetnum, streetname, sur, type, bednumber, description,image);
		}
		success.setVisible(true);
		}
		catch (Throwable a) {
			a=new InvalidInputException();
		}
	}
	
	public void comboChanged(ActionEvent event) throws Exception{
		String type=addpro.getValue();
		if (type.equals("Apartment")) {
			roomnum.setText("Room Number");
			bednum.setItems(bdnumchoice);
			bednum.setVisible(true);
			last.setVisible(false);
		}
		if (type.equals("PremiumSuite")) {
			bednum.setVisible(false);
			last.setVisible(true);
			roomnum.setText("Last Maintanance");
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
//		primaryStage.setTitle("Add property");
//		Parent root=FXMLLoader.load(getClass().getResource("/view/Addproperty.fxml"));
//		Scene sc1=new Scene(root);
//		primaryStage.setScene(sc1);
//		primaryStage.show();
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		addpro.setItems(typechoice);
		bednum.setVisible(false);
		last.setVisible(false);
		success.setVisible(false);
	}
	
}
