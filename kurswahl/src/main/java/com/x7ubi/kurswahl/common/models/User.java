package com.x7ubi.kurswahl.common.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "USER")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String firstname;

    @Column(nullable = false, length = 100)
    private String surname;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = true, length = 100)
    private String generatedPassword;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sender")
    private Set<Message> sentMessages;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private Set<AddresseeMessage> addresseeMessage;

    public User() {
    }

    public User(String username, String firstname, String surname, String password) {
        this.username = username;
        this.firstname = firstname;
        this.surname = surname;
        this.password = password;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGeneratedPassword() {
        return generatedPassword;
    }

    public void setGeneratedPassword(String generatedPassword) {
        this.generatedPassword = generatedPassword;
    }

    public Set<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(Set<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public Set<AddresseeMessage> getAddresseeMessage() {
        return addresseeMessage;
    }

    public void setAddresseeMessage(Set<AddresseeMessage> addresseeMessage) {
        this.addresseeMessage = addresseeMessage;
    }
}
