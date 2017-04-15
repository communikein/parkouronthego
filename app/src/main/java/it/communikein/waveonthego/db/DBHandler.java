package it.communikein.waveonthego.db;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.communikein.waveonthego.datatype.Article;
import it.communikein.waveonthego.datatype.Event;
import it.communikein.waveonthego.datatype.Spot;

/**
 *
 * Created by Elia Maracani on 15/04/2017.
 */

public class DBHandler {

    public final static String DB_ARTICLES = "articles";
    public final static String DB_EVENTS = "events";
    public final static String DB_SPOTS = "spots";

    private static DBHandler instance;

    private FirebaseDatabase db;
    private DatabaseReference refArticles;
    private DatabaseReference refEvents;
    private DatabaseReference refSpots;

    public static DBHandler getInstance(){
        if (instance == null)
            instance = new DBHandler();

        return instance;
    }

    private DBHandler(){
        if (db == null)
            db = FirebaseDatabase.getInstance();
        if (refArticles == null)
            refArticles = db.getReference(DB_ARTICLES);
        if (refEvents == null)
            refEvents = db.getReference(DB_EVENTS);
        if (refSpots == null)
            refSpots = db.getReference(DB_SPOTS);
    }

    public void writeToArticles(Article article){
        refArticles.child(article.getID()).setValue(article);
    }

    public void writeToEvents(Event event){
        refEvents.child(event.getID()).setValue(event);
    }

    public void writeToSpots(Spot spot){
        refSpots.push().setValue(spot);
    }
}
