package ru.cft.focus.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public record Message(String senderName, String text, Date date/*, boolean connect*/) {
    public String formatMessage() {
//        if (connect) {
//
//        }
        return formatTimeStamp(date) + " " + senderName + ": " + text;
    }

    public String formatConnection() {
        return formatTimeStamp(date) + " " + senderName + " JOINED";
    }

    private String formatTimeStamp(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return "[" + sdf.format(date) + "]";
    }
}
//{
//    private final String senderName;
//    private final String text;
//    private final Date timestamp;
//
//    public Message(String senderName, String text, Date timestamp) {
//        this.senderName = senderName;
//        this.text = text;
//        this.timestamp = timestamp;
//    }
//
//    public String getSenderName() {
//        return senderName;
//    }
//
//    public String getText() {
//        return text;
//    }
//
//    public Date getTimestamp() {
//        return timestamp;
//    }
//}
