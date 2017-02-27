package it.communikein.waveonthego;

import com.google.android.gms.maps.model.LatLng;

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

    private String mName;
    private String mLocation;
    private LatLng mCoords;
    private Date mDateStart;
    private Date mDateEnd;
    private String mDescription;

    public Event(String name, String description, String location, LatLng coords, Date start, Date end) {
        this.mName = name;
        this.mDescription = description;
        this.mLocation = location;
        this.mCoords = coords;
        this.mDateStart = start;
        this.mDateEnd = end;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getLocationString() {
        return mLocation;
    }

    public void setmLocation(String location) {
        this.mLocation = location;
    }

    public LatLng getCoords() {
        return mCoords;
    }

    public void setCoords(LatLng coords) {
        this.mCoords = coords;
    }

    public Date getDateStart() {
        return mDateStart;
    }

    public void setDateStart(Date start) {
        this.mDateStart = start;
    }

    public Date getDateEnd() {
        return mDateEnd;
    }

    public void setDateStartEnd(Date end) {
        this.mDateEnd = end;
    }


    public static String printDate(Date toPrint, SimpleDateFormat sdf) {
        return sdf.format(toPrint);
    }
}
