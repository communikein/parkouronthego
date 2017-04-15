package it.communikein.waveonthego.datatype;


import com.google.firebase.database.PropertyName;

import java.util.Date;

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


    public String printDate() {
        return Utils.dayMonthFormat.format(getDatePublish());
    }
}
