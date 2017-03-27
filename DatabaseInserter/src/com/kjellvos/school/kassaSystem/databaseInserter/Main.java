package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.school.kassaSystem.databaseInserter.database.Database;
import com.kjellvos.school.kassaSystem.databaseInserter.functions.RegexAndFocusFunctions;
import com.kjellvos.school.kassaSystem.databaseInserter.interfaces.SceneImplementation;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
    private AddNewItem addNewItem;
    private AddNewTemporaryPrice addNewTemporaryPrice;
    private OverviewItem overviewItem;

    private Stack<SceneImplementation> scenes = new Stack<>();
    private SceneImplementation scene;

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        regexAndFocusFunctions = new RegexAndFocusFunctions();
        database = new Database(this);
        getItemsList = new GetItemsList(this);
        mainMenu = new MainMenu(this);
        addNewItem = new AddNewItem(this);
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
}
