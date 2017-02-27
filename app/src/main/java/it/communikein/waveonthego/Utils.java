package it.communikein.waveonthego;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by eliam on 27/02/2017.
 */

public class Utils {

    static SimpleDateFormat dateFormat =
            new SimpleDateFormat("EEEEEEEEEEE, d MMM yyyy", Locale.getDefault());
    static SimpleDateFormat timeFormat =
            new SimpleDateFormat("HH:mm", Locale.getDefault());

}
