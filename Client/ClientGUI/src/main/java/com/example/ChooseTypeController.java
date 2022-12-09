package com.example;

import connectionModule.ConnectionModule;
import entities.Raise;
import entities.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;

public class ChooseTypeController {
    @FXML
    public  void initialize(){
        updatePage();
    }

    private   void updatePage(){
        btnBuisness.setDisable(Client.isBuisness);
        btnBuisness.setDisable(!Client.isBuisness);
    }
    public void onBuisness(ActionEvent event) {
        Client.isBuisness = true;
        updatePage();
    }
    public void onStandart(ActionEvent event) {
        Client.isBuisness = false;
        updatePage();
    }

    @FXML
    private Button btnBuisness;
    @FXML
    private Button btnStandard;
}
