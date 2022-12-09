package com.example;

import Commands.Response;
import connectionModule.ConnectionModule;
import entities.Raise;
import enums.UserType;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.security.AllPermission;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RaisesController {

    public static int countHot;

    @FXML
    public  void initialize(){
        if(Client.userType == UserType.ADMIN){

        }
        else{
            addButton.setText("В корзину");
            editButton.setVisible(false);
            buttonDel.setVisible(false);
        }

        countHot =2;
        colSource.setCellValueFactory(new PropertyValueFactory<Raise, String>("sourceName"));
        colDestination.setCellValueFactory(new PropertyValueFactory<Raise, String>("destinationName"));
        colCostStandart.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Raise, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Raise, String> raiseStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        return String.valueOf(raiseStringCellDataFeatures.getValue().getCostStandart());
                    }
                };
            }
        });
        colCostBusiness.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Raise, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Raise, String> raiseStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        return String.valueOf(raiseStringCellDataFeatures.getValue().getCostBuisiness());
                    }
                };
            }
        });
        colDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Raise, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Raise, String> raiseStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        DateFormat fmt = new SimpleDateFormat("HH:mm dd.MM.yyyy");
                        return fmt.format(raiseStringCellDataFeatures.getValue().getDate());
                    }
                };
            }
        });
        colRecomendation.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Raise, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Raise, String> raiseStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        if(RaisesController.countHot > 0)
                        {
                            countHot--;
                            return "HOT!";
                        }
                        return "";
                    }
                };
            }
        });

        updatePage();
    }
    @FXML
    public  void updatePage() {
        try {
            var raises = ConnectionModule.getAllRaises();
            ObservableList<Raise> list = FXCollections.observableArrayList();

            String search = searchInput.getText();

            for(var item: raises){
                boolean bShow = true;

                if(!search.isEmpty()){
                    bShow = item.getDestinationName().contains(search) || item.getSourceName().contains(search);
                }
                Date date = new Date();
                if(item.getDate().getTime() < date.getTime() + 3600000)
                    bShow = false;

                if(bShow){
                    list.add(item);
                }
            }

            tableRaises.setItems(list);

        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка соединения", "");
        }
    }
    @FXML
    private TextField searchInput;
    @FXML
    private TableView<Raise> tableRaises;
    @FXML
    private TableColumn<Raise, String> colCostStandart;
    @FXML
    private TableColumn<Raise, String> colCostBusiness;
    @FXML
    private TableColumn<Raise, String> colDate;

    @FXML
    private TableColumn<Raise, String> colDestination;

    @FXML
    private TableColumn<Raise, String> colRecomendation;

    @FXML
    private TableColumn<Raise, String> colSource;
    @FXML
    private Button addButton;

    @FXML
    private Button buttonDel;

    @FXML
    private Button editButton;

    @FXML
    void onClickAdd(ActionEvent event) {
        if(Client.userType == UserType.ADMIN){
            Client.selectedRaise = null;
            Client.viewManager.setSceneToStage(ViewManager.Views.addEditRaise, "Добавление рейса");
        }
        else{
            Raise raise = tableRaises.getSelectionModel().getSelectedItem();
            if(raise == null)
                return;

            try {
                if(ConnectionModule.addToBuy(raise.getId(), Client.isBuisness) == Response.SUCCESSFULLY){
                    AlertManager.showInformationAlert("Успешно добавлено в корзину!", "");
                }
            } catch (Exception e) {
                AlertManager.showErrorAlert("Ошибка", "Данный рейс уже в корзине");
            }
        }
    }

    @FXML
    void onClickDel(ActionEvent event) {
        Raise raise = tableRaises.getSelectionModel().getSelectedItem();
        if(raise == null)
            return;

        try {
            ConnectionModule.deleteRaise(raise.getId());
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка сервера!", "");
        }
    }

    @FXML
    void onClickEdit(ActionEvent event) {
        Raise raise = tableRaises.getSelectionModel().getSelectedItem();
        if(raise == null)
            return;

        Client.selectedRaise = raise;
        Client.viewManager.setSceneToStage(ViewManager.Views.addEditRaise, "Редактирование рейса");
    }

    @FXML
    void onClickExit(ActionEvent event) {
        Client.viewManager.setSceneToStage(ViewManager.Views.menu, "Меню");
    }

    public void onKeyTypedSearch(KeyEvent keyEvent) {
        updatePage();
    }
}
