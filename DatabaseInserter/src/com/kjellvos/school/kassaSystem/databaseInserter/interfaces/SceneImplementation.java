package com.kjellvos.school.kassaSystem.databaseInserter.interfaces;

import javafx.scene.Scene;

/**
 * Created by kjevo on 3/26/17.
 */
public interface SceneImplementation {
    Scene createAndGetScene();

    void reload();

    Scene getScene();
}