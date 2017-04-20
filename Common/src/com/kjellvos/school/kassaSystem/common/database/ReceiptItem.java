package com.kjellvos.school.kassaSystem.common.database;

import javafx.beans.property.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by kjell on 9-4-2017.
 */
public class ReceiptItem {
    private IntegerProperty id, amount;
    private StringProperty name, totalPrice;
    private DoubleProperty price;

    public ReceiptItem(int id, int amount, String name, double price){
        this.id = new SimpleIntegerProperty(id);
        this.amount = new SimpleIntegerProperty(amount);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        BigDecimal bigDecimal = new BigDecimal(price * amount);
        totalPrice = new SimpleStringProperty(bigDecimal.toPlainString());
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

    public String getTotalPrice(){
        BigDecimal bigDecimal = new BigDecimal(Double.parseDouble(totalPrice.get()));
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        return bigDecimal.toPlainString();
    }

    public void setTotalPrice(double total){
        BigDecimal bigDecimal = new BigDecimal(total);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        totalPrice.set(bigDecimal.toPlainString());
    }

    public StringProperty totalPriceProperty(){
        BigDecimal bigDecimal = new BigDecimal(Double.parseDouble(totalPrice.get()));
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        totalPrice.set(bigDecimal.toPlainString());
        return totalPrice;
    }

    public void recaltulateTotal(){
        BigDecimal bigDecimal = new BigDecimal(price.get() * amount.get());
        totalPrice.set(bigDecimal.toPlainString());
    }
}
