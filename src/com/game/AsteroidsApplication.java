package com.game;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.*;

public class AsteroidsApplication extends Application {

    public static int WIDTH = 300;
    public static int HEIGHT = 200;

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage stage) {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);

        Ship ship = new Ship(WIDTH / 2, HEIGHT / 2);
        List<Asteroid> asteroids = generateAsteroids();

        // node classını inherit eden polygon ve circle rotate methodunu kullanabilir
        pane.getChildren().add(ship.getCharacter());
        asteroids.forEach(asteroid -> {
            pane.getChildren().add(asteroid.getCharacter());
        });

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
                asteroids.forEach(asteroid -> asteroid.move());

                asteroids.forEach(asteroid -> {
                    if (ship.collide(asteroid)) {
                        stop();
                    }
                });
            }
        }.start();

        stage.setTitle("Asteroids!");
        stage.setScene(scene);
        stage.show();
    }

    private List<Asteroid> generateAsteroids () {
        List<Asteroid> asteroidList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Random rnd = new Random();
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH / 3), rnd.nextInt(HEIGHT / 2));
            asteroidList.add(asteroid);
        }

        return asteroidList;
    }
}
