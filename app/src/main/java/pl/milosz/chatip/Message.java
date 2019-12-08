package pl.milosz.chatip;

import java.util.Calendar;
import java.util.Date;

public class Message {
    private String message;
    private boolean userFlag;
    private Calendar calendar;

    public Message(String message, boolean userFlag, Calendar calendar) {
        this.message = message;
        this.userFlag = userFlag;
        this.calendar = calendar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isUserFlag() {
        return userFlag;
    }

    public void setUserFlag(boolean userFlag) {
        this.userFlag = userFlag;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
