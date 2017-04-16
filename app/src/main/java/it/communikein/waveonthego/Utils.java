package it.communikein.waveonthego;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 * Created by Elia Maracani on 27/02/2017.
 */
public class Utils {
    public static final SimpleDateFormat dayMonthFormat =
            new SimpleDateFormat("dd/MM", Locale.getDefault());
    public static final SimpleDateFormat dayMonthYearFormat =
            new SimpleDateFormat("dd MMMMM yyyy", Locale.getDefault());
    public static final SimpleDateFormat dateFBFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat dateTimeFormat =
            new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat timeFormat =
            new SimpleDateFormat("HH:mm", Locale.getDefault());

    public static String getPath(Uri uri, Context context) {
        String[] imagePathColumn = {MediaStore.Images.Media.DATA};

        ContentResolver contentResolver = context.getContentResolver();
        String mediaPath = "";

        Cursor imageCursor = contentResolver.query(uri,
                imagePathColumn, null, null, null);

        if (imageCursor != null) {
            imageCursor.moveToFirst();

            int index = imageCursor.getColumnIndex(imagePathColumn[0]);
            mediaPath = imageCursor.getString(index);
            imageCursor.close();
        }

        return mediaPath;
    }
}
