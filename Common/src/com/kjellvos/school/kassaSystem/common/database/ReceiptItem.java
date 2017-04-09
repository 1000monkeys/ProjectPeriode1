package com.kjellvos.school.kassaSystem.common.database;

import javafx.beans.property.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by kjell on 9-4-2017.
 */
public class ReceiptItem {
    IntegerProperty id, amount;
    StringProperty name;
    DoubleProperty price, totalPrice;

    public ReceiptItem(int id, int amount, String name, double price){
        this.id = new SimpleIntegerProperty(id);
        this.amount = new SimpleIntegerProperty(amount);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        totalPrice = new SimpleDoubleProperty(price * amount);
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

    public int getAmount() {
        return amount.get();
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }

    public IntegerProperty amountProperty() {
        return amount;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public Double getTotalPrice(){
        BigDecimal bigDecimal = new BigDecimal(totalPrice.get());
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public void setTotalPrice(double total){
        totalPrice.setValue(total);
    }

    public DoubleProperty totalPriceProperty(){
        BigDecimal bigDecimal = new BigDecimal(totalPrice.get());
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        totalPrice.setValue(bigDecimal.doubleValue());
        return totalPrice;
    }

    public void recaltulateTotal(){
        totalPrice.setValue(price.get()*amount.get());
    }
}
