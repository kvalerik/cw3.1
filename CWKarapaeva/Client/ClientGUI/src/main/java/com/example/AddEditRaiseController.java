package com.example;

import com.example.Client;
import connectionModule.ConnectionModule;
import entities.Raise;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AddEditRaiseController {

    @FXML
    public void initialize(){

        timeSlider.setMax(1000 * 60 * 60 * 24 - 1);
        timeSlider.setMin(0);

        if(Client.selectedRaise != null){
            sourceInput.setText(Client.selectedRaise.getSourceName());
            destinationInput.setText(Client.selectedRaise.getDestinationName());
            buisinesCostInput.setText(String.valueOf(Client.selectedRaise.getCostBuisiness()));
            standardCostInput.setText(String.valueOf(Client.selectedRaise.getCostStandart()));
            dateInput.setValue(Client.selectedRaise.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            timeSlider.setValue(Client.selectedRaise.getDate().getTime()%(1000*60*60*24));
            sourceInput.setText(Client.selectedRaise.getSourceName());
        }

        timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue,
                    Number newValue) {
                long time = newValue.intValue();
                time /= 60000;
                long hours = time / 60;
                long minuts = time % 60;
                timeLabel.setText(hours + ":" + (minuts < 10? "0": "") + minuts);
            }
        });
    }
    @FXML
    private TextField buisinesCostInput;

    @FXML
    private DatePicker dateInput;

    @FXML
    private TextField destinationInput;

    @FXML
    private TextField sourceInput;

    @FXML
    private TextField standardCostInput;

    @FXML
    private Label timeLabel;

    @FXML
    private Slider timeSlider;

    @FXML
    void onClickCancel(ActionEvent event) {
        Client.viewManager.setSceneToStage(ViewManager.Views.raises, "Рейсы");
    }

    @FXML
    void onClickOK(ActionEvent event) {
        String sourceName = sourceInput.getText();
        String destinationName = destinationInput.getText();
        String standartCost = standardCostInput.getText();
        String buisnessCost = buisinesCostInput.getText();
        Date date =Date.from(dateInput.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        long time = (long)timeSlider.getValue();

        if(sourceName.isEmpty() ||destinationName.isEmpty() || standartCost.isEmpty() || buisnessCost.isEmpty() || date == null || time == 0){
            AlertManager.showErrorAlert("Ошибка", "Заполните все поля");
            return;
        }

        float standart = 0;
        float buisness = 0;
        try
        {
            standart = Float.parseFloat(standartCost);
            buisness = Float.parseFloat(buisnessCost);
            if(standart <= 0 || buisness<= 0)
                throw new NumberFormatException();
        }
        catch (NumberFormatException e){
            AlertManager.showWarningAlert("Ошибка!", "Введите положительное число в графу цены");
            return;
        }

        date.setTime(date.getTime() + time);
        Date now = new Date();
        if(now.getTime() + 360000 > date.getTime()){
            AlertManager.showWarningAlert("Ошибка!", "Введите время позже!");
            return;
        }
        Raise raise = new Raise();
        raise.setDestinationName(destinationName);
        raise.setSourceName(sourceName);
        raise.setCostStandart(standart);
        raise.setCostBuisiness(buisness);
        raise.setDate(date);

        try {
            ConnectionModule.createRaise(raise);
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка сервера!", "");
            return;
        }

        Client.viewManager.setSceneToStage(ViewManager.Views.raises, "Рейсы");
    }

    @FXML
    void onDragDone(DragEvent event) {

    }

}
