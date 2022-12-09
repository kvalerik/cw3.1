package entities;

import java.io.Serializable;
import java.util.Date;

public class Raise implements Serializable {
    private int id;
    private String sourceName;
    private String destinationName;
    private float costStandart;
    private float costBuisiness;

    private Date date;

    public Raise() {
        date = new Date();
    }

    public Raise(int id, String source, String destinationName, float costStandart, float costBuisiness, Date date) {
        this.id = id;
        this.sourceName = source;
        this.destinationName = destinationName;
        this.costStandart = costStandart;
        this.costBuisiness = costBuisiness;
        this.date = new Date();
        this.date.setTime(date.getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public float getCostStandart() {
        return costStandart;
    }

    public void setCostStandart(float costStandart) {
        this.costStandart = costStandart;
    }

    public float getCostBuisiness() {
        return costBuisiness;
    }

    public void setCostBuisiness(float costBuisiness) {
        this.costBuisiness = costBuisiness;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
