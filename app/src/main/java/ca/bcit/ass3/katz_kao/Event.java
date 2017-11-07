package ca.bcit.ass3.katz_kao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lel on 2017-10-31.
 */

public class Event {

    private String eventName;
    private Date eventDate;

    public Event(String name, Date date){
        eventName = name;
        eventDate = date;
    }

    public void setEventName (String name) {
        eventName = name;
    }

    public void setEventDate (Date date) {
        eventDate = date;
    }

    public String getEventName () {
        return eventName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public Long getMilliDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss"); //Or whatever format fits best your needs.
        SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        String dateFormatted = f.format(eventDate);
        long milliseconds = 0;
        try {
            Date d = f.parse(dateFormatted);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }

    public void setMilliDate(Long ms) {
        Date d = new Date(ms);
        eventDate = d;
    }

}
