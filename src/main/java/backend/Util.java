package backend;

/**
 * Created by user on 20.02.2016.
 */
import java.text.SimpleDateFormat;
import java.util.*;
public class Util {

    private static final String format ="yyyy-MM-dd";

    public static String getDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

}
