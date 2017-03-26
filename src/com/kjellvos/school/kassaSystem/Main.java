package com.kjellvos.school.kassaSystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Stack;

public class Main extends Application {
    private Stage primaryStage;
    private Stack<Scene> scenes = new Stack<>();
    private Scene scene;
    private MainMenu mainMenu;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;

        mainMenu = new MainMenu(this);
        scene = scenes.push(mainMenu.createAndGetScene());

        primaryStage.setTitle("Kassa V0.1");
        primaryStage.setWidth(800D);
        primaryStage.setHeight(600D);
        primaryStage.setScene(scene);
        primaryStage.show();
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
}
