package it.communikein.waveonthego.datatype;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.communikein.waveonthego.Utils;

/**
 *
 * Created by Elia Maracani on 18/02/2017.
 */
public class Event {

    @Exclude
    public static final String EVENT = "EVENT";

    @Exclude
    private static final double ERROR_COORDS = -1;

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
    public String getLocation() {
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
    }

    @PropertyName("longitude")
    public double getLongitude() {
        return longitude;
    }

    @PropertyName("longitude")
    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", getID());
        result.put("name", getName());
        result.put("location", getLocation());
        result.put("latitude", getLatitude());
        result.put("longitude", getLongitude());
        result.put("date_start", getDateStart());
        result.put("date_end", getDateEnd());
        result.put("description", getDescription());

        return result;
    }

    @Exclude
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("id", getID());
            obj.put("name", getName());
            obj.put("location", getLocation());
            obj.put("latitude", getLatitude());
            obj.put("longitude", getLongitude());
            obj.put("date_start", printDate(getDateStart(), Utils.dateTimeFormat));
            obj.put("date_end", printDate(getDateEnd(), Utils.dateTimeFormat));
            obj.put("description", getDescription());
        } catch (JSONException e) {
            obj = new JSONObject();
        }

        return obj;
    }


    @Exclude
    public static String printDate(Date toPrint, SimpleDateFormat sdf) {
        return sdf.format(toPrint);
    }

    @Exclude
    public static Event fromJSON(JSONObject obj) {
        Event event = null;

        if (obj != null) {
            try {
                event  = new Event();
                if (obj.has("id"))
                    event.setID(obj.getString("id"));
                if (obj.has("name"))
                    event.setName(obj.getString("name"));
                if (obj.has("description"))
                    event.setDescription(obj.getString("description"));
                if (obj.has("latitude"))
                    event.setLatitude(obj.getDouble("latitude"));
                if (obj.has("longitude"))
                    event.setLongitude(obj.getDouble("longitude"));
                event.setCoords(new LatLng(event.getLatitude(), event.getLongitude()));
                if (obj.has("location"))
                    event.setLocation(obj.getString("location"));
                if (obj.has("date_start"))
                    event.setDateStart(Utils.dateTimeFormat.parse(obj.getString("date_start")));
                if (obj.has("date_end"))
                    event.setDateEnd(Utils.dateTimeFormat.parse(obj.getString("date_end")));
            } catch (JSONException | ParseException e) {
                event = null;
            }
        }

        return event;
    }
}
