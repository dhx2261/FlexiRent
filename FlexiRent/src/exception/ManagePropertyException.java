package exception;

import javafx.scene.control.Alert;

public class ManagePropertyException extends Exception{

	public ManagePropertyException(String message) {
		Alert al=new Alert(Alert.AlertType.ERROR);
		al.setContentText("Return property failed because "+message);
		al.showAndWait();
		return;
	}
}
