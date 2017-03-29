package com.kjellvos.school.kassaSystem.common.Extensions;

import com.kjellvos.school.kassaSystem.common.interfaces.SceneChanger;
import com.kjellvos.school.kassaSystem.common.interfaces.SceneImplementation;
import javafx.stage.Stage;

import java.util.Stack;

/**
 * Created by kjevo on 3/29/17.
 */
public class MainScene implements SceneChanger{
    private Stage stage;
    private Stack<SceneImplementation> scenes = new Stack<>();
    private SceneImplementation scene;

    public MainScene(Stage stage){
        this.stage = stage;
    }

    public void changeScene(SceneImplementation sceneImplementation){
        scenes.push(sceneImplementation);
        scene = sceneImplementation;
        stage.setScene(scene.createAndGetScene());
    }

    public void returnToPreviousScene(){
        if (scenes.size() > 1) {
            scenes.pop();
            scene = scenes.get(scenes.size() - 1);
            scene.reload();
            stage.setScene(scene.getScene());
        }
    }

    public SceneImplementation getCurrentScene() {
        return scene;
    }

    public Stack<SceneImplementation> getScenes(){
        return scenes;
    }

    public Stage getPrimaryStage() {
        return stage;
    }
}
