package com.game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class AsteroidsApplication extends Application {

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage stage) {
        Pane pane = new Pane();
        pane.setPrefSize(600, 400);

        Ship ship = new Ship(150,150);

        // node classını inherit eden polygon ve circle rotate methodunu kullanabilir
        pane.getChildren().add(ship.getCharacter());

        Scene scene = new Scene(pane);

        // key released, key pressed'in çalışması biraz yavaştır. Bu yüzden eylemleri içinde yapmak yerine keycode'ları map'e aktar

        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();

        scene.setOnKeyPressed(event -> pressedKeys.put(event.getCode(), Boolean.TRUE));
        scene.setOnKeyReleased(event -> pressedKeys.put(event.getCode(), Boolean.FALSE));


        // saniyede 60 kere (60fps) run eden animation timer başlat
        new AnimationTimer() {
            @Override
            public void handle (long now) {
                if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }

                if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }

                if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }

                ship.move();
            }
        }.start();

        stage.setTitle("Asteroids!");
        stage.setScene(scene);
        stage.show();
    }
}
