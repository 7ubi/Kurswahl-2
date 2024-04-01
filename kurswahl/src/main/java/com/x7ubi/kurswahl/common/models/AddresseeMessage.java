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
    @JoinColumn(name = "messageId")
    private Message message;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

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
}