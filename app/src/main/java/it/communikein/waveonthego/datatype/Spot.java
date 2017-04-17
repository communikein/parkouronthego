package it.communikein.waveonthego.datatype;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by Elia Maracani on 15/04/2017.
 */
@IgnoreExtraProperties
public class Spot {

    @Exclude
    public static final String SPOT = "SPOT";

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
    @PropertyName("images")
    private ArrayList<String> images = new ArrayList<>();


    public Spot() {
        // Needed for Firebase
    }

    public Spot(String name, String description, String location, LatLng coords) {
        setName(name);
        setDescription(description);
        setLocation(location);
        setCoords(coords);
        setImages(new ArrayList<String>());
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

    @PropertyName("images")
    public ArrayList<String> getImages() {
        return images;
    }

    @Exclude
    private JSONArray getImagesJSONArray() {
        JSONArray array = new JSONArray();

        if (getImages() != null) for (String str : getImages())
            array.put(str);

        return array;
    }

    @PropertyName("images")
    public void setImages(ArrayList<String> images) {
        this.images = new ArrayList<>();
        for (String str : images)
            addImage(str);
    }

    @Exclude
    private void setImages(JSONArray images) {
        setImages(new ArrayList<String>());

        try {
            if (images != null) for (int i = 0; i < images.length(); i++)
                addImage(images.getString(i));
        } catch (JSONException e) {
            setImages(new ArrayList<String>());
        }
    }

    @Exclude
    public void addImage(String image) {
        this.images.add(image);
    }

    @Exclude
    public void removeImage(String image) {
        this.images.remove(image);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", getID());
        result.put("name", getName());
        result.put("location", getLocation());
        result.put("latitude", getLatitude());
        result.put("longitude", getLongitude());
        result.put("description", getDescription());
        result.put("images", getImages());

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
            obj.put("description", getDescription());
            obj.put("images", getImagesJSONArray());
        } catch (JSONException e) {
            obj = new JSONObject();
        }

        return obj;
    }


    public static Spot fromJSON(JSONObject obj) {
        Spot spot = null;
        if (obj != null) {
            try {
                spot = new Spot();

                if (obj.has("id"))
                    spot.setID(obj.getString("id"));
                if (obj.has("description"))
                    spot.setDescription(obj.getString("description"));
                if (obj.has("location"))
                    spot.setLocation(obj.getString("location"));
                if (obj.has("latitude"))
                    spot.setLatitude(obj.getDouble("latitude"));
                if (obj.has("longitude"))
                    spot.setLongitude(obj.getDouble("longitude"));
                spot.setCoords(new LatLng(spot.getLatitude(), spot.getLongitude()));
                if (obj.has("name"))
                    spot.setName(obj.getString("name"));
                if (obj.has("images"))
                    spot.setImages(obj.getJSONArray("images"));
            } catch (JSONException e) {
                spot = null;
            }
        }

        return spot;
    }
}
