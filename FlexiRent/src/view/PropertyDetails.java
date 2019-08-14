package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;

import controller.MainWindowController;
import controller.PropertyDetailsController;
import controller.RentPropertyController;
import exception.InvalidInputException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import model.DateTime;
import model.FlexiRentSystem;
import model.RentalProperty;

public class PropertyDetails extends Application{
	String property;
	Stage stage=new Stage();
	Button submit=new Button("Submit");
//	String propertyID;
	
	public void init(String RP) {
		this.property=RP;
		submit.setVisible(false);
	}
	
	public void showWindow(){
		try {
			start(new Stage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader=new FXMLLoader();
		
		//Create menu bar
		MenuBar menubar=new MenuBar();
		//Data menu
		Menu data=new Menu("Data");
		MenuItem imp=new MenuItem("Import");
		MenuItem exp=new MenuItem("Export");
		data.getItems().add(imp);
		data.getItems().add(exp);
		menubar.getMenus().add(data);
		//Menage menu
		Menu manage=new Menu("Manage");
		menubar.getMenus().add(manage);
		MenuItem add=new MenuItem("Add property");
		manage.getItems().add(add);
		//Help menu
		Menu help=new Menu("Exit");
		menubar.getMenus().add(help);
		MenuItem exit=new MenuItem("Exit");
		exit.setOnAction(e ->{
			Platform.exit();
		});
		help.getItems().add(exit);
		
		// Data export
				exp.setOnAction(e -> {
					DirectoryChooser directoryChooser = new DirectoryChooser();
					File selectedDirectory = directoryChooser.showDialog(primaryStage);
					if (selectedDirectory == null) {
						// No Directory selected
					} else {
						String path = selectedDirectory.getAbsolutePath();
						writeToFile(selectedDirectory.getAbsolutePath(), "export_data.txt");
					}
				});

				// Data import
				imp.setOnAction(e -> {
					FileChooser filechooser = new FileChooser();
					filechooser.getExtensionFilters().add(new ExtensionFilter("TXT Files", "*.txt"));
					File file = filechooser.showOpenDialog(primaryStage);
					MainWindowController.importdata(file);
				});
		
		//Go to main page funtion
		Button gomain=new Button("Go To Main Page");
		gomain.setOnAction(e ->{
			try {
				MainWindow maintest=new MainWindow();
				maintest.showWindow();
				Stage stage = (Stage)gomain.getScene().getWindow();
		        stage.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			primaryStage.close();
		});
		
		//Add property action
				add.setOnAction(e->{
					try {
						Stage s2 = new Stage();
						Parent root2 = FXMLLoader.load(getClass().getResource("/view/Addproperty.fxml"));
						Scene sc1 = new Scene(root2,622,900);
						s2.setScene(sc1);
						s2.show();
						primaryStage.close();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
		
		//Center of borderpane
		ScrollPane sp=new ScrollPane();
		sp.setPrefSize(600, 600);
		HBox hb=new HBox();
		VBox vb1=new VBox();
		vb1.setPadding(new Insets(30,30,30,30));
		vb1.setSpacing(20);
		VBox vb2=new VBox();
		vb2.setPadding(new Insets(30,30,30,30));
		vb2.setSpacing(20);
		Label propertyID=new Label(FlexiRentSystem.getprop().get(property).getID());
		propertyID.setFont(Font.font(25));
		vb1.getChildren().add(propertyID);
		ImageView im;
		try {
			im=new ImageView(FlexiRentSystem.getprop().get(property).getImage());
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			im=new ImageView("Image/Noimage.jpg");
		}
		im.setFitWidth(450);
		im.setFitHeight(300);
		vb1.getChildren().add(im);
		Label proptype=new Label("Type: "+FlexiRentSystem.getprop().get(property).getType());
		proptype.setFont(Font.font(25));
		vb1.getChildren().add(proptype);
		Label propsta=new Label();
		propsta.setText("Status: "+FlexiRentSystem.getprop().get(property).getStatus());
		propsta.setFont(Font.font(25));
		vb1.getChildren().add(propsta);
		Label address=new Label();
		address.setFont(Font.font(25));
		address.setText(FlexiRentSystem.getprop().get(property).getSnum()+" "+FlexiRentSystem.getprop().get(property).getSname()+", "+FlexiRentSystem.getprop().get(property).getSuburb());
		vb1.getChildren().add(address);
		Text desc=new Text(FlexiRentSystem.getprop().get(property).getDesc());
		vb1.getChildren().add(desc);
		desc.setWrappingWidth(400);
		hb.getChildren().add(vb1);
		
		//Right side of the center
		Label man=new Label("Property Operation");
		man.setFont(Font.font(25));
		vb2.getChildren().add(man);
		Label datelabel=new Label("Choose Date");
		datelabel.setVisible(false);
		DatePicker date=new DatePicker();
		date.setVisible(false);
		Label cusID=new Label("Enter customer ID");
		cusID.setVisible(false);
		TextField customer=new TextField();
		customer.setVisible(false);
		Label status=new Label();
		status.setFont(Font.font(25));
		status.setTextFill(Color.RED);

		//Property manage combobox
		ObservableList<String> choice = FXCollections.observableArrayList("Book","Return","Maintance","Complete Maintance");
		ComboBox act=new ComboBox(choice);
		vb2.getChildren().add(act);
		act.setOnAction(e->{
			if (act.getValue().equals("Book")) {
				submit.setVisible(false);
				FXMLLoader load = new FXMLLoader();
				Stage stage1=new Stage();
				load.setLocation(getClass().getResource("/view/RentProperty.fxml"));
				try {
					load.load();
					RentPropertyController rentc=load.getController();
					rentc.passpro(FlexiRentSystem.getprop().get(property));
					Parent root=load.getRoot();
					Scene scene=new Scene(root,600,600);
					stage1.setScene(scene);
					stage1.showAndWait();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if (act.getValue().equals("Return")) {
				
				submit.setVisible(true);
				datelabel.setVisible(true);
				date.setVisible(true);
				cusID.setVisible(true);
				customer.setVisible(true);
				status.setVisible(false);
			}
			else {
				submit.setVisible(true);
				datelabel.setVisible(true);
				date.setVisible(true);
				cusID.setVisible(false);
				customer.setVisible(false);
				status.setVisible(false);
			}
		});

		vb2.getChildren().add(datelabel);
		vb2.getChildren().add(date);
		vb2.getChildren().add(cusID);
		vb2.getChildren().add(customer);
		vb2.getChildren().add(submit);
		vb2.getChildren().add(status);
		vb2.getChildren().add(gomain);
		hb.getChildren().add(vb2);
		
		//Submit action
		submit.setOnAction(e->{
			try {
			if (act.getValue().equals("Return")) {
				String returndatesql=date.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				String cus=customer.getText();
				PropertyDetailsController.returnproperty(FlexiRentSystem.getprop().get(property),cus,returndatesql);
				status.setText("Operation succeed");
				status.setVisible(true);
			}
			else if (act.getValue().equals("Maintance")){
				PropertyDetailsController.maintenance(FlexiRentSystem.getprop().get(property));
				status.setText("Operation succeed");
				status.setVisible(true);
			}
			else if(act.getValue().equals("Complete Maintance")) {
				String comdate=date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				PropertyDetailsController.commain(comdate, FlexiRentSystem.getprop().get(property));
				status.setText("Operation succeed");
				status.setVisible(true);
			}
			}
			catch (Throwable a) {
				a=new InvalidInputException();
			}
			status.setVisible(false);
			status.setVisible(true);
		});
		
		//Bottom of the page to list records
		ObservableList<String> rec = FXCollections.observableArrayList();
		ListView<String> tree=new ListView<String>();
		tree.setPrefHeight(200);
		if (!FlexiRentSystem.getprop().get(property).getRec().isEmpty()) {
		for (String recid:FlexiRentSystem.getprop().get(property).getRec().keySet()) {
			rec.add(FlexiRentSystem.getprop().get(property).getRec().get(recid).getDetails());
		}
		tree.setItems(rec);
		}

		BorderPane bp=new BorderPane();
		bp.setPrefSize(800, 900);
		bp.setTop(menubar);
		bp.setCenter(sp);
		sp.setContent(hb);
		bp.setBottom(tree);
		Scene scene=new Scene(bp);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Rental Property System");
		primaryStage.show();
	}
	
	// Method for writing file
	private void writeToFile(String absolutePath, String fileName){
		PrintWriter writer;
			try {
				writer = new PrintWriter(absolutePath + "/" + fileName, "UTF-8");
				for (String keys : FlexiRentSystem.getprop().keySet()) {
					writer.println(FlexiRentSystem.getprop().get(keys).toString() + ":"
							+ FlexiRentSystem.getprop().get(keys).getImage() + ":"
							+ FlexiRentSystem.getprop().get(keys).getDesc());
					if (!FlexiRentSystem.getprop().get(keys).getRec().isEmpty()) {
						for (String ck : FlexiRentSystem.getprop().get(keys).getRec().keySet())
							writer.println(FlexiRentSystem.getprop().get(keys).getRec().get(ck).toString());
					}
					writer.println("");
				}
				writer.close();
			} catch (FileNotFoundException e) {
			} catch (UnsupportedEncodingException e) {
			}
	}

}
