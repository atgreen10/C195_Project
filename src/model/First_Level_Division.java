package model;

import utils.requests;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class First_Level_Division {

    private int division_ID;
    private String division;
    private LocalDateTime create_Date;
    private String created_by;
    private Timestamp last_Update;
    private String last_Updated_By;
    private int country_ID;

    public First_Level_Division() throws SQLException {
    }

    public First_Level_Division(int division_ID, String division, LocalDateTime create_Date, String created_By,
                                Timestamp last_Update, String last_Updated_By, int country_ID) throws SQLException {
        this.division_ID = division_ID;
        this.division = division;
        this.create_Date = create_Date;
        this.created_by = created_By;
        this.last_Update = last_Update;
        this.last_Updated_By = last_Updated_By;
        this.country_ID = country_ID;
    }

    public int getDivision_ID() {
        return division_ID;
    }

    public void setDivision_ID(int division_ID) {
        this.division_ID = division_ID;
    }

    public String getDivision() {
        return division;
    }


    public void setDivision(String division) {
        this.division = division;
    }

    public LocalDateTime getCreate_Date() {
        return create_Date;
    }

    public void setCreate_Date(LocalDateTime create_Date) {
        this.create_Date = create_Date;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public Timestamp getLast_Update() {
        return last_Update;
    }

    public void setLast_Update(Timestamp last_Update) {
        this.last_Update = last_Update;
    }

    public String getLast_Updated_By() {
        return last_Updated_By;
    }

    public void setLast_Updated_By(String last_Updated_By) {
        this.last_Updated_By = last_Updated_By;
    }

    public int getCountry_ID() {
        return country_ID;
    }

    public void setCountry_ID(int country_ID) {
        this.country_ID = country_ID;
    }
    @Override
    public String toString(){
        return division;
    }


}
