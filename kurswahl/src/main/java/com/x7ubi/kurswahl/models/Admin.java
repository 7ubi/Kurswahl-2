package com.x7ubi.kurswahl.models;

import jakarta.persistence.*;

@Entity
@Table(name = "ADMIN")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long adminId;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    public Admin(Long adminId, User user) {
        this.adminId = adminId;
        this.user = user;
    }

    public Admin() {
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
