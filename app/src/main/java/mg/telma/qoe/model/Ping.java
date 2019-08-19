package mg.telma.qoe.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Ping extends RealmObject implements Serializable {
    @PrimaryKey
    private Integer id;
    private double minDuration;
    private double maxDuration;
    private double avgDureation;
    private Boolean statusPing;

    public Ping() {
    }

    public Ping(double minDuration, double maxDuration, double avgDureation) {
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.avgDureation = avgDureation;
    }

    public double getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(double minDuration) {
        this.minDuration = minDuration;
    }

    public double getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(double maxDuration) {
        this.maxDuration = maxDuration;
    }

    public double getAvgDureation() {
        return avgDureation;
    }

    public void setAvgDureation(double avgDureation) {
        this.avgDureation = avgDureation;
    }

    public Boolean getStatusPing() {
        return statusPing;
    }

    public void setStatusPing(Boolean statusPing) {
        this.statusPing = statusPing;
    }
}
