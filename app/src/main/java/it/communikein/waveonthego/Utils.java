package it.communikein.waveonthego;


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
}
