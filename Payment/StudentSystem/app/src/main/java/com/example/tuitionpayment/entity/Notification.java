package com.example.tuitionpayment.entity;

/**
 * Represents a notification entity.
 * Contains information about the notification's ID, content, timestamp, and user ID.
 */
public class Notification {

    /**
     * Unique identifier for the notification.
     */
    private String id;

    /**
     * Content of the notification.
     */
    private String content;

    /**
     * Timestamp of when the notification was created.
     */
    private String time;

    /**
     * User ID associated with the notification.
     */
    private String uid;

    /**
     * Constructs a {@code Notification} with the specified details.
     *
     * @param id      Unique identifier for the notification.
     * @param content Content of the notification.
     * @param time    Timestamp of when the notification was created.
     * @param uid     User ID associated with the notification.
     */
    public Notification(String id, String content, String time, String uid) {
        this.id = id;
        this.content = content;
        this.time = time;
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getUid() {
        return uid;
    }
}
