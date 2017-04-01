package com.kjellvos.school.kassaSystem.common.database;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

/**
 * Created by kjevo on 3/26/17.
 */
public class Price {
    IntegerProperty id;
    LocalDateTime fromWhen, tillWhen;
    StringProperty price;
    boolean defaultPrice;

    public Price(int id, LocalDateTime fromWhen, LocalDateTime tillWhen, String price){
        this.id = new SimpleIntegerProperty(id);
        this.fromWhen = fromWhen;
        this.tillWhen = tillWhen;
        this.price = new SimpleStringProperty(price);
    }

    public Price(int id, LocalDateTime fromWhen, LocalDateTime tillWhen, String price, boolean defaultPrice){
        this.id = new SimpleIntegerProperty(id);
        this.fromWhen = fromWhen;
        this.tillWhen = tillWhen;
        this.price = new SimpleStringProperty(price);
        this.defaultPrice = defaultPrice;
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

    public LocalDateTime getFromWhen() {
        return fromWhen;
    }

    public void setFromWhen(LocalDateTime fromWhen) {
        this.fromWhen = fromWhen;
    }

    public LocalDateTime getTillWhen() {
        return tillWhen;
    }

    public void setTillWhen(LocalDateTime tillWhen) {
        this.tillWhen = tillWhen;
    }

    public String getPrice() {
        return price.get();
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public StringProperty priceProperty() {
        return price;
    }

    public boolean isDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(boolean defaultPrice) {
        this.defaultPrice = defaultPrice;
    }
}
