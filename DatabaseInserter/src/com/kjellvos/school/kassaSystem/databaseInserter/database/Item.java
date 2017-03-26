package com.kjellvos.school.kassaSystem.databaseInserter.database;

import javafx.scene.control.Button;

/**
 * Created by kjevo on 3/25/17.
 */
public class Item {
    private int id;
    private String name, description;
    private float price;
    private Button button;

    public Item(int id, String name, String description, float price, Button button){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.button = button;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public Button getButton(){
        return button;
    }
}
