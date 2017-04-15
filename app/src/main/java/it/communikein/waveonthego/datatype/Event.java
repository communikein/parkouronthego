package it.communikein.waveonthego.datatype;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by eliam on 18/02/2017.
 */

public class Event {
    public static final SimpleDateFormat dateAdapterFormat =
            new SimpleDateFormat("dd/MM", Locale.getDefault());
    public static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd MMMMM yyyy", Locale.getDefault());
    public static final SimpleDateFormat dateTimeFormat =
            new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat dateFBFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @PropertyName("id")
    private String mID;
    @PropertyName("name")
    private String mName;
    @PropertyName("location")
    private String mLocation;
    @PropertyName("coords")
    private LatLng mCoords;
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
        this.mID = id;
        this.mName = name;
        this.mDescription = description;
        this.mLocation = location;
        this.mCoords = coords;
        this.mDateStart = start;
        this.mDateEnd = end;
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

    @PropertyName("coords")
    public LatLng getCoords() {
        return mCoords;
    }

    @PropertyName("coords")
    public void setCoords(LatLng coords) {
        this.mCoords = coords;
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