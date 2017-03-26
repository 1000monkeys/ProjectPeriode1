package com.kjellvos.school.kassaSystem;

import com.kjellvos.os.gridHandler.GridHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;

/**
 * Created by kjell on 10-3-2017.
 */
public class MainMenu {
    private Main main;

    private Button productenButton, bonnenButton, kaartenButton, kassaLadeButton, corrigeerButton, betaalButton;
    private TitledPane categorieListView;
    private ListView bonListView;

    private GridHandler gridHandler;

    public MainMenu(Main main) {
        this.main = main;
    }

    public Scene createAndGetScene() {
        gridHandler = new GridHandler();

        productenButton = new Button("Producten");
        bonnenButton = new Button("Bonnen");
        kaartenButton = new Button("Kaarten");
        kassaLadeButton = new Button("Kassa lade");
        corrigeerButton = new Button("Corrigeer");
        betaalButton = new Button("Betaal");

        categorieListView = new TitledPane("Categorieen", new Pane());
        bonListView = new ListView();


        gridHandler.add(0,0, productenButton, false);
        gridHandler.add(0, 1, bonnenButton, false);
        gridHandler.add(0, 2, kaartenButton, false);
        gridHandler.add(0, 4, kassaLadeButton, false);

        gridHandler.add(1, 0, categorieListView, 1, 5, false);

        gridHandler.add(2, 0, bonListView, 1, 3, false);
        gridHandler.add(2, 3, corrigeerButton, false);
        gridHandler.add(2, 4, betaalButton, false);

        return gridHandler.getGridAsScene();
    }
}
