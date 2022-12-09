package com.example;
import connectionModule.ConnectionModule;
import entities.Raise;
import entities.TicketToBuy;
import entities.User;
import enums.UserType;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Client extends Application {

    public static ViewManager viewManager;

    public static UserType userType;
    public static boolean isBuisness;

    public static Raise selectedRaise;
    public static TicketToBuy selectedTicketToBuy;

    @Override
    public void start(Stage stage) throws IOException {
        ConnectionModule.connectToServer();
        stage.setResizable(false);
        viewManager = new ViewManager(stage);
        viewManager.setSceneToStage(ViewManager.Views.authorization, "Авторизация");
    }

    public static void main(String[] args) {
        launch();
    }
}
