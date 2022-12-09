package com.example;

import connectionModule.ConnectionModule;
import enums.UserType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class MenuController {

    @FXML
    public void initialize(){
        if(Client.userType == UserType.ADMIN){
            profilesButton.setText("Пользователи");
            reportsButton.setText("Отчеты");
        }
        else{
            profilesButton.setText("Корзина");
            reportsButton.setText("Мои билеты");
        }
    }
    @FXML
    private Button aboutButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button profilesButton;

    @FXML
    private Button raisesButton;

    @FXML
    private Button reportsButton;

    @FXML
    void onClickAbout(ActionEvent event) {
        String header = "";
        String text = "";

        if(Client.userType == UserType.ADMIN)
            header = "Вы администратор.";
        else header = "Вы клиент.";
    }

    @FXML
    void onClickExit(ActionEvent event) {
        try {
            ConnectionModule.exit();
            Client.viewManager.setSceneToStage(ViewManager.Views.authorization, "Авторизация");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickProfiles(ActionEvent event) {
        if(Client.userType == UserType.ADMIN)
            Client.viewManager.setSceneToStage(ViewManager.Views.profiles, "Профили");
        else
            Client.viewManager.setSceneToStage(ViewManager.Views.ticketsToBuy, "Корзина");
    }

    @FXML
    void onClickRaises(ActionEvent event) {
            Client.viewManager.setSceneToStage(ViewManager.Views.raises, "Рейсы");
    }

    @FXML
    void onClickReports(ActionEvent event) {
        if(Client.userType == UserType.ADMIN)
            Client.viewManager.setSceneToStage(ViewManager.Views.tickets, "История покупок");
        else
            Client.viewManager.setSceneToStage(ViewManager.Views.tickets, "Мои билеты");
    }

}
