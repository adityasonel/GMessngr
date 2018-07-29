package com.huntersdevs.www.gmessngr.pojo;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ContactPOJO extends RealmObject implements Serializable {

    @PrimaryKey
    private String contact;

    public ContactPOJO() {
    }

    public ContactPOJO(String contact) {
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
