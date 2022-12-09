package com.example;

import connectionModule.ConnectionModule;
import enums.UserType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthorizationController {

    @FXML
    private TextField loginInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    void onClickEnter(ActionEvent event) {
        var login = loginInput.getText();
        var password = passwordInput.getText();

        try {

            Client.userType = ConnectionModule.singUp(login, password);
            if(Client.userType == UserType.UNDEFINED){
                AlertManager.showErrorAlert("Пользователь не найден!", "");
            }
            else{
                Client.viewManager.setSceneToStage(ViewManager.Views.menu, "Меню");
            }
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка соединения", "");
        }
    }

    @FXML
    void onClickRegistration(ActionEvent event) {
        Client.viewManager.setSceneToStage(ViewManager.Views.registration, "Регистрация");
    }

}
