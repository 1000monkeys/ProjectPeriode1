package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.school.kassaSystem.databaseInserter.database.Database;
import com.kjellvos.school.kassaSystem.databaseInserter.functions.RegexAndFocusFunctions;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Stack;

/**
 * Created by kjevo on 3/24/17.
 */
public class Main extends Application{
    private Database database;
    private Stage primaryStage;
    private RegexAndFocusFunctions regexAndFocusFunctions;
    private MainMenu mainMenu;
    private GetItemsList getItemsList;
    private AddNewItem addNewItem;
    private AddNewTemporaryPrice addNewTemporaryPrice;
    private OverviewItem overviewItem;
    private Stack<Scene> scenes = new Stack<>();
    private Scene scene;

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

        scene = scenes.push(this.getMainMenu().createAndGetScene());

        this.primaryStage.setTitle("Kassa Database Inserter V0.1");
        this.primaryStage.setWidth(800D);
        this.primaryStage.setHeight(600D);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
    }

    public void changeScene(Scene scene){
        this.scene = scene;
        scenes.push(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void returnToPreviousScene(){
        if (scenes.size() > 1) {
            scenes.pop();
            primaryStage.setScene(scenes.peek());
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

    public AddNewTemporaryPrice getAddNewTemporaryPrice() {
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

    public OverviewItem getOverviewItem(){
        return overviewItem;
    }
}
