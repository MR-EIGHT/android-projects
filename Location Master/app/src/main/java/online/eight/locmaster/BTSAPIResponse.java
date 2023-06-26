package online.eight.locmaster;

import com.google.gson.annotations.SerializedName;

public class BTSAPIResponse {


    @SerializedName("lat")
    private String latitude;
    @SerializedName("lon")
    private String longitude;

    @SerializedName("range")
    private String range;


    @Override
    public String toString() {
        return "{latitude: " + latitude + ", longitude: " + longitude + ", range: " + range+"}";
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getRange() {
        return this.range;
    }
}