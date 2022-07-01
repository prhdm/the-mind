package controllers;

import client.RequestHandler;
import client.request.RequestType;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class MenuController {
    public TextField nameTextField;

    public void onExitButtonClicked() {
        System.exit(0);
    }

    public void OnStartGameButtonClicked() {
        String name = nameTextField.getText();
        new RequestHandler(RequestType.AUTHENTICATE,name);
        if (name.equals(""))
            MainController.getInstance().showAlert("Please Enter a name","Name is empty");
        else if (RequestHandler.isConnected())
            MainController.getInstance().setAnchorPane("/views/startGame-view.fxml");
        else
            MainController.getInstance().showAlert("Connection Refused","Please Check your Internet");
    }



    public void OnJoinGameButtonClicked() {
        String name = nameTextField.getText();
        new RequestHandler(RequestType.AUTHENTICATE,name);
        if (name.equals(""))
            MainController.getInstance().showAlert("Please Enter a name","Name is empty");
        else if (RequestHandler.isConnected())
            MainController.getInstance().setAnchorPane("/views/joinGame-view.fxml");
        else
            MainController.getInstance().showAlert("Connection Refused","Please Check your Internet");
    }
}
