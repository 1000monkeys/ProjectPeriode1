package com.kjellvos.school.kassaSystem.common.interfaces;

import javafx.stage.Stage;

/**
 * Created by kjevo on 3/29/17.
 */
public interface SceneChanger {
    void changeScene(SceneImplementation sceneImplementation);

    void returnToPreviousScene();

    Stage getPrimaryStage();

    SceneImplementation getCurrentScene();
}

