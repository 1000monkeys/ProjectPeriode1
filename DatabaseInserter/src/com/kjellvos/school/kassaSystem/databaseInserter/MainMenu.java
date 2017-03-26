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

    Button addNewItem, addNewTemporaryPrice, addNewCustomer;

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

        gridHandler.add(0, 0, addNewItem, false);
        gridHandler.add(0, 1, addNewTemporaryPrice, false);

        scene = gridHandler.getGridAsScene();
        return scene;
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
