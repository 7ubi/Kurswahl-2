package com.x7ubi.kurswahl.common.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "MESSAGE")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long messageId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String message;

    @ManyToOne(fetch = FetchType.EAGER)
    private User sender;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "message")
    private Set<AddresseeMessage> addresseeMessage;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Set<AddresseeMessage> getAddresseeMessage() {
        return addresseeMessage;
    }

    public void setAddresseeMessage(Set<AddresseeMessage> addresseeMessage) {
        this.addresseeMessage = addresseeMessage;
    }
}
