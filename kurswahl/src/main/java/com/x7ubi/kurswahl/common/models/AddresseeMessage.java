package com.x7ubi.kurswahl.common.models;


import jakarta.persistence.*;

@Entity
@Table(name = "ADDRESSEE_MESSAGE")
public class AddresseeMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long addresseeMessageId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "message_id")
    private Message message;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private boolean readMessage = false;

    public Long getAddresseeMessageId() {
        return addresseeMessageId;
    }

    public void setAddresseeMessageId(Long addresseeMessageId) {
        this.addresseeMessageId = addresseeMessageId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isReadMessage() {
        return readMessage;
    }

    public void setReadMessage(boolean readMessage) {
        this.readMessage = readMessage;
    }
}