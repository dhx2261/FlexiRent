package exception;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PropertyExistException extends Exception{

	Alert a=new Alert(AlertType.WARNING,"Property already exist");
}
