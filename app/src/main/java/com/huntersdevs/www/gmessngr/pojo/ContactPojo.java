package com.huntersdevs.www.gmessngr.pojo;

public class ContactPojo {

    private String contactId, contactName, contactNumber, contactEmail,
            contactPhoto, contactOtherDetails;

    public ContactPojo() {
    }

    public ContactPojo(String contactId, String contactName, String contactNumber, String contactEmail, String contactPhoto, String contactOtherDetails) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.contactEmail = contactEmail;
        this.contactPhoto = contactPhoto;
        this.contactOtherDetails = contactOtherDetails;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhoto() {
        return contactPhoto;
    }

    public void setContactPhoto(String contactPhoto) {
        this.contactPhoto = contactPhoto;
    }

    public String getContactOtherDetails() {
        return contactOtherDetails;
    }

    public void setContactOtherDetails(String contactOtherDetails) {
        this.contactOtherDetails = contactOtherDetails;
    }
}
