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

    Button itemOverview, addNewCustomer, categorieOverview;

    public MainMenu(Main main){
        this.main = main;
    }

    @Override
    public void reload() {

    }

    public Scene createAndGetScene(){
        gridHandler = new GridHandler();

        itemOverview = new Button("Item aanpassen, toevoegen, verwijderen of tijdelijke prijs aan item toevoegen.");
        itemOverview.setOnMouseClicked(event -> {
            main.changeScene(main.getItemsList());
        });

        addNewCustomer = new Button("Klantenkaart registreren, aanpassen of verwijderen.");

        categorieOverview = new Button("Categorie aanpassen, toevoegen verwijderen.");
        categorieOverview.setOnMouseClicked(event -> {
            main.changeScene(main.getCategorieList());
        });

        gridHandler.add(0, 0, itemOverview, false);
        gridHandler.add(0, 1, addNewCustomer, false);
        gridHandler.add(0, 2, categorieOverview, false);

        scene = gridHandler.getGridAsScene();
        return scene;
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
