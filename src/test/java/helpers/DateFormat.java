package helpers;

import java.text.SimpleDateFormat;

public class DateFormat {
    public String getDateInShortFormat() {
        java.util.Date myNowDate = new java.util.Date();
        SimpleDateFormat formatOk = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");//21.03.2017 18:03
        return formatOk.format(myNowDate);
    }
}