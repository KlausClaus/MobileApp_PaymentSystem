package com.example.tuitionpayment.entity;

public class Notification {
    private String id;
    private String content;
    private String time;
    private String uid;

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
