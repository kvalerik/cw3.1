package com.example;

import com.example.AlertManager;
import com.example.Client;
import connectionModule.ConnectionModule;
import entities.Raise;
import entities.TicketToBuy;
import enums.UserType;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TicketsToBuyController {
    @FXML
    public  void initialize(){
        if(Client.userType == UserType.ADMIN){

        }
        else{

        }

        colSource.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TicketToBuy, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TicketToBuy, String> ticketToBuyStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        return ticketToBuyStringCellDataFeatures.getValue().getRaise().getSourceName();
                    }
                };
            }
        });
        colDestination.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TicketToBuy, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TicketToBuy, String> ticketToBuyStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        return ticketToBuyStringCellDataFeatures.getValue().getRaise().getDestinationName();
                    }
                };
            }
        });
        colCost.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TicketToBuy, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TicketToBuy, String> raiseStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        float cost = raiseStringCellDataFeatures.getValue().isBuisness()? raiseStringCellDataFeatures.getValue().getRaise().getCostBuisiness() : raiseStringCellDataFeatures.getValue().getRaise().getCostStandart();
                        return String.valueOf(cost) + "руб.";
                    }
                };
            }
        });
        colType.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TicketToBuy, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TicketToBuy, String> ticketToBuyStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        return ticketToBuyStringCellDataFeatures.getValue().isBuisness()? "Бизнес-класс": "Стандарт";
                    }
                };
            }
        });
        colDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TicketToBuy, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<TicketToBuy, String> raiseStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        DateFormat fmt = new SimpleDateFormat("HH:mm dd.MM.yyyy");
                        return fmt.format(raiseStringCellDataFeatures.getValue().getRaise().getDate());
                    }
                };
            }
        });

        updatePage();
    }
    @FXML
    public  void updatePage() {
        try {
            var raises = ConnectionModule.getAllTicketsToBuy();
            ObservableList<TicketToBuy> list = FXCollections.observableArrayList();

            for(var item: raises){
                boolean bShow = true;


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
    private TableView<TicketToBuy> tableRaises;
    @FXML
    private TableColumn<TicketToBuy, String> colType;
    @FXML
    private TableColumn<TicketToBuy, String> colCost;
    @FXML
    private TableColumn<TicketToBuy, String> colDate;

    @FXML
    private TableColumn<TicketToBuy, String> colDestination;

    @FXML
    private TableColumn<TicketToBuy, String> colSource;
    @FXML
    private Button buyButton;

    @FXML
    private Button delButton;

    @FXML
    private Button exitButton;

    @FXML
    void onClickBuy(ActionEvent event) {
        var item = tableRaises.getSelectionModel().getSelectedItem();
        if(item == null)
            return;

        Client.selectedTicketToBuy = item;

        Client.viewManager.setSceneToStage(ViewManager.Views.buy, "Оформление покупки");
    }

    @FXML
    void onClickDel(ActionEvent event) {
        var item = tableRaises.getSelectionModel().getSelectedItem();
        if(item == null)
            return;

        try {
            ConnectionModule.deleteTicketToBuy(item.getId());
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка сервера!", "");
        }
    }

    @FXML
    void onClickExit(ActionEvent event) {

        Client.viewManager.setSceneToStage(ViewManager.Views.menu, "Меню");
    }

}
