package ca.bcit.ass3.katz_kao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lel on 2017-10-31.
 */

public class Event {

    private String eventName;
    private String eventDate;

    public Event(String name, String date){
        eventName = name;
        eventDate = date;
    }

    public void setEventName (String name) {
        eventName = name;
    }

    public void setEventDate (String date) {
        eventDate = date;
    }

    public String getEventName () {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

}
