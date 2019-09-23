package android.bignerdbranch.com;

import java.util.Date;
import java.util.UUID;

public class CheckIn {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private String mDetails;
    private double mLat;
    private double mLon;
    public CheckIn() {
        this(UUID.randomUUID());
    }

    public CheckIn(UUID id) {
        mId = id;
        mDate = new Date();
    }


    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDetails() {return mDetails;}

    public void setDetails(String details){
        mDetails = details;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getLocation() {return "Latitude " + getLat() + " Longitude " + getLon();}

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public double getLat() {
        return mLat;
    }
    public double getLon(){
        return mLon;
    }
}
