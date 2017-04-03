package com.kjellvos.school.kassaSystem.common.database;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

/**
 * Created by kjevo on 3/31/17.
 */
public class CustomerCard {
    private IntegerProperty id, houseNumber;
    private StringProperty firstName, lastName, streetName, telephoneNumber;
    private Button button;

    public CustomerCard(int id, String firstName, String lastName, String streetName, Button button){
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.streetName = new SimpleStringProperty(streetName);
        this.button = button;
    }

    public CustomerCard(int id, String firstName, String lastName, String streetName, int houseNumber, String telephoneNumber) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.streetName = new SimpleStringProperty(streetName);
        this.houseNumber = new SimpleIntegerProperty(houseNumber);
        this.telephoneNumber = new SimpleStringProperty(telephoneNumber);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getHouseNumber() {
        return houseNumber.get();
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber.set(houseNumber);
    }

    public IntegerProperty houseNumberProperty() {
        return houseNumber;
    }

    public String getTelephoneNumber() {
        return telephoneNumber.get();
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber.set(telephoneNumber);
    }

    public StringProperty telephoneNumberProperty() {
        return telephoneNumber;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getStreetName() {
        return streetName.get();
    }

    public void setStreetName(String streetName) {
        this.streetName.set(streetName);
    }

    public StringProperty streetNameProperty() {
        return streetName;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }
}
