package com.cache.bp.bpcashed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="bp_application")
@Builder
@AllArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String url;

    private String name;

    @OneToMany(mappedBy = "application", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Message> messages;

    @Column(nullable = false)
    private Boolean online;

    protected Application() {
    }

    public Application(String url) {
        super();
        this.url = url;
        this.online = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }
}
