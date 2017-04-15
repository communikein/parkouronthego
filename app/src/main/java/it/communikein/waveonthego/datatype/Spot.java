package it.communikein.waveonthego.datatype;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

/**
 *
 * Created by Elia Maracani on 15/04/2017.
 */
public class Spot {

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
    @PropertyName("description")
    private String mDescription;


    public Spot() {
        // Needed for Firebase
    }

    public Spot(String name, String description, String location, LatLng coords) {
        setName(name);
        setDescription(description);
        setLocation(location);
        setCoords(coords);
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
    private void setName(String name) {
        this.mName = name;
    }

    @PropertyName("description")
    public String getDescription() {
        return mDescription;
    }

    @PropertyName("description")
    private void setDescription(String description) {
        this.mDescription = description;
    }

    @PropertyName("location")
    public String getLocationString() {
        return mLocation;
    }

    @PropertyName("location")
    private void setLocation(String location) {
        this.mLocation = location;
    }

    @Exclude
    public LatLng getCoords() {
        return mCoords;
    }

    @Exclude
    private void setCoords(LatLng coords) {
        this.mCoords = coords;
        if (coords != null) {
            this.latitude = coords.latitude;
            this.longitude = coords.longitude;
        }
    }

    @PropertyName("latitude")
    private double getLatitude() {
        return latitude;
    }

    @PropertyName("latitude")
    public void setLatitude(double latitude) {
        this.latitude = latitude;

        if (getLongitude() != ERROR_COORDS)
            setCoords(new LatLng(getLatitude(), getLongitude()));
    }

    @PropertyName("longitude")
    private double getLongitude() {
        return longitude;
    }

    @PropertyName("longitude")
    public void setLongitude(double longitude) {
        this.longitude = longitude;

        if (getLatitude() != ERROR_COORDS)
            setCoords(new LatLng(getLatitude(), getLongitude()));
    }
}
