package entities;

public class TicketToBuy {
    private int id;
    private User user;
    private Raise raise;
    private boolean isBuisness;

    public TicketToBuy() {
    }

    public TicketToBuy(int id, User user, Raise raise, boolean isBuisness) {
        this.user = user;
        this.raise = raise;
        this.isBuisness = isBuisness;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Raise getRaise() {
        return raise;
    }

    public void setRaise(Raise raise) {
        this.raise = raise;
    }

    public boolean isBuisness() {
        return isBuisness;
    }

    public void setBuisness(boolean buisness) {
        isBuisness = buisness;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
