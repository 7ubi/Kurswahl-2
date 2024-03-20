package com.x7ubi.kurswahl.common.models;

import jakarta.persistence.*;

@Entity
@Table(name = "SETTING")
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long settingId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer value;

    public Long getSettingId() {
        return settingId;
    }

    public void setSettingId(Long settingId) {
        this.settingId = settingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
