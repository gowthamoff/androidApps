package com.kazimasum.retro;
import com.google.gson.annotations.SerializedName;

public class CurrentWeather {
    @SerializedName("last_updated_epoch")
    private long lastUpdatedEpoch;

    @SerializedName("last_updated")
    private String lastUpdated;

    @SerializedName("temp_c")
    private double tempC;

    @SerializedName("temp_f")
    private double tempF;

    public long getLastUpdatedEpoch() {
        return lastUpdatedEpoch;
    }

    public void setLastUpdatedEpoch(long lastUpdatedEpoch) {
        this.lastUpdatedEpoch = lastUpdatedEpoch;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public double getTempC() {
        return tempC;
    }

    public void setTempC(double tempC) {
        this.tempC = tempC;
    }

    public double getTempF() {
        return tempF;
    }

    public void setTempF(double tempF) {
        this.tempF = tempF;
    }
}
