package connectionModule;

import Commands.AuthorizationCommand;
import Commands.Command;
import Commands.Response;
import entities.*;
import entities.Ticket;
import enums.UserType;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Properties;

public class ConnectionModule {

    private static Socket connectionSocket;
    private static final String serverIp;
    private static final int serverPort;
    private static ObjectOutputStream objectOutputStream;
    private static ObjectInputStream objectInputStream;

    private static Properties getPropertiesFromConfig() throws IOException {

        var properties = new Properties();
        String propFileName = "Client/ConnectionModule/src/main/resources/config.properties";
        var inputStream = new FileInputStream(propFileName);
        if (inputStream == null)
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        properties.load(inputStream);
        return properties;
    }

    static {
        try {
            var properties = getPropertiesFromConfig();
            serverIp = properties.getProperty("serverIp");
            serverPort = Integer.parseInt(properties.getProperty("serverPort"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean connectToServer() throws IOException {

        connectionSocket = new Socket(serverIp, serverPort);
        //connectionSocket.setSoTimeout(3000);
        if (!connectionSocket.isConnected()) return false;
        objectOutputStream = new ObjectOutputStream(connectionSocket.getOutputStream());
        objectInputStream = new ObjectInputStream(connectionSocket.getInputStream());
        return true;
    }

    private static void sendObject(Serializable object) throws IOException {

        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
    }

    private static  <T> T receiveObject() throws Exception {

        return (T) objectInputStream.readObject();
    }

    public static UserType singUp(String login, String password) throws Exception {
        sendObject(AuthorizationCommand.AUTHORIZE);
        sendObject(login);
        sendObject(password);
        return receiveObject();
    }

    public static Response registration(User user) throws Exception {

        sendObject(AuthorizationCommand.REGISTER);
        sendObject(user);
        return receiveObject();
    }

    //ТОЛЬКО ПРИ РЕГИСТРАЦИИ
    public static boolean checkIfLoginExists(String login) throws Exception {

        sendObject(AuthorizationCommand.CHECK_IF_LOGIN_EXISTS);
        sendObject(login);
        Response response = receiveObject();
        return response == Response.SUCCESSFULLY;
    }

    public static void exit() throws IOException {
        sendObject(Command.EXIT);
    }
    public static List<Raise> getAllRaises() throws Exception {
        sendObject(Command.GET_ALL_RAISES);
        return receiveObject();
    }
    public static List<TicketToBuy> getAllTicketsToBuy() throws Exception {
        sendObject(Command.GET_ALL_TICKETS_TO_BUY);
        return receiveObject();
    }

    public static Response createRaise(Raise raise) throws Exception {
        sendObject(Command.CREATE_RAISE);
        sendObject(raise);
        return receiveObject();
    }

    public static Response editRaise(Raise raise) throws Exception {
        sendObject(Command.EDIT_RAISE);
        sendObject(raise);
        return receiveObject();
    }

    public static Response deleteRaise(int raiseId) throws Exception {
        sendObject(Command.DELETE_RAISE);
        sendObject(raiseId);
        return receiveObject();
    }
    public static Response addToBuy(int raiseId, boolean isBuisness) throws Exception {
        sendObject(Command.ADD_TO_BUY);
        sendObject(raiseId);
        sendObject(isBuisness);
        return receiveObject();
    }
    public static Response deleteTicketToBuy(int id) throws Exception {
        sendObject(Command.DELETE_TICKET_TO_BUY);
        sendObject(id);
        return receiveObject();
    }
    public static Response buyTicket(Ticket ticket) throws Exception {
        sendObject(Command.BUY_TICKET);
        sendObject(ticket);
        return receiveObject();
    }

    public static Response banClient(int clientId) throws Exception {
        sendObject(Command.BAN_CLIENT);
        sendObject(clientId);
        return receiveObject();
    }

    public static Response unbanClient(int clientId) throws Exception {
        sendObject(Command.UNBAN_CLIENT);
        sendObject(clientId);
        return receiveObject();
    }

    public static List<Ticket> getAllCurrentClientTickets() throws Exception {
        sendObject(Command.GET_ALL_CURRENT_CLIENT_TICKETS);
        return receiveObject();
    }

    public static List<Ticket> getAllTickets() throws Exception {
        sendObject(Command.GET_ALL_TICKETS);
        return receiveObject();
    }

    public static List<User> getAllClients() throws Exception {
        sendObject(Command.GET_ALL_CLIENTS);
        return receiveObject();
    }

    public static User getCurrentProfile() throws Exception {
        sendObject(Command.GET_CURRENT_PROFILE);
        return receiveObject();
    }
}