package com.kjellvos.school.kassaSystem.databaseInserter.database;

import javafx.scene.control.Button;
import javafx.scene.image.Image;

/**
 * Created by kjevo on 3/25/17.
 */
public class Item {
    private int id;
    private String name, description, categorie;
    private float price;
    private Button button;
    private Image image;

    public Item(int id, String name, String description, float price, String categorie, Button button){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categorie = categorie;
        this.button = button;
    }

    public Item(int id, String name, String description, float price, String categorie, Image image){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categorie = categorie;
        this.image = image;
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

    public Image getImage() {
        return image;
    }

    public String getCategorie() {
        return categorie;
    }
}
