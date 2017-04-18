package it.communikein.waveonthego.datatype;

import com.google.firebase.database.PropertyName;

/**
 * Created by eliam on 18/04/2017.
 */

public class Admin {

    @PropertyName("uid")
    private String mUid;
    @PropertyName("display_name")
    private String mName;
    @PropertyName("email")
    private String mMail;

    public Admin() {
        // Needed for Firebase
    }

    public Admin(String uid, String name, String mail) {
        this.mUid = uid;
        this.mName = name;
        this.mMail = mail;
    }

    @PropertyName("uid")
    public String getUID() {
        return mUid;
    }

    @PropertyName("uid")
    public void setUID(String uid) {
        this.mUid = uid;
    }

    @PropertyName("display_name")
    public String getName() {
        return mName;
    }

    @PropertyName("display_name")
    public void setName(String display_name) {
        this.mName = display_name;
    }

    @PropertyName("email")
    public String getMail() {
        return mMail;
    }

    @PropertyName("email")
    public void setMail(String email) {
        this.mMail = email;
    }


    /*@Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", getID());
        result.put("title", getTitle());
        result.put("summary", getSummary());
        result.put("url_full_article", getUrlFullArticle());
        result.put("date_publish", getDatePublish());

        return result;
    }

    @Exclude
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("id", getID());
            obj.put("title", getTitle());
            obj.put("summary", getSummary());
            obj.put("url_full_article", getUrlFullArticle());
            obj.put("date_publish", Utils.dayMonthYearFormat.format(getDatePublish()));
        } catch (JSONException e) {
            obj = new JSONObject();
        }

        return obj;
    }


    @Exclude
    public static Admin fromJSON(JSONObject obj) {
        Admin admin = null;

        if (obj != null) {
            try {
                admin  = new Admin();
                if (obj.has("id"))
                    admin.setUID(obj.getString("uid"));
                if (obj.has("title"))
                    admin.setName(obj.getString("display_name"));
                if (obj.has("summary"))
                    admin.setMail(obj.getString("email"));
            } catch (JSONException e) {
                admin = null;
            }
        }

        return admin;
    }*/
}
