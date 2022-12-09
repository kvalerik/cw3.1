package dbLayer.repositories;

import dbLayer.managers.DataAccessManager;
import entities.Ticket;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TicketsRepository {

    private final Connection dbConnection;

    public TicketsRepository(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    private Ticket convertResultSetToSingleObj(ResultSet resultSet) throws SQLException {

        resultSet.beforeFirst();
        if (!resultSet.next()) return new Ticket();
        return convertResultSetToObj(resultSet);
    }

    private Ticket convertResultSetToObj(ResultSet resultSet) throws SQLException {

        var obj = new Ticket();
        obj.setId(resultSet.getInt("id"));
        var access = new DataAccessManager(dbConnection);
        obj.setTicketToBuy(access.ticketsToBuyRepository.getById(resultSet.getInt("clientsRaisesId")));
        obj.setFullname(resultSet.getString("fullname"));
        obj.setPasport(resultSet.getString("pasport"));
        obj.setSeries(resultSet.getString("series"));
        return obj;
    }

    private List<Ticket> convertResultSetToList(ResultSet resultSet) throws SQLException {

        var list = new ArrayList<Ticket>();
        resultSet.beforeFirst();
        while (resultSet.next()) {

            list.add(convertResultSetToObj(resultSet));
        }
        return list;
    }

    private int getMaxId() throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT MAX(id) from tickets;");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    public int create(Ticket obj) throws SQLException {

        var insertStatement = dbConnection.prepareStatement(
                "INSERT INTO tickets (clientsRaisesId, fullname, pasport, series) " +
                        "values (?, ?, ?, ?)");

        insertStatement.setInt(1, obj.getTicketToBuy().getId());
        insertStatement.setString(2, obj.getFullname());
        insertStatement.setString(3, obj.getPasport());
        insertStatement.setString(4, obj.getSeries());
        insertStatement.executeUpdate();
        return getMaxId();
    }

    public void update(Ticket obj) throws SQLException {

        var updateStatement = dbConnection.prepareStatement(
                "UPDATE tickets SET clientsRaisesId=?, fullname=?, pasport=?, series=? where id = ?");
        updateStatement.setInt(1, obj.getTicketToBuy().getId());
        updateStatement.setString(2, obj.getFullname());
        updateStatement.setString(3, obj.getPasport());
        updateStatement.setString(4, obj.getSeries());
        updateStatement.executeUpdate();
    }

    public void delete(int id) throws SQLException {

        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from tickets where id=?");
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
    }

    public Ticket getById(int id) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM tickets where id = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, id);
        statement.executeQuery();
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    public List<Ticket> getAll() throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM tickets;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.executeQuery();
        return convertResultSetToList(statement.getResultSet());
    }

    public List<Ticket> getAll(int clientId) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM tickets WHERE clientsRaisesId IN(SELECT id FROM clients_raises WHERE clientId = ?);",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, clientId);
        statement.executeQuery();
        return convertResultSetToList(statement.getResultSet());
    }
}
