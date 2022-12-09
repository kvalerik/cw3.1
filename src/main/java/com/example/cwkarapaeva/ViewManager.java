package com.example.cwkarapaeva;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ViewManager {

    public enum Views{
        authorization,
        registration,
        buy,
        profiles,
        raises,
        tickets,
        ticketsToBuy,
        menu,
        addEditRaise
    }

    private class ViewInfo{
        public final String name;
        public final double width;
        public final double height;

        public ViewInfo(String name, double width, double height){
            this.name = name;
            this.width = width;
            this.height = height;
        }
    }

    private Map<Views, ViewInfo> views;
    private final String pathPrefix = "";
    private final Stage stage;
    public <ControllerType> Pair<ControllerType, Scene> getScene(Views view) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(pathPrefix + views.get(view).name));
            Scene scene = new Scene(fxmlLoader.load(), views.get(view).width, views.get(view).height);
            ControllerType controller = fxmlLoader.getController();
            return new Pair<>(controller, scene);
        } catch (IOException e) {
            //AlertManager.showErrorAlert("View loading error", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public <ControllerType, ItemType> Pair<ControllerType, ItemType> getItem(String itemName) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ClientGUI.class.getResource(pathPrefix + itemName));
            ItemType item = fxmlLoader.load();
            ControllerType controller = fxmlLoader.getController();
            return new Pair<>(controller, item);
        } catch (IOException e) {
            //AlertManager.showErrorAlert("View loading error", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void setSceneToStage(Views view, String title) {
        Scene scene = getScene(view).getValue();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public ViewManager(Stage stage) {
        this.stage = stage;
        this.stage.setResizable(false);

        views = new HashMap<Views, ViewInfo>();

        views.put(Views.authorization, new ViewInfo("Authorization.fxml", 600, 400));
        views.put(Views.registration, new ViewInfo("Registration.fxml", 600, 400));
        views.put(Views.buy, new ViewInfo("Buy.fxml", 600, 400));
        views.put(Views.profiles, new ViewInfo("Profiles.fxml", 600, 400));
        views.put(Views.raises, new ViewInfo("Raises.fxml", 600, 400));
        views.put(Views.tickets, new ViewInfo("Tickets.fxml", 600, 400));
        views.put(Views.ticketsToBuy, new ViewInfo("TicketsToBuy.fxml", 600, 400));
        views.put(Views.menu, new ViewInfo("Menu.fxml", 600, 400));
        views.put(Views.addEditRaise, new ViewInfo("AddEditRaise.fxml", 600, 400));
    }
}
