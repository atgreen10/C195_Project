package model;

public class Contact {

    private int contactID;
    private String contactName;
    private String contactEmail;

    public Contact(){

    };

    public Contact(int contactID, String contactName, String contactEmail){

    };

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
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
}
