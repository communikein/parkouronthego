package it.communikein.waveonthego;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by eliam on 18/02/2017.
 */

public class Event {
    public static final SimpleDateFormat datePrintAdapterFormat =
            new SimpleDateFormat("dd/MM", Locale.getDefault());
    public static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd MMMMM yyyy", Locale.getDefault());
    public static final SimpleDateFormat dateFBFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    private String mName;
    private String mLocation;
    private LatLng mCoords;
    private Date mDate;
    private String mDescription;

    public Event(String name, String description, String location, LatLng coords, Date date) {
        this.mName = name;
        this.mDescription = description;
        this.mLocation = location;
        this.mCoords = coords;
        this.mDate = date;
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

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public String printDate() {
        return datePrintAdapterFormat.format(getDate());
    }

    public String printDateFull() {
        return dateFormat.format(getDate());
    }
}
