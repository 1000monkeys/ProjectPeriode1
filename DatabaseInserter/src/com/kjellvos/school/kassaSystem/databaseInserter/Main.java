package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.school.kassaSystem.databaseInserter.database.Database;
import com.kjellvos.school.kassaSystem.databaseInserter.functions.RegexAndFocusFunctions;
import com.kjellvos.school.kassaSystem.databaseInserter.interfaces.SceneImplementation;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Stack;

/**
 * Created by kjevo on 3/24/17.
 */
//TODO na invoeren doorgestuurd naar de lijst met items
public class Main extends Application{
    private Database database;
    private Stage primaryStage;
    private RegexAndFocusFunctions regexAndFocusFunctions;
    private MainMenu mainMenu;
    private GetItemsList getItemsList;
    private GetCategorieList getCategorieList;
    private AddNewItem addNewItem;
    private AddNewCategorie addNewCategorie;
    private AddNewTemporaryPrice addNewTemporaryPrice;
    private OverviewItem overviewItem;

    private Stack<SceneImplementation> scenes = new Stack<>();
    private SceneImplementation scene;


    @Override
    @SuppressWarnings("ErrorNotRethrown")
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        regexAndFocusFunctions = new RegexAndFocusFunctions();
        try {
            database = new Database(this);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Whoops!");
            alert.setHeaderText("Er lijkt geen data te zijn!");
            alert.setContentText("Als er wel data hoort te zijn, vraag dan uw administrator om hulp!");
            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
            alert.showAndWait();

            Platform.exit();
            e.printStackTrace();
        } catch (PropertyVetoException | IOException e) {
            e.printStackTrace();
        }
        getItemsList = new GetItemsList(this);
        getCategorieList = new GetCategorieList(this);
        mainMenu = new MainMenu(this);
        addNewItem = new AddNewItem(this);
        addNewCategorie = new AddNewCategorie(this);
        addNewTemporaryPrice = new AddNewTemporaryPrice(this);
        overviewItem = new OverviewItem(this);

        scenes.add(mainMenu);
        this.primaryStage.setTitle("Kassa Database Inserter V0.1");
        this.primaryStage.setWidth(800D);
        this.primaryStage.setHeight(600D);
        this.primaryStage.setScene(scenes.peek().createAndGetScene());
        this.primaryStage.show();
    }

    public void changeScene(SceneImplementation sceneImplementation){
        scenes.push(sceneImplementation);
        scene = sceneImplementation;
        primaryStage.setScene(scene.createAndGetScene());
    }

    public void returnToPreviousScene(){
        if (scenes.size() > 1) {
            scenes.pop();
            scene = scenes.get(scenes.size()-1);
            scene.reload();
            primaryStage.setScene(scene.getScene());
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public AddNewItem getAddNewItem() {
        return addNewItem;
    }

    public MainMenu getMainMenu(){
        return mainMenu;
    }

    public AddNewTemporaryPrice getAddNewTemporaryPrice(int id) {
        addNewTemporaryPrice.setId(id);
        return addNewTemporaryPrice;
    }

    public Database getDatabase() {
        return database;
    }

    public RegexAndFocusFunctions getRegexAndFocusFunctions() {
        return regexAndFocusFunctions;
    }

    public GetItemsList getItemsList() {
        return getItemsList;
    }

    public OverviewItem getOverviewItem(int id){
        overviewItem.setId(id);
        return overviewItem;
    }

    public SceneImplementation getScene() {
        return scene;
    }

    public AddNewCategorie getAddNewCategorie() {
        return addNewCategorie;
    }

    public GetCategorieList getCategorieList() {
        return getCategorieList;
    }
}
