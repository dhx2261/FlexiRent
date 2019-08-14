package exception;

import javafx.scene.control.Alert;

public class InvalidInputException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidInputException() {
		Alert al=new Alert(Alert.AlertType.ERROR);
		al.setContentText("Invalid input, please try again!");
		al.showAndWait();
		return;
	}

}
