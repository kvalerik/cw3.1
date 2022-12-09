package dbLayer.managers;

import dbLayer.repositories.*;

import java.sql.Connection;

public class DataAccessManager {

    public final ClientsRepository clientsRepository;
    public final AdminsRepository adminsRepository;
    public final TicketsToBuyRepository ticketsToBuyRepository;
    public final RaisesRepository raisesRepository;
    public final TicketsRepository ticketsRepository;

    public DataAccessManager(Connection connection) {

        clientsRepository = new ClientsRepository(connection);
        adminsRepository = new AdminsRepository(connection);
        ticketsToBuyRepository = new TicketsToBuyRepository(connection);
        raisesRepository = new RaisesRepository(connection);
        ticketsRepository = new TicketsRepository(connection);
    }
}
