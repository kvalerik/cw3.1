package Commands;

public enum Command {

    //COMMON
    EXIT,
    GET_ALL_RAISES,

    //ADMIN
    CREATE_RAISE,
    EDIT_RAISE,
    DELETE_RAISE,
    BAN_CLIENT,
    UNBAN_CLIENT,
    GET_ALL_CLIENTS,

    //CLIENT
    GET_ALL_CURRENT_CLIENT_TICKETS,
    GET_CURRENT_PROFILE,
    ADD_TO_BUY,
    GET_ALL_TICKETS_TO_BUY,
    DELETE_TICKET_TO_BUY,
    BUY_TICKET,
    GET_ALL_TICKETS,
}
