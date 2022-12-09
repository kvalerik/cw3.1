package dbLayer.repositories;

import entities.Raise;
import entities.TicketToBuy;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RaisesRepository {

    private final Connection dbConnection;

    public RaisesRepository(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    private Raise convertResultSetToSingleObj(ResultSet resultSet) throws SQLException {

        resultSet.beforeFirst();
        if (!resultSet.next()) return new Raise();
        return convertResultSetToObj(resultSet);
    }

    private Raise convertResultSetToObj(ResultSet resultSet) throws SQLException {

        var obj = new Raise();
        obj.setId(resultSet.getInt("id"));
        obj.setSourceName(resultSet.getString("sourceName"));
        obj.setDestinationName(resultSet.getString("destinationName"));
        obj.setCostBuisiness(resultSet.getFloat("costBuisness"));
        obj.setCostStandart(resultSet.getFloat("costStandart"));
        var timestamp = resultSet.getTimestamp("date");
        Date date = new Date();
        date.setTime(timestamp.getTime());
        obj.setDate(date);
        return obj;
    }

    private List<Raise> convertResultSetToList(ResultSet resultSet) throws SQLException {

        var list = new ArrayList<Raise>();
        resultSet.beforeFirst();
        while (resultSet.next()) {

            list.add(convertResultSetToObj(resultSet));
        }
        return list;
    }

    private int getMaxId() throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT MAX(id) from raises;");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    public int create(Raise obj) throws SQLException {

        var insertStatement = dbConnection.prepareStatement(
                "INSERT INTO raises (sourceName, destinationName, date, costBuisness, costStandart) " +
                        "values (?, ?, ?, ?, ?)");

        insertStatement.setString(1, obj.getSourceName());
        insertStatement.setString(2, obj.getDestinationName());
        insertStatement.setTimestamp(3, new Timestamp(obj.getDate().getTime()));
        insertStatement.setFloat(4, obj.getCostBuisiness());
        insertStatement.setFloat(5, obj.getCostStandart());
        insertStatement.executeUpdate();
        return getMaxId();
    }

    public void update(Raise obj) throws SQLException {

        var updateStatement = dbConnection.prepareStatement(
                "UPDATE raises SET sourceName=?, destinationName=?, date=?, costBuisness=?, costStandart=? where id = ?");
        updateStatement.setString(1, obj.getSourceName());
        updateStatement.setString(2, obj.getDestinationName());
        updateStatement.setTimestamp(3, new Timestamp(obj.getDate().getTime()));
        updateStatement.setFloat(4, obj.getCostBuisiness());
        updateStatement.setFloat(5, obj.getCostStandart());
        updateStatement.setInt(6, obj.getId());
        updateStatement.executeUpdate();
    }

    public void delete(int id) throws SQLException {

        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from raises where id=?");
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
    }
    public Raise getById(int id) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM raises where id = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, id);
        statement.executeQuery();
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    public List<Raise> getAll() throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM raises;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.executeQuery();
        return convertResultSetToList(statement.getResultSet());
    }
}
