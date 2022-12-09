package dbLayer.repositories;

import entities.Raise;
import entities.Status;
import entities.TicketToBuy;
import entities.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TicketsToBuyRepository {

    private final Connection dbConnection;

    public TicketsToBuyRepository(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    private TicketToBuy convertResultSetToSingleObj(ResultSet resultSet) throws SQLException {

        resultSet.beforeFirst();
        if (!resultSet.next()) return new TicketToBuy();
        return convertResultSetToObj(resultSet);
    }

    private TicketToBuy convertResultSetToObj(ResultSet resultSet) throws SQLException {

        var obj = new TicketToBuy();
        obj.setId(resultSet.getInt("id"));
        obj.setBuisness(resultSet.getInt("isBuisness") == 1);
        obj.setUser(new User());
        obj.getUser().setLogin(resultSet.getString("login"));
        obj.getUser().setPassword(resultSet.getString("password"));
        obj.getUser().setId(resultSet.getInt("clientId"));
        obj.getUser().setStatus(resultSet.getInt("status")==0?Status.BANNED:Status.NOT_BANNED);
        obj.setRaise(new Raise());
        obj.getRaise().setId(resultSet.getInt("raiseId"));
        obj.getRaise().setCostBuisiness(resultSet.getFloat("costBuisness"));
        obj.getRaise().setCostStandart(resultSet.getFloat("costStandart"));
        obj.getRaise().setSourceName(resultSet.getString("sourceName"));
        obj.getRaise().setDestinationName(resultSet.getString("destinationName"));
        Date date = new Date();
        var time = resultSet.getTimestamp("date");
        date.setTime(time.getTime());
        obj.getRaise().setDate(date);

        return obj;
    }

    private List<TicketToBuy> convertResultSetToList(ResultSet resultSet) throws SQLException {

        var list = new ArrayList<TicketToBuy>();
        resultSet.beforeFirst();
        while (resultSet.next()) {

            list.add(convertResultSetToObj(resultSet));
        }
        return list;
    }

    private int getMaxId() throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT MAX(id) from clients_raises;");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    public void addToBuy(int id, int clientId, boolean isBuisness) throws SQLException {

        var addStatement = dbConnection.prepareStatement(
                "INSERT INTO clients_raises (raiseId, clientId, isBuisness) VALUES (?, ?, ?)");
        addStatement.setInt(1, id);
        addStatement.setInt(2, clientId);
        addStatement.setInt(3, isBuisness? 1:0);
        addStatement.executeUpdate();
    }
    public List<TicketToBuy> getAllToBuy(int clientId) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM clients_raises INNER JOIN raises r ON client_raises.raiseId=r.id INNER JOIN clients c ON client_raises.clientId=c.id WHERE clientId = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, clientId);
        statement.executeQuery();
        return convertResultSetToList(statement.getResultSet());
    }

    public TicketToBuy getById(int id) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM clients_raises INNER JOIN raises r ON client_raises.raiseId=r.id INNER JOIN clients c ON client_raises.clientId=c.id WHERE id = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, id);
        statement.executeQuery();
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    public List<TicketToBuy> getAll() throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM clients_raises INNER JOIN raises r ON client_raises.raiseId=r.id INNER JOIN clients c ON client_raises.clientId=c.id;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.executeQuery();
        return convertResultSetToList(statement.getResultSet());
    }

    public void delete(int id) throws SQLException {

        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from clients_raises where id=?");
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
    }
}
