package com.kjellvos.school.kassaSystem;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.common.Extensions.MainScene;
import com.kjellvos.school.kassaSystem.common.interfaces.SceneImplementation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by kjell on 10-3-2017.
 */
public class MainMenu extends MainScene implements SceneImplementation {
    private Scene scene;

    private Button productenButton, bonnenButton, kaartenButton, kassaLadeButton, corrigeerButton, betaalButton;
    private TitledPane categorieListView;
    private ListView bonListView;

    private GridHandler gridHandler;

    public MainMenu(Stage stage) {
        super(stage);
        super.getPrimaryStage().setTitle("Kassa V0.1");
        super.getPrimaryStage().setWidth(800D);
        super.getPrimaryStage().setHeight(600D);
        super.changeScene(this);
        super.getPrimaryStage().show();
    }

    public Scene createAndGetScene() {
        gridHandler = new GridHandler();

        productenButton = new Button("Producten");
        bonnenButton = new Button("Bonnen");
        kaartenButton = new Button("Kaarten");
        kassaLadeButton = new Button("Kassa lade");
        corrigeerButton = new Button("Corrigeer");
        betaalButton = new Button("Betaal");

        AnchorPane pane = new AnchorPane();
        TitledPane categorie1 = new TitledPane("Categorie 1", new Pane());
        pane.setTopAnchor(categorie1, 0D);
        pane.setBottomAnchor(categorie1, 0D);
        pane.setLeftAnchor(categorie1, 0D);
        pane.setRightAnchor(categorie1, 0D);
        pane.getChildren().add(categorie1);

        categorieListView = new TitledPane("Categorieen", pane);
        categorieListView.setCollapsible(false);
        bonListView = new ListView();

        gridHandler.add(0,0, productenButton, false);
        gridHandler.add(0, 1, bonnenButton, false);
        gridHandler.add(0, 2, kaartenButton, false);
        gridHandler.add(0, 4, kassaLadeButton, false);

        gridHandler.add(1, 0, categorieListView, 1, 5, false);

        gridHandler.add(2, 0, bonListView, 1, 3, false);
        gridHandler.add(2, 3, corrigeerButton, false);
        gridHandler.add(2, 4, betaalButton, false);

        scene = gridHandler.getGridAsScene();
        return scene;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public void reload() {
        //Should do nothing
    }
}
