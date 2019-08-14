package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import controller.MainWindowController;
import controller.RentPropertyController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.FlexiRentSystem;
import model.RentalProperty;

public class MainWindow extends Application {

//	Stage stage=new Stage();

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		// Create menu bar
		MenuBar menubar = new MenuBar();
		// Data menu
		Menu data = new Menu("Data");
		MenuItem imp = new MenuItem("Import");
		MenuItem exp = new MenuItem("Export");
		data.getItems().add(imp);
		data.getItems().add(exp);
		menubar.getMenus().add(data);
//		FileChooser fc=new FileChooser();
		// Menage menu
		Menu manage = new Menu("Manage");
		menubar.getMenus().add(manage);
		MenuItem add = new MenuItem("Add property");
		manage.getItems().add(add);
		// Help menu
		Menu help = new Menu("Exit");
		menubar.getMenus().add(help);
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(e -> {
			Platform.exit();
		});
		help.getItems().add(exit);

		ScrollPane sp = new ScrollPane();
		sp.setPrefSize(1200, 1200);
		FlowPane flow = new FlowPane();
		flow.setPrefWidth(1200);
		sp.setContent(flow);

		// Data export
		exp.setOnAction(e -> {
			DirectoryChooser directoryChooser = new DirectoryChooser();
			File selectedDirectory = directoryChooser.showDialog(primaryStage);
			if (selectedDirectory == null) {
				// No Directory selected
			} else {
				String path = selectedDirectory.getAbsolutePath();
				writeToFile(selectedDirectory.getAbsolutePath(), "export_data.txt​");
			}
		});

		// Data import
		imp.setOnAction(e -> {
			FileChooser filechooser = new FileChooser();
			filechooser.getExtensionFilters().add(new ExtensionFilter("TXT Files", "*.txt"));
			File file = filechooser.showOpenDialog(primaryStage);
			MainWindowController.importdata(file);
		});

		// Search
		ObservableList<String> searchchoice = FXCollections.observableArrayList("All property", "Apartment",
				"PremiumSuite", "Bednum-1", "Bednum-2", "Bednum-3", "Available", "Renting", "Maintenance", "Suburb");
		ComboBox search = new ComboBox(searchchoice);
		search.setPromptText("Search property");
		TextField inputField = new TextField();
		inputField.setPrefWidth(200);
		inputField.setPromptText("Search by suburb");
		inputField.setVisible(false);
		Button searsub = new Button("Submit");
		searsub.setVisible(false);
		HBox top = new HBox();
		top.getChildren().add(menubar);
		top.getChildren().add(search);
		top.getChildren().add(inputField);
		top.getChildren().add(searsub);

