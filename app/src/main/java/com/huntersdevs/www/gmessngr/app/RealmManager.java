package com.huntersdevs.www.gmessngr.app;

import android.content.Context;

import com.huntersdevs.www.gmessngr.R;
import com.huntersdevs.www.gmessngr.pojo.ContactPOJO;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmManager {

    private Realm realm;
    private static RealmManager instance = null;

    private RealmManager(Context mContext) {
        Realm.init(mContext);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(mContext.getString(R.string.realm_db_name))
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getInstance(config);
    }

    public static synchronized RealmManager getInstance(Context context){
        if(null == instance) instance = new RealmManager(context);
        return instance;
    }

    public Realm getRealm(){
        return this.realm;
    }

    public void setContacts(ContactPOJO contactPOJO) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(contactPOJO);
        realm.commitTransaction();
    }

    public ArrayList<String> getContacts() {
        RealmResults<ContactPOJO> results = realm.where(ContactPOJO.class).findAll();
        ArrayList<String> contacts = new ArrayList<>();
        for(RealmObject realmObject : results){
            contacts.add(String.valueOf(realmObject));
        }
        return contacts;
    }

    public String getGMessngrContact(String mContact) {
        ContactPOJO result = realm.where(ContactPOJO.class).equalTo("contact", mContact).findFirst();
        return result.getContact();
    }

}