package it.communikein.waveonthego.datatype;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.communikein.waveonthego.Utils;

/**
 *
 * Created by Elia Maracani on 17/02/2017.
 */
public class Article {

    @PropertyName("id")
    private String mID;
    @PropertyName("title")
    private String mTitle;
    @PropertyName("summary")
    private String mSummary;
    @PropertyName("url_full_article")
    private String mUrlFullArticle;
    @PropertyName("date_publish")
    private Date mDatePublish;

    public Article() {
        // Needed for Firebase
    }

    public Article(String id, String title, String summary, String urlFullArticle, Date datePublish) {
        this.mID = id;
        this.mTitle = title;
        this.mSummary = summary;
        this.mUrlFullArticle = urlFullArticle;
        this.mDatePublish = datePublish;
    }

    @PropertyName("id")
    public String getID() {
        return mID;
    }

    @PropertyName("id")
    public void setID(String id) {
        this.mID = id;
    }

    @PropertyName("title")
    public String getTitle() {
        return mTitle;
    }

    @PropertyName("title")
    public void setTitle(String title) {
        this.mTitle = title;
    }

    @PropertyName("summary")
    public String getSummary() {
        return mSummary;
    }

    @PropertyName("summary")
    public void setSummary(String summary) {
        this.mSummary = summary;
    }

    @PropertyName("url_full_article")
    public String getUrlFullArticle() {
        return mUrlFullArticle;
    }

    @PropertyName("url_full_article")
    public void setUrlFullArticle(String urlFullArticle) {
        this.mUrlFullArticle = urlFullArticle;
    }

    @PropertyName("date_publish")
    public Date getDatePublish() {
        return mDatePublish;
    }

    @PropertyName("date_publish")
    public void setDatePublish(Date datePublish) {
        this.mDatePublish = datePublish;
    }

    @Exclude
    public String printDate() {
        return Utils.dayMonthFormat.format(getDatePublish());
    }

    @Exclude
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
    public static Article fromJSON(JSONObject obj) {
        Article article = null;

        if (obj != null) {
            try {
                article  = new Article();
                if (obj.has("id"))
                    article.setID(obj.getString("id"));
                if (obj.has("title"))
                    article.setTitle(obj.getString("title"));
                if (obj.has("summary"))
                    article.setSummary(obj.getString("summary"));
                if (obj.has("url_full_article"))
                    article.setUrlFullArticle(obj.getString("url_full_article"));
                if (obj.has("date_publish"))
                    article.setDatePublish(Utils.dayMonthYearFormat
                            .parse(obj.getString("date_publish")));
            } catch (JSONException | ParseException e) {
                article = null;
            }
        }

        return article;
    }
}
