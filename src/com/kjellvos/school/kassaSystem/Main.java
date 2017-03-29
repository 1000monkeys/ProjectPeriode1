package com.kjellvos.school.kassaSystem;

import com.kjellvos.school.kassaSystem.common.Extensions.MainScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private MainScene mainScene;

    @Override
    public void start(Stage primaryStage){
        mainScene = new MainMenu(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
