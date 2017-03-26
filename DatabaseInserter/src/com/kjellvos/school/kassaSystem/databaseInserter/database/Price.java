package com.kjellvos.school.kassaSystem.databaseInserter.database;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by kjevo on 3/26/17.
 */
public class Price {
    int id;
    LocalDateTime fromWhen, tillWhen;
    float price;
    boolean defaultPrice;

    public Price(int id, LocalDateTime fromWhen, LocalDateTime tillWhen, float price){
        this.id = id;
        this.fromWhen = fromWhen;
        this.tillWhen = tillWhen;
        this.price = price;
    }

    public Price(int id, LocalDateTime fromWhen, LocalDateTime tillWhen, float price, boolean defaultPrice){
        this.id = id;
        this.fromWhen = fromWhen;
        this.tillWhen = tillWhen;
        this.price = price;
        this.defaultPrice = defaultPrice;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getFromWhen() {
        return fromWhen;
    }

    public LocalDateTime getTillWhen() {
        return tillWhen;
    }

    public float getPrice() {
        return price;
    }

    public boolean getDefaultPrice() {
        return defaultPrice;
    }
}
