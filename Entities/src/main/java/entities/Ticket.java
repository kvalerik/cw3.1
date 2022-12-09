package entities;

import java.io.Serializable;
import java.util.Date;

public class Ticket implements Serializable {
    private int id;
    private TicketToBuy ticketToBuy;
    private String fullname;
    private String pasport;
    private String series;

    public Ticket(int id, TicketToBuy ticketToBuy, String fullname, String pasport, String series) {
        this.id = id;
        this.ticketToBuy = ticketToBuy;
        this.fullname = fullname;
        this.pasport = pasport;
        this.series = series;
    }

    public Ticket() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TicketToBuy getTicketToBuy() {
        return ticketToBuy;
    }

    public void setTicketToBuy(TicketToBuy ticketToBuy) {
        this.ticketToBuy = ticketToBuy;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPasport() {
        return pasport;
    }

    public void setPasport(String pasport) {
        this.pasport = pasport;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }
}
