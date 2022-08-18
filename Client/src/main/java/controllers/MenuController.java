package controllers;

import client.RequestHandler;
import client.request.RequestType;
import client.response.Response;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class MenuController {

    @FXML
    public TextField nameTextField;

    @FXML
    public void OnStartGameButtonClicked() {
        if (getAuthToken())
            MainController.getInstance().setAnchorPane("/views/startGame-view.fxml");
    }

    @FXML
    public void OnJoinGameButtonClicked() {
        if (getAuthToken())
            MainController.getInstance().setAnchorPane("/views/joinGame-view.fxml");
    }

    @FXML
    public void onExitButtonClicked() {
        System.exit(0);
    }

    public boolean getAuthToken() {
        String name = nameTextField.getText();
        if (name.equals("")) {
            MainController.getInstance().showAlert("Please Enter a name", "Name is empty");
            return false;
        }
        RequestHandler req = new RequestHandler(RequestType.AUTHENTICATE,name);
        req.sendRequest();
        if (!RequestHandler.isConnected()) {
            MainController.getInstance().showAlert("Connection Refused", "Please Check your Internet");
            return false;
        }
        Response response = req.getResponse();
        req.setAuthToken(response.getBody());
        return true;
    }

}
