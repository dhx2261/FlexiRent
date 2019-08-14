package exception;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class RentPropertyException extends Exception{
	
//	String message;
	
	public RentPropertyException(String message) {
		super(message); 
		Alert al=new Alert(Alert.AlertType.ERROR);
		al.setContentText("Opration failed because "+message);
		al.showAndWait();
		return;
	}

//	public String getMessage() {  
//        return message;  
//    }  
	
}
