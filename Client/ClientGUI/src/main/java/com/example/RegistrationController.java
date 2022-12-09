package com.example;

import Commands.Response;
import connectionModule.ConnectionModule;
import entities.Status;
import entities.User;
import enums.UserType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrationController {

    @FXML
    private TextField loginInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private PasswordField repeatInput;

    @FXML
    void onClickEnter(ActionEvent event) {
        var login = loginInput.getText();
        var password = passwordInput.getText();
        var repeatPassword = repeatInput.getText();

        if(login.isEmpty() ||password.isEmpty() ||repeatPassword.isEmpty()){
            AlertManager.showWarningAlert("Поля должны быть заполнены", "Заполните все поля!");
            return;
        }
        if(!password.equals(repeatPassword)){
            AlertManager.showWarningAlert("Ошибка", "Пароли должны совпадать!");
            return;
        }

        try {
            if( ConnectionModule.registration(new User(0, login, password, Status.NOT_BANNED)) == Response.SUCCESSFULLY){
                Client.userType = UserType.USER;
                Client.viewManager.setSceneToStage(ViewManager.Views.menu, "Меню");
            }
            else{
                AlertManager.showErrorAlert("Ошибка", "Пользователь с таким логином уже существует");
            }

        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка соединения", "");
        }
    }

    @FXML
    void onClickGoBack(ActionEvent event) {
        Client.viewManager.setSceneToStage(ViewManager.Views.authorization, "Авторизация");
    }

}
