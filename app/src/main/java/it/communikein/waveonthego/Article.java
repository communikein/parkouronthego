package it.communikein.waveonthego;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by eliam on 17/02/2017.
 */

public class Article {

    public static final SimpleDateFormat datePrintAdapterFormat =
            new SimpleDateFormat("dd/MM", Locale.getDefault());
    public static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd MMMMM yyyy", Locale.getDefault());

    private String mTitle;
    private String mSummary;
    private String mURLFullArticle;
    private Date mDatePublish;


    public Article(String title, String summary, String URLfullArticle, Date datePublish) {
        this.mTitle = title;
        this.mSummary = summary;
        this.mURLFullArticle = URLfullArticle;
        this.mDatePublish = datePublish;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        this.mSummary = summary;
    }

    public String getURLfullArticle() {
        return mURLFullArticle;
    }

    public void setURLfullArticle(String URLFullArticle) {
        this.mURLFullArticle = URLFullArticle;
    }

    public Date getDatePublish() {
        return mDatePublish;
    }

    public void setDatePublish(Date datePublish) {
        this.mDatePublish = datePublish;
    }

    public String printDate() {
        return datePrintAdapterFormat.format(getDatePublish());
    }
}
