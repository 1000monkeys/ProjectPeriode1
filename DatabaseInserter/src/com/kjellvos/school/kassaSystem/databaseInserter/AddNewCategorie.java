package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.databaseInserter.interfaces.SceneImplementation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * Created by kjevo on 3/28/17.
 */
public class AddNewCategorie implements SceneImplementation {
    Main main;
    GridHandler gridHandler;

    Scene scene;

    Button backToLastMenuButton, submitButton;
    Text categorieText;
    TextField categorieTextField;

    public AddNewCategorie(Main main){
        this.main = main;
    }

    @Override
    public Scene createAndGetScene() {
        gridHandler = new GridHandler();
        backToLastMenuButton = new Button("Terug naar vorig menu.");
        backToLastMenuButton.setOnMouseClicked(event -> {
            main.returnToPreviousScene();
        });
        gridHandler.add(0, 0, backToLastMenuButton, 2, 1, false);

        gridHandler.add(0, 1, new Text(), 1, 5, false);
        categorieText = new Text("Categorie naam:");
        gridHandler.add(0, 6, categorieText, false);
        categorieTextField = new TextField();
        gridHandler.add(1, 6, categorieTextField, false);
        gridHandler.add(0, 7, new Text(), 1, 5, false);


        submitButton = new Button("Toevoegen!");
        submitButton.setOnMouseClicked(event -> {
            main.getDatabase().newCategorieUpload(categorieTextField.getText());
        });
        gridHandler.add(0, 13, submitButton, 2, 1, false);
        scene = gridHandler.getGridAsScene();
        return scene;
    }

    @Override
    public void reload() {

    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
