package it.communikein.waveonthego.datatype;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * Created by Elia Maracani on 18/02/2017.
 */
public class Event {

    @Exclude
    public static final double ERROR_COORDS = -1;

    @PropertyName("id")
    private String mID;
    @PropertyName("name")
    private String mName;
    @PropertyName("location")
    private String mLocation;
    @Exclude
    private LatLng mCoords;
    @PropertyName("latitude")
    private double latitude = ERROR_COORDS;
    @PropertyName("longitude")
    private double longitude = ERROR_COORDS;
    @PropertyName("date_start")
    private Date mDateStart;
    @PropertyName("date_end")
    private Date mDateEnd;
    @PropertyName("description")
    private String mDescription;


    public Event() {
        // Needed for Firebase
    }

    public Event(String id, String name, String description, String location, LatLng coords,
                 Date start, Date end) {
        setID(id);
        setName(name);
        setDescription(description);
        setLocation(location);
        setCoords(coords);
        setDateStart(start);
        setDateEnd(end);
    }

    @PropertyName("id")
    public String getID() {
        return mID;
    }

    @PropertyName("id")
    public void setID(String id) {
        this.mID = id;
    }

    @PropertyName("name")
    public String getName() {
        return mName;
    }

    @PropertyName("name")
    public void setName(String name) {
        this.mName = name;
    }

    @PropertyName("description")
    public String getDescription() {
        return mDescription;
    }

    @PropertyName("description")
    public void setDescription(String description) {
        this.mDescription = description;
    }

    @PropertyName("location")
    public String getLocationString() {
        return mLocation;
    }

    @PropertyName("location")
    public void setLocation(String location) {
        this.mLocation = location;
    }

    @Exclude
    public LatLng getCoords() {
        return mCoords;
    }

    @Exclude
    public void setCoords(LatLng coords) {
        this.mCoords = coords;
        if (coords != null) {
            this.latitude = coords.latitude;
            this.longitude = coords.longitude;
        }
    }

    @PropertyName("latitude")
    public double getLatitude() {
        return latitude;
    }

    @PropertyName("latitude")
    public void setLatitude(double latitude) {
        this.latitude = latitude;

        if (getLongitude() != ERROR_COORDS)
            setCoords(new LatLng(getLatitude(), getLongitude()));
    }

    @PropertyName("longitude")
    public double getLongitude() {
        return longitude;
    }

    @PropertyName("longitude")
    public void setLongitude(double longitude) {
        this.longitude = longitude;

        if (getLatitude() != ERROR_COORDS)
            setCoords(new LatLng(getLatitude(), getLongitude()));
    }

    @PropertyName("date_start")
    public Date getDateStart() {
        return mDateStart;
    }

    @PropertyName("date_start")
    public void setDateStart(Date start) {
        this.mDateStart = start;
    }

    @PropertyName("date_end")
    public Date getDateEnd() {
        return mDateEnd;
    }

    @PropertyName("date_end")
    public void setDateEnd(Date end) {
        this.mDateEnd = end;
    }


    public static String printDate(Date toPrint, SimpleDateFormat sdf) {
        return sdf.format(toPrint);
    }
}
