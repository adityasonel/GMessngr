package com.huntersdevs.www.gmessngr.pojo;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GMessngrContactPOJO extends RealmObject implements Serializable {

    @PrimaryKey
    private String gmessngrContact;

    public GMessngrContactPOJO() {
    }

    public GMessngrContactPOJO(String gmessngrContact) {
        this.gmessngrContact = gmessngrContact;
    }

    public String getGmessngrContact() {
        return gmessngrContact;
    }

    public void setGmessngrContact(String gmessngrContact) {
        this.gmessngrContact = gmessngrContact;
    }
}
