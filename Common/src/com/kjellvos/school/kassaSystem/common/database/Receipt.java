package com.kjellvos.school.kassaSystem.common.database;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by kjell on 17-6-2017.
 */
public class Receipt {
    private IntegerProperty id;
    private StringProperty firstName, lastName, whenReceipt;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");


    public Receipt(int id, String firstName, String lastName, LocalDateTime whenReceipt) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        String temp = whenReceipt.format(formatter);
        this.whenReceipt = new SimpleStringProperty(temp);
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

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String setLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setWhenReceipt(String whenReceipt) {
        this.whenReceipt.set(whenReceipt);
    }

    public String getWhenReceipt() {
        return whenReceipt.get();
    }

    public StringProperty whenReceiptProperty() {
        return whenReceipt;
    }
}