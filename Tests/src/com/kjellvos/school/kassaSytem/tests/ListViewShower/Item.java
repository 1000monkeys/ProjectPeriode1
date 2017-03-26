package com.kjellvos.school.kassaSytem.tests.ListViewShower;

/**
 * Created by kjell on 11-3-2017.
 */
public class Item {
    private int id;
    private float price;
    private String productName;

    public Item(int id, float price, String productName){
        this.id = id;
        this.price = price;
        this.productName = productName;
    }

    public float getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }
}
