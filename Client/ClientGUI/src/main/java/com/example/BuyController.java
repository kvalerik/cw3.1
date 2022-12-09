package com.example;

import connectionModule.ConnectionModule;
import entities.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class BuyController {

    @FXML
    private TextField fioInput;

    @FXML
    private TextField numberInput;

    @FXML
    private TextField pasportInput;

    @FXML
    void onClickApply(ActionEvent event) {
        String fio = fioInput.getText();
        String pasport = pasportInput.getText();
        String series = numberInput.getText();

        if(fio.isEmpty() || pasport.isEmpty() || series.isEmpty()){
            AlertManager.showErrorAlert("Ошибка!", "Введите все поля");
            return;
        }
        if(pasport.length() <= 10 || pasport.length() >= 50){
            AlertManager.showErrorAlert("Ошибка!", "Паспортные данные должны содержать от 11 до 49 символов");
            return;
        }
        if(series.length() !=9){
            AlertManager.showErrorAlert("Ошибка!", "Серийный номер должен быть в формате AZ1234567");
            return;
        }

        boolean isCorrect = true;
        for(int i=0;i<series.length();i++){
            if(i<=1 && (!(series.charAt(i)>='a'&& series.charAt(i)<='z')||!(series.charAt(i)>='A'&& series.charAt(i)<='Z'))){
                isCorrect = false;
                break;
            }
            if(i>1 && (series.charAt(i)<'0'||series.charAt(i) > '9')){
                isCorrect=false;
            }
        }

        if(!isCorrect){
            AlertManager.showErrorAlert("Ошибка!", "Серийный номер должен быть в формате AZ1234567");
            return;
        }
        Ticket ticket = new Ticket(0, Client.selectedTicketToBuy, fio, pasport, series);

        try {
            ConnectionModule.buyTicket(ticket);
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка сервера!", "");
        }

        Client.viewManager.setSceneToStage(ViewManager.Views.ticketsToBuy, "Корзина");
    }

    @FXML
    void onClickCancel(ActionEvent event) {
        Client.viewManager.setSceneToStage(ViewManager.Views.ticketsToBuy, "Корзина");
    }

}
