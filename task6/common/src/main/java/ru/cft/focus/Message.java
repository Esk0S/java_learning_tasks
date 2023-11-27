package ru.cft.focus;

import java.text.SimpleDateFormat;
import java.util.Date;

public record Message(String senderName, String text, Date date) {
    public String formatMessage() {
        return formatTimeStamp(date) + " " + senderName + ": " + text;
    }

    public String formatConnection() {
        return formatTimeStamp(date) + " " + senderName + " JOINED";
    }

    public String formatDisconnection() {
        return formatTimeStamp(date) + " " + senderName + " LEFT";
    }

    private String formatTimeStamp(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return "[" + sdf.format(date) + "]";
    }
}
