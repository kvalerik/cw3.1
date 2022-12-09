package com.example;

import connectionModule.ConnectionModule;
import entities.Raise;
import entities.Ticket;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TicketsController {

    boolean isHistory;
    @FXML
    public void initialize(){

        isHistory=false;

        if(Client.userType != UserType.ADMIN){
            btnTop.setVisible(false);
        }

        colFullname.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ticket, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Ticket, String> ticketStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        return ticketStringCellDataFeatures.getValue().getFullname();
                    }
                };
            }
        });
        colSource.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ticket, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Ticket, String> ticketStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        return ticketStringCellDataFeatures.getValue().getTicketToBuy().getRaise().getSourceName();
                    }
                };
            }
        });
        colDestination.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ticket, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Ticket, String> ticketStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        return ticketStringCellDataFeatures.getValue().getTicketToBuy().getRaise().getSourceName();
                    }
                };
            }
        });
        colDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ticket, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Ticket, String> raiseStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        DateFormat fmt = new SimpleDateFormat("HH:mm dd.MM.yyyy");
                        return fmt.format(raiseStringCellDataFeatures.getValue().getTicketToBuy().getRaise().getDate());
                    }
                };
            }
        });
        colType.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ticket, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Ticket, String> raiseStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        return raiseStringCellDataFeatures.getValue().getTicketToBuy().isBuisness()? "Бизнес-класс": "Стандарт";
                    }
                };
            }
        });

        updatePage();
    }

    private void updatePage(){
        ObservableList<Ticket> listEntities = FXCollections.observableArrayList();
        try {
            List<Ticket> list = null;
            if(Client.userType == UserType.ADMIN)
                list = ConnectionModule.getAllTickets();
            else
                list = ConnectionModule.getAllCurrentClientTickets();

            Date date = new Date();

            for (var item: list) {
                if(isHistory && item.getTicketToBuy().getRaise().getDate().getTime() < date.getTime())
                    listEntities.add(item);
                else if(!isHistory&& item.getTicketToBuy().getRaise().getDate().getTime() > date.getTime())
                    listEntities.add(item);
            }
        } catch (Exception e) {
            AlertManager.showErrorAlert("Ошибка", "Ошбика соединения");
        }
        tableTickets.setItems(listEntities);
    }
    @FXML
    private Button aboutButton;

    @FXML
    private Button rulesButton;
    @FXML
    private Button btnHistory;
    @FXML
    private Button reportButton;
    @FXML Button btnTop;

    @FXML
    private TableView<Ticket> tableTickets;
    @FXML
    private TableColumn<Ticket, String> colFullname;
    @FXML
    private TableColumn<Ticket, String> colType;
    @FXML
    private TableColumn<Ticket, String> colDate;
    @FXML
    private TableColumn<Ticket, String> colDestination;
    @FXML
    private TableColumn<Ticket, String> colSource;

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
        Client.viewManager.setSceneToStage(ViewManager.Views.menu, "Меню");
    }

    @FXML
    void onClickRules(ActionEvent event) {
        AlertManager.showInformationAlert("Правила полетов.", "при тряске, сильной вибрации, попадании в воздушные ямы следуйте указаниям членов экипажа самолета;\n" +
                "застегните ремни безопасности;\n" +
                "оставайтесь на своих местах, пригните голову к коленям и обхватите ее руками. Ноги уприте в пол, выдвинув их как можно дальше, но не под переднее кресло;\n" +
                "ни при каких обстоятельствах не покидайте своего места до нормализации полета или полной остановки самолета.");
    }

    @FXML
    void onHistory(ActionEvent event) {
        isHistory = !isHistory;
        btnHistory.setText(isHistory? "История": "Текущие");
        updatePage();
    }

    @FXML
    void onClickReport(ActionEvent event) {
        if(Client.userType == UserType.USER){
            Ticket ticket = tableTickets.getSelectionModel().getSelectedItem();
            if(ticket == null) {
                AlertManager.showInformationAlert("Сначала выберите билет", "");
                return;
            }
            try {
                FileOutputStream out = new FileOutputStream(ticket.getFullname() +" билет в "+ ticket.getTicketToBuy().getRaise().getDestinationName() +".txt");
                String text = "ФИО: "+ticket.getFullname()+"\n";
                text += "Паспорт: " + ticket.getPasport() + "" + ticket.getSeries() + "\n";
                text += "Откуда: " + ticket.getTicketToBuy().getRaise().getSourceName() + "\n";
                text += "Куда: " + ticket.getTicketToBuy().getRaise().getDestinationName() + "\n";
                DateFormat fmt = new SimpleDateFormat("HH:mm dd.MM.yyyy");
                text+= "Дата: " + fmt.format(ticket.getTicketToBuy().getRaise().getDate()) + "\n";
                text +="Тип: " + (ticket.getTicketToBuy().isBuisness()? "Бизнес-класс": "Стандарт") + "\n";
                float cost = ticket.getTicketToBuy().getRaise().getCostStandart();
                if(ticket.getTicketToBuy().isBuisness())
                    ticket.getTicketToBuy().getRaise().getCostBuisiness();
                text +="Стоимость: " + cost + "руб." + "\n";

                out.write(text.getBytes());
                out.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        else{
            try {
                Date date = new Date();
                var list = ConnectionModule.getAllTickets();
                FileOutputStream out = new FileOutputStream("AdminReport.txt");
                for(var ticket : list){
                    String text = "ФИО: "+ticket.getFullname()+"\n";
                    text += "Паспорт: " + ticket.getPasport() + "" + ticket.getSeries() + "\n";
                    text += "Откуда: " + ticket.getTicketToBuy().getRaise().getSourceName() + "\n";
                    text += "Куда: " + ticket.getTicketToBuy().getRaise().getDestinationName() + "\n";
                    DateFormat fmt = new SimpleDateFormat("HH:mm dd.MM.yyyy");
                    text+= "Дата: " + fmt.format(ticket.getTicketToBuy().getRaise().getDate()) + "\n";
                    text +="Тип: " + (ticket.getTicketToBuy().isBuisness()? "Бизнес-класс": "Стандарт") + "\n";
                    float cost = ticket.getTicketToBuy().getRaise().getCostStandart();
                    if(ticket.getTicketToBuy().isBuisness())
                        ticket.getTicketToBuy().getRaise().getCostBuisiness();
                    text +="Стоимость: " + cost + "руб." + "\n\n\n";
                    if(isHistory && ticket.getTicketToBuy().getRaise().getDate().getTime() < date.getTime())
                        out.write(text.getBytes());
                    else if(!isHistory&& ticket.getTicketToBuy().getRaise().getDate().getTime() > date.getTime())
                        out.write(text.getBytes());

                }
                out.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML
    void onTop(){
        List<Raise> purposes = null;
        try {
            purposes = ConnectionModule.getAllRaises();
            List<Ticket> tickets = ConnectionModule.getAllTickets();
            int[] counts = new int[purposes.size()];
            for(int i=0; i<purposes.size();i++){
                counts[i] = 0;
            }

            int countBuisnes = 0;
            int countStandart = 0;

            for(var record : tickets){
                for(int i=0;i<purposes.size();i++){
                    if(record.getTicketToBuy().getRaise().getId() == purposes.get(i).getId()){
                        counts[i]++;
                    }
                    if(record.getTicketToBuy().isBuisness()){
                        countBuisnes++;
                    }
                    else
                        countStandart++;
                }
            }

            for(int i=0; i<purposes.size();i++){
                for(int j=i+1; j< purposes.size(); j++){
                    if(counts[i] < counts[j]){
                        Raise temp = purposes.get(i);
                        purposes.set(i, purposes.get(j));
                        purposes.set(j, temp);

                        int tempCount = counts[i];
                        counts[i] = counts[j];
                        counts[j] = tempCount;
                    }
                }
            }

            FileOutputStream out = new FileOutputStream("AdminTop.txt");
            String headerText = "Статистика популярности рейсов (Всего записей "+ tickets.size() +"):\n";
            out.write(headerText.getBytes());
            for (int i=0; i<purposes.size(); i++){
                DateFormat fmt = new SimpleDateFormat("HH:mm dd.MM.yyyy");
                String text = purposes.get(i).getSourceName() + " - " + purposes.get(i).getDestinationName() + " (" +fmt.format( purposes.get(i).getDate()) +"): " + counts[i] + "(" + ((double)counts[i])/tickets.size() + "%)\n\n\n";
                out.write(text.getBytes());
            }
            String text = "Кол-во билетов бизнс-класс: "+ countBuisnes + "\n";
            text += "Кол-во билетов бизнс-класс: "+ countStandart + "\n";
            out.write(text.getBytes());
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
