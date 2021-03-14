
package model;

public class Contact {

    private String contactID;
    private String contactName;
    private String contactEmail;

    public Contact() {

    }

    public Contact(String contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;

    }

    public String getContactID() {
        return (contactID);
    }

    public void setContactID(int contactID) {
        this.contactID = String.valueOf(contactID);
    }

    public String getContactName() {
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

    @Override
    public String toString() {
        return ((contactID) + ". " + contactName);
    }
}