package com.example;

import connectionModule.ConnectionModule;
import entities.Raise;
import entities.Status;
import entities.Ticket;
import entities.User;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.List;

public class ProfilesController {

    @FXML
    public void initialize(){
        colLogin.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> userStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        return userStringCellDataFeatures.getValue().getLogin();
                    }
                };
            }
        });
        colStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> userStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        return userStringCellDataFeatures.getValue().getStatus() == Status.BANNED? "Заблокирован": "Активен";
                    }
                };
            }
        });
        colDiscount.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> userStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        List<Ticket> list = null;
                        try {
                            list  = ConnectionModule.getAllTickets();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        int count = 0;
                        for(var item : list){
                            if(item.getTicketToBuy().getUser().getId() == userStringCellDataFeatures.getValue().getId())
                                count++;
                        }
                        count*=5;
                        if(count > 40)
                            count = 40;
                        return Integer.toString(count)+ "%";
                    }
                };
            }
        });
    }
    private void updatePage(){
        ObservableList<User> users = FXCollections.observableArrayList();

        try {
            var list = ConnectionModule.getAllClients();
            for (var item: list) {
                users.add(item);
            }
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка", "Ошбика соединения");
        }

        profilesTable.setItems(users);
    }
    @FXML
    private TableView<User> profilesTable;

    @FXML
    private TableColumn<User, String> colLogin;

    @FXML
    private TableColumn<User, String> colStatus;

    @FXML
    private TableColumn<User, String> colDiscount;
    @FXML
    void onClickBlock(ActionEvent event) {
        var user = profilesTable.getSelectionModel().getSelectedItem();
        if(user != null){
            user.setStatus(Status.BANNED);
            try {
                ConnectionModule.banClient(user.getId());
            } catch (Exception e) {
                AlertManager.showErrorAlert("Ошибка", "Ошбика соединения");
            }
        }
        updatePage();
    }
    @FXML
    void onClickUnlock(ActionEvent event) {
        var user = profilesTable.getSelectionModel().getSelectedItem();
        if(user != null){
            user.setStatus(Status.NOT_BANNED);
            try {
                ConnectionModule.unbanClient(user.getId());
            } catch (Exception e) {
                AlertManager.showErrorAlert("Ошибка", "Ошбика соединения");
            }
        }
        updatePage();
    }

    @FXML
    void onClickExit(ActionEvent event) {
        Client.viewManager.setSceneToStage(ViewManager.Views.menu, "Меню");
    }

}
