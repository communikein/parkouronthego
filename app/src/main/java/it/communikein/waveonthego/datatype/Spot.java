package it.communikein.waveonthego.datatype;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.PropertyName;

/**
 *
 * Created by Elia Maracani on 15/04/2017.
 */
public class Spot {
    @PropertyName("id")
    private String mID;
    @PropertyName("name")
    private String mName;
    @PropertyName("location")
    private String mLocation;
    @PropertyName("coords")
    private LatLng mCoords;
    @PropertyName("description")
    private String mDescription;


    public Spot() {
        // Needed for Firebase
    }

    public Spot(String id, String name, String description, String location, LatLng coords) {
        this.mID = id;
        this.mName = name;
        this.mDescription = description;
        this.mLocation = location;
        this.mCoords = coords;
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
}
