package com.game;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AsteroidsApplication extends Application {

    public static int WIDTH = 800;
    public static int HEIGHT = 600;
    private int difficulty = 1;

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage stage) {

        Pane pane = new Pane();
        Text text = new Text(10, 20, "Points: 0");
        pane.setPrefSize(WIDTH, HEIGHT);

        Ship ship = new Ship(WIDTH / 2, HEIGHT / 2);
        List<Asteroid> asteroids = generateAsteroids();
        List<Projectile> projectiles = new ArrayList<>();

        pane.getChildren().add(text);
        // concurrent puanlama sistemi için atomic integer tanımlanır
        AtomicInteger points = new AtomicInteger();

        // node classını inherit eden polygon ve circle rotate methodunu kullanabilir
        pane.getChildren().add(ship.getCharacter());
        asteroids.forEach(asteroid -> {
            pane.getChildren().add(asteroid.getCharacter());
        });

        Scene scene = new Scene(pane);

        BorderPane difficultyScreen = difficultyScreen(stage, scene);

        Scene difficultyScene = new Scene(difficultyScreen);

        // key released, key pressed'in çalışması biraz yavaştır. Bu yüzden eylemleri içinde yapmak yerine keycode'ları map'e aktar
        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();

        scene.setOnKeyPressed(event -> pressedKeys.put(event.getCode(), Boolean.TRUE));
        scene.setOnKeyReleased(event -> pressedKeys.put(event.getCode(), Boolean.FALSE));

        // saniyede 60 kere (60fps) run eden animation timer başlat
        new AnimationTimer() {
            @Override
            public void handle (long now) {

                addRandomAsteroids(ship, asteroids, pane);

                if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }

                if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }

                if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }

                // sadece 3 tane projectile kabul edilir
                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 12) {

                    Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectiles.add(projectile);

                    projectile.accelerate();
                    projectile.setMovement(projectile.getMovement().normalize().multiply(3));

                    pane.getChildren().add(projectile.getCharacter());
                }

                ship.move();
                asteroids.forEach(asteroid -> asteroid.move());
                projectiles.forEach(projectile -> projectile.move());

                // projectile ile çarpışma sonucu silinmesi gereken asteroidler && projectilelar ölü olarak işaretlenir
                projectiles.forEach(projectile -> {
                    asteroids.forEach(asteroid -> {
                        if (projectile.collide(asteroid)) {
                            projectile.setAlive(false);
                            asteroid.setAlive(false);
                            // puan eklenir
                            text.setText("Points: " + points.addAndGet(1000));
                        }
                    });
                });

                // ölü projectilelar ekrandan silinir
                projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .forEach(projectile -> pane.getChildren().remove(projectile.getCharacter()));

                // ölü projectile'lar ana listeden silinir
                projectiles.removeAll(projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .collect(Collectors.toList()));

                // ölü asteroidler ekrandan silinir
                asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .forEach(asteroid -> pane.getChildren().remove(asteroid.getCharacter()));

                // ölü asteroidler ana listeden silinir
                asteroids.removeAll(asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .collect(Collectors.toList()));

                asteroids.forEach(asteroid -> {
                    if (ship.collide(asteroid)) {
                        stop();
                    }
                });
            }
        }.start();

        stage.setTitle("Asteroids!");
        stage.setScene(difficultyScene);
        stage.show();
    }

    private void addRandomAsteroids (Ship ship, List<Asteroid> asteroids, Pane pane) {

        double choice = 0;

        if (difficulty == 1) {
            choice = 0.005;
        } else if (difficulty == 2) {
            choice = 0.1;
        } else if (difficulty == 3) {
            choice = 0.2;
        } else if (difficulty == 4) {
            choice = 0.5;
        }

        if (Math.random() < (choice)) {
            Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
            if (!asteroid.collide(ship)) {
                asteroids.add(asteroid);
                pane.getChildren().add(asteroid.getCharacter());
            }
        }
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

    private BorderPane difficultyScreen (Stage stage, Scene gameScene) {
        BorderPane screen = new BorderPane();

        Button easy = new Button("Easy Peazy");
        easy.setPrefWidth(100);
        Button average = new Button("Average");
        average.setPrefWidth(100);
        Button hard = new Button("Hard");
        hard.setPrefWidth(100);
        Button impossible = new Button("Get Fucked");
        impossible.setPrefWidth(100);

        VBox vbox = new VBox();
        Text text = new Text("Choose a difficulty");
        vbox.getChildren().addAll(text, easy, average, hard, impossible);
        vbox.setSpacing(10);

        screen.setCenter(vbox);
        screen.setPadding(new Insets(20, 20, 20, 20));

        easy.setOnAction(actionEvent -> {
            stage.setScene(gameScene);
        });

        average.setOnAction(actionEvent -> {
            stage.setScene(gameScene);
            this.difficulty = 2;
        });

        hard.setOnAction(actionEvent -> {
            stage.setScene(gameScene);
            this.difficulty = 3;
        });

        impossible.setOnAction(actionEvent -> {
            stage.setScene(gameScene);
            this.difficulty = 4;
        });

        return screen;
    }
}
