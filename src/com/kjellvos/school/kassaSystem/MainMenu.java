package com.kjellvos.school.kassaSystem;

import com.kjellvos.os.gridHandler.GridHandler;
import com.kjellvos.school.kassaSystem.common.Extensions.MainScene;
import com.kjellvos.school.kassaSystem.common.database.Categorie;
import com.kjellvos.school.kassaSystem.common.database.Item;
import com.kjellvos.school.kassaSystem.common.interfaces.SceneImplementation;
import com.kjellvos.school.kassaSystem.database.Database;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * Created by kjell on 10-3-2017.
 */
public class MainMenu extends MainScene implements SceneImplementation {
    private Database database;

    private Scene scene;

    private Button productenButton, bonnenButton, kaartenButton, kassaLadeButton, corrigeerButton, betaalButton;
    private TitledPane categorieListView;
    private ListView bonListView;

    private GridHandler gridHandler;

    public MainMenu(Stage stage) {
        super(stage);
        try {
            database = new Database(this);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Whoops!");
            alert.setHeaderText("Er lijkt geen database te zijn!");
            alert.setContentText("Als er wel databasse hoort te zijn, vraag dan uw administrator om hulp!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
            alert.showAndWait();

            Platform.exit();
            e.printStackTrace();
        }

        super.getPrimaryStage().setTitle("Kassa V0.1");
        super.getPrimaryStage().setWidth(800D);
        super.getPrimaryStage().setHeight(600D);
        super.getPrimaryStage().setResizable(false);
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

        ObservableList categories = database.getCategories();
        TitledPane[] titledPanes = new TitledPane[categories.size()];
        Accordion accordion = new Accordion();
        for (int i = 0; i < categories.size(); i++) {
            ObservableList items = database.getItemsByCategorie(((Categorie)categories.get(i)).getId());
            VBox vBox = new VBox();
            for (int j = 0; j < items.size(); j++) {
                Text text = new Text(((Item)items.get(j)).getPrice());
                vBox.getChildren().add(text);
            }
            titledPanes[i] = new TitledPane(((Categorie)categories.get(i)).getName(), vBox);
        }
        accordion.getPanes().addAll(titledPanes);

        bonListView = new ListView();

        gridHandler.add(0,0, productenButton, false);
        gridHandler.add(0, 1, bonnenButton, false);
        gridHandler.add(0, 2, kaartenButton, false);
        gridHandler.add(0, 4, kassaLadeButton, false);

        gridHandler.add(1, 0, accordion, 1, 5, false);

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
