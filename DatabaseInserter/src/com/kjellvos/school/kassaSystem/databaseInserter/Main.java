package com.kjellvos.school.kassaSystem.databaseInserter;

import com.kjellvos.school.kassaSystem.common.functions.RegexAndFocusFunctions;
import com.kjellvos.school.kassaSystem.common.interfaces.SceneChanger;
import com.kjellvos.school.kassaSystem.databaseInserter.database.Database;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * Created by kjevo on 3/24/17.
 */
public class Main extends Application{
    private MainMenu mainMenu;

    @Override
    public void start(Stage primaryStage){
        mainMenu = new MainMenu(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