		// Search property action
		search.setOnAction(e -> {
			FlexiRentSystem.getprop().clear();
			if (search.getValue().equals("Suburb")) {
				inputField.setVisible(true);
				searsub.setVisible(true);
			} else {
				inputField.setVisible(false);
				searsub.setVisible(false);
				if (search.getValue().equals("Apartment"))
					FlexiRentSystem.setsql2(" Where type='Apartment'");
				else if (search.getValue().equals("PremiumSuite"))
					FlexiRentSystem.setsql2(" Where type='PremiumSuite'");
				else if (search.getValue().equals("Bednum-1"))
					FlexiRentSystem.setsql2(" Where bednum=1");
				else if (search.getValue().equals("Bednum-2"))
					FlexiRentSystem.setsql2(" Where bednum=2");
				else if (search.getValue().equals("Bednum-3"))
					FlexiRentSystem.setsql2(" Where bednum=3");
				else if (search.getValue().equals("Available"))
					FlexiRentSystem.setsql2(" Where status='available'");
				else if (search.getValue().equals("Renting"))
					FlexiRentSystem.setsql2(" Where status='renting'");
				else if (search.getValue().equals("Maintenance"))
					FlexiRentSystem.setsql2(" Where status='maintenance'");
				else if (search.getValue().equals("All property")) {
					FlexiRentSystem.setsql2("");
				}
			}
			try {
				flow.getChildren().clear();
				FlexiRentSystem.getproperty();
				FlexiRentSystem.getrecord();
				flow.getChildren().addAll(this.setcenter());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		searsub.setOnAction(ea -> {
			FlexiRentSystem.getprop().clear();
			String searchsub = inputField.getText().toLowerCase();
			FlexiRentSystem.setsql2(" Where lower(suburb) like '%" + searchsub + "%'");
			try {
				flow.getChildren().clear();
				FlexiRentSystem.getproperty();
				Alert fi = new Alert(Alert.AlertType.INFORMATION, "Export data successfully");
			} catch (Exception e1) {
			}
			flow.getChildren().addAll(this.setcenter());
		});

		// Add property action
		add.setOnAction(e -> {
			try {
				Stage s2 = new Stage();
				Parent root2 = FXMLLoader.load(getClass().getResource("/view/Addproperty.fxml"));
				Scene sc1 = new Scene(root2, 622, 900);
				s2.setScene(sc1);
				s2.show();
				primaryStage.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		flow.getChildren().addAll(this.setcenter());

		BorderPane bp = new BorderPane();
		bp.setPrefSize(1200, 1200);
		bp.setTop(top);
		bp.setCenter(sp);
		Scene scene = new Scene(bp);
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

	// Method to set the center content of boarderpane
	public ObservableList<VBox> setcenter() {
		ObservableList<VBox> pane = FXCollections.observableArrayList();
		for (String keys : FlexiRentSystem.getprop().keySet()) {
			RentalProperty pro = FlexiRentSystem.getprop().get(keys);
			VBox vb = new VBox();
			ImageView image;
			try {
				image = new ImageView(FlexiRentSystem.getprop().get(keys).getImage());

			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				image = new ImageView("Image/Noimage.jpg");
			}
			image.setFitWidth(450);
			image.setFitHeight(300);
			vb.getChildren().add(image);
			// Create Detail button
			ImageView info = new ImageView("Image/Info.png");
			info.setFitHeight(15);
			info.setFitWidth(15);
			Button details = new Button("Details", info);
			// Create Book button
			Button book = new Button("Book");
			
			//Book button action
			book.setOnAction(e -> {
				FXMLLoader load = new FXMLLoader();
				Stage stage1 = new Stage();
				load.setLocation(getClass().getResource("/view/RentProperty.fxml"));
				try {
					load.load();
					RentPropertyController rentc = load.getController();
					rentc.passpro(pro);
					Parent root = load.getRoot();
					Scene scene = new Scene(root, 600, 600);
					stage1.setScene(scene);
					stage1.show();
					Stage stage2 = (Stage) book.getScene().getWindow();
					stage2.close();
				} catch (IOException e1) {
				}
			});
			HBox hb = new HBox();
			hb.setPrefWidth(600);
			hb.setSpacing(20);
			Label address = new Label();
			address.setText(
					FlexiRentSystem.getprop().get(keys).getSnum() + " " + FlexiRentSystem.getprop().get(keys).getSname()
							+ ", " + FlexiRentSystem.getprop().get(keys).getSuburb());
			hb.getChildren().add(address);
			address.setFont(Font.font(18));
			
			//Details button action
			details.setOnAction(e -> {
					PropertyDetails PD = new PropertyDetails();
					PD.init(keys);
					PD.showWindow();
					Stage stage = (Stage) details.getScene().getWindow();
					stage.close();
			});
			hb.getChildren().add(details);
			hb.getChildren().add(book);
			vb.getChildren().add(hb);
			Label proptype = new Label("Type: " + FlexiRentSystem.getprop().get(keys).getType());
			proptype.setFont(Font.font(18));
			vb.getChildren().add(proptype);
			Label propsta = new Label();
			propsta.setText("Status: " + FlexiRentSystem.getprop().get(keys).getStatus());
			propsta.setFont(Font.font(18));
			vb.getChildren().add(propsta);
			Label desc = new Label(FlexiRentSystem.getprop().get(keys).getDesc());
			desc.setWrapText(true);
			vb.getChildren().add(desc);
			vb.setPrefSize(600, 200);
			vb.setPadding(new Insets(30, 30, 30, 30));
			vb.setSpacing(10);
			pane.add(vb);
		}
		return pane;
	}

	public void showWindow() throws Exception {
		this.start(new Stage());
	}

	@Override
	public void init() throws Exception {
		FlexiRentSystem.getprop().clear();
		FlexiRentSystem.getproperty();
		FlexiRentSystem.getrecord();
	}

	public static void main(String[] args) throws Exception {
//		FlexiRentSystem.getproperty();
//		FlexiRentSystem.getrecord();
		launch(args);
	}

}
