package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.databaseInserter.interfaces.SceneImplementation;
import javafx.scene.Scene;
import javafx.scene.control.Button;

/**
 * Created by kjevo on 3/24/17.
 */
public class MainMenu implements SceneImplementation {
    Main main;
    GridHandler gridHandler;

    Scene scene;

    Button addNewItem, addNewTemporaryPrice, addNewCustomer, addNewCategorie, categorieOverview;

    public MainMenu(Main main){
        this.main = main;
    }

    @Override
    public void reload() {

    }

    public Scene createAndGetScene(){
        gridHandler = new GridHandler();

        addNewItem = new Button("Nieuwe item toevoegen.");
        addNewItem.setOnMouseClicked(event -> {
            main.changeScene(main.getAddNewItem());
        });

        addNewTemporaryPrice = new Button("Item aanpassen of tijdelijke prijs aan item toevoegen.");
        addNewTemporaryPrice.setOnMouseClicked(event -> {
            main.changeScene(main.getItemsList());
        });

        addNewCustomer = new Button("Nieuwe klantenkaard registereen.");

        addNewCategorie = new Button("Nieuwe item categorie toevoegen.");
        addNewCategorie.setOnMouseClicked(event -> {
                main.changeScene(main.getAddNewCategorie());
        });

        categorieOverview = new Button("Lijst van alle categorieen bekijken.");
        categorieOverview.setOnMouseClicked(event -> {
            main.changeScene(main.getCategorieList());
        });

        gridHandler.add(0, 0, addNewItem, false);
        gridHandler.add(0, 1, addNewTemporaryPrice, false);
        gridHandler.add(0, 2, addNewCustomer, false);
        gridHandler.add(0, 3, addNewCategorie, false);
        gridHandler.add(0, 4, categorieOverview, false);

        scene = gridHandler.getGridAsScene();
        return scene;
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
