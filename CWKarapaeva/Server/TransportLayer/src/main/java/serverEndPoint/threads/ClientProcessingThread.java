package serverEndPoint.threads;

import Commands.AuthorizationCommand;
import Commands.Command;
import Commands.Response;
import dbLayer.managers.DataAccessManager;
import entities.*;
import entities.Ticket;
import enums.UserType;
import serverEndPoint.ConnectedClientInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

//В этом потоке происходит взаимодействие с клиентом
public class ClientProcessingThread extends Thread {

    private final DataAccessManager dataAccessManager;

    private final ConnectedClientInfo clientInfo;

    private final ObjectOutputStream objectOutputStream;

    private final ObjectInputStream objectInputStream;

    public ClientProcessingThread(ConnectedClientInfo clientInfo, Connection dbConnection) throws IOException {
        this.clientInfo = clientInfo;
        var socket = clientInfo.getConnectionSocket();
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        dataAccessManager = new DataAccessManager(dbConnection);
    }

    private void sendObject(Serializable object) throws IOException {

        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
    }

    private <T> T receiveObject() throws IOException, ClassNotFoundException {

        return (T) objectInputStream.readObject();
    }

    @Override
    public void run() {

        while (true) {
            try {
                switch (clientLobby()) {
                    case ADMIN, USER -> {
                        processing();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void interrupt() {
        try {
            //Заканчиваем работу
            clientInfo.getConnectionSocket().close();
        } catch (IOException e) { //Аналогично
            throw new RuntimeException(e);
        }
        super.interrupt();
    }

    public ConnectedClientInfo getClientInfo() {
        return clientInfo;
    }

    private UserType clientLobby() throws Exception {

        while (true) {

            AuthorizationCommand command = receiveObject();
            switch (command) {
                case AUTHORIZE -> {
                    String login = receiveObject();
                    String password = receiveObject();
                    var user = dataAccessManager.clientsRepository.get(login, password);
                    if (user.getId() != 0 && user.getStatus() == Status.NOT_BANNED) {
                        sendObject(UserType.USER);
                        clientInfo.setIdInDB(user.getId());
                        clientInfo.setType(UserType.USER);
                        return UserType.USER;
                    }
                    var admin = dataAccessManager.adminsRepository.get(login, password);
                    if (admin.getId() != 0) {
                        sendObject(UserType.ADMIN);
                        clientInfo.setIdInDB(admin.getId());
                        clientInfo.setType(UserType.ADMIN);
                        return UserType.ADMIN;
                    }
                    clientInfo.setIdInDB(0);
                    sendObject(UserType.UNDEFINED);
                }
                case CHECK_IF_LOGIN_EXISTS -> {

                    String login = receiveObject();
                    var user = dataAccessManager.clientsRepository.get(login);
                    var admin = dataAccessManager.adminsRepository.get(login);
                    if (user.getId() == 0 && admin.getId() == 0) {
                        sendObject(Response.NOT_FOUND);
                    } else {
                        sendObject(Response.SUCCESSFULLY);
                    }
                }
                case REGISTER -> {
                    User user = receiveObject();
                    try {
                        int id = dataAccessManager.clientsRepository.create(user);
                        clientInfo.setIdInDB(id);
                        clientInfo.setType(UserType.USER);
                        sendObject(Response.SUCCESSFULLY);
                        return UserType.USER;
                    } catch (Exception e) {
                        sendObject(Response.ERROR);
                    }
                }
                default -> {
                    sendObject(Response.UNKNOWN_COMMAND);
                }
            }
        }
    }

    private void processing() throws IOException, ClassNotFoundException {

        while (true) {

            Command command = receiveObject();
            switch (command) {

                case EXIT -> {
                    return;
                }
                case GET_ALL_RAISES -> {
                    try {
                        var list = dataAccessManager.raisesRepository.getAll();
                        sendObject(new ArrayList<>(list));
                    } catch (SQLException e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case CREATE_RAISE -> {
                    Raise raise = receiveObject();
                    try {
                        dataAccessManager.raisesRepository.create(raise);
                        sendObject(Response.SUCCESSFULLY);
                    } catch (SQLException e) {
                        sendObject(Response.ERROR);
                    }
                }
                case EDIT_RAISE -> {
                    Raise raise = receiveObject();
                    try {
                        dataAccessManager.raisesRepository.update(raise);
                        sendObject(Response.SUCCESSFULLY);
                    } catch (SQLException e) {
                        sendObject(Response.ERROR);
                    }
                }
                case DELETE_RAISE -> {
                    int id = receiveObject();
                    try {
                        dataAccessManager.raisesRepository.delete(id);
                        sendObject(Response.SUCCESSFULLY);
                    } catch (SQLException e) {
                        sendObject(Response.ERROR);
                    }
                }
                case ADD_TO_BUY -> {
                    int id = receiveObject();
                    boolean isBuisness = receiveObject();
                    try {
                        dataAccessManager.ticketsToBuyRepository.addToBuy(id, clientInfo.getIdInDB(), isBuisness);
                        sendObject(Response.SUCCESSFULLY);
                    } catch (SQLException e) {
                        sendObject(Response.ERROR);
                    }
                }
                case GET_ALL_TICKETS_TO_BUY -> {
                    try {
                        var list = dataAccessManager.ticketsToBuyRepository.getAllToBuy(clientInfo.getIdInDB());
                        sendObject(new ArrayList<>(list));
                    } catch (SQLException e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case DELETE_TICKET_TO_BUY-> {
                    int id = receiveObject();
                    try {
                        dataAccessManager.ticketsToBuyRepository.delete(id);
                        sendObject(Response.SUCCESSFULLY);
                    } catch (SQLException e) {
                        sendObject(Response.ERROR);
                    }
                }
                case BUY_TICKET->{
                    Ticket ticket = receiveObject();
                    try {
                        dataAccessManager.ticketsRepository.create(ticket);
                        sendObject(Response.SUCCESSFULLY);
                    } catch (SQLException e) {
                        sendObject(Response.ERROR);
                    }
                }
                case BAN_CLIENT -> {
                    int id = receiveObject();
                    try {
                        var user = dataAccessManager.clientsRepository.getById(id);
                        if (user.getId() == 0) {
                            sendObject(Response.ERROR);
                            continue;
                        }
                        user.setStatus(Status.BANNED);
                        dataAccessManager.clientsRepository.update(user);
                        sendObject(Response.SUCCESSFULLY);
                    } catch (SQLException e) {
                        sendObject(Response.ERROR);
                    }
                }
                case UNBAN_CLIENT -> {
                    int id = receiveObject();
                    try {
                        var user = dataAccessManager.clientsRepository.getById(id);
                        if (user.getId() == 0) {
                            sendObject(Response.ERROR);
                            continue;
                        }
                        user.setStatus(Status.NOT_BANNED);
                        dataAccessManager.clientsRepository.update(user);
                        sendObject(Response.SUCCESSFULLY);
                    } catch (SQLException e) {
                        sendObject(Response.ERROR);
                    }
                }
                case GET_ALL_CURRENT_CLIENT_TICKETS -> {
                    try {
                        var list = dataAccessManager.ticketsRepository.getAll(clientInfo.getIdInDB());
                        sendObject(new ArrayList<>(list));
                    } catch (SQLException e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case GET_ALL_TICKETS -> {
                    try {
                        var list = dataAccessManager.ticketsRepository.getAll();
                        sendObject(new ArrayList<>(list));
                    } catch (SQLException e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case GET_ALL_CLIENTS -> {
                    try {
                        var list = dataAccessManager.clientsRepository.getAll();
                        sendObject(new ArrayList<>(list));
                    } catch (SQLException e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case GET_CURRENT_PROFILE -> {
                    try {
                        switch (clientInfo.getType()){
                            case ADMIN -> {
                                var obj = dataAccessManager.adminsRepository.getById(clientInfo.getIdInDB());
                                sendObject(obj);
                                break;
                            }
                            case USER -> {
                                var obj = dataAccessManager.clientsRepository.getById(clientInfo.getIdInDB());
                                sendObject(obj);
                                break;
                            }
                        }
                    } catch (SQLException e) {
                        sendObject(new User());
                    }
                }
            }
        }
    }
}