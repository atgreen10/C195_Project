
package model;

import utils.requests;

import java.util.HashMap;
import java.util.Map;

public class Contact {

    private String contactID;
    private static String contactName;
    private String contactEmail;

    public Contact() {

    }

    public Contact(int contactID, String contactName, String contactEmail) {

    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public static String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

//    @Override
//    public String toString() {
//        return (Integer.parseInt(contactID) + ". " + contactName);
//    }
}