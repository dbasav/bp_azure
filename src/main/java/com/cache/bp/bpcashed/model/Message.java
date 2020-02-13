package com.cache.bp.bpcashed.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="bp_message")
public class Message {

    static final long MESSAGE_TIMEOUT = 24 * 60 * 60 * 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String messageBody;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Timestamp timestamp;

    @ManyToOne(optional = false)
    private Application application;

    protected Message() {
    }

    public Message(String messageBody, String contentType, Application application) {
        super();
        this.messageBody = messageBody;
        this.contentType = contentType;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.application = application;
    }


    public Long getDestinationId() {
        return application.getId();
    }

    public String getDestinationUrl() {
        return application.getUrl();
    }

    public Boolean isMessageTimeout() {
        return timestamp.getTime() < System.currentTimeMillis() - MESSAGE_TIMEOUT;
    }


    public static long getMessageTimeout() {
        return MESSAGE_TIMEOUT;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public String toString() {
        return String.format("Message[id=%d, messageBody='%s', contentType='%s']", id, messageBody, contentType);
    }
}

