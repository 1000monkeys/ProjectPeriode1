package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.os.gridHandler.GridHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 * Created by kjevo on 3/24/17.
 */
public class MainMenu {
    Main main;
    GridHandler gridHandler;

    Button addNewItem, addNewTemporaryPrice, addNewCustomer;

    public MainMenu(Main main){
        this.main = main;
    }

    public Scene createAndGetScene(){
        gridHandler = new GridHandler();

        addNewItem = new Button("Nieuwe item toevoegen.");
        addNewItem.setOnMouseClicked(event -> {
            main.changeScene(main.getAddNewItem().createAndGetScene());
        });

        addNewTemporaryPrice = new Button("Item aanpassen of tijdelijke prijs aan item toevoegen.");
        addNewTemporaryPrice.setOnMouseClicked(event -> {
            main.changeScene(main.getItemsList().createAndGetScene());
        });

        gridHandler.add(0, 0, addNewItem, false);
        gridHandler.add(0, 1, addNewTemporaryPrice, false);

        return gridHandler.getGridAsScene();
    }
}
