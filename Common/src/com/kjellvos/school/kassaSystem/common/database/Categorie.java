package com.kjellvos.school.kassaSystem.common.database;

/**
 * Created by kjevo on 3/28/17.
 */
public class Categorie {
    private int id;
    private String name;

    public Categorie(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
