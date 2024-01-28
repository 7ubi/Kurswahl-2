package com.x7ubi.kurswahl.common.models;

import jakarta.persistence.*;

@Entity
@Table(name = "CHOICE_CLASS")
public class ChoiceClass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long choiceClassId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn()
    Class aClass;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn()
    Choice choice;

    @Column(nullable = false)
    boolean selected = false;

    public Long getChoiceClassId() {
        return choiceClassId;
    }

    public void setChoiceClassId(Long choiceClassId) {
        this.choiceClassId = choiceClassId;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
