package com.game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application {

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage stage) {
        Pane pane = new Pane();
        pane.setPrefSize(600, 400);

        Polygon ship = new Polygon(-5, -5, 10, 0, -5, 5);
        ship.setTranslateX(300);
        ship.setTranslateY(200);

        // node classını inherit eden polygon ve circle rotate methodunu kullanabilir

        pane.getChildren().add(ship);

        Scene scene = new Scene(pane);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                ship.setRotate(ship.getRotate() - 5);
            }
            if (event.getCode() == KeyCode.RIGHT) {
                ship.setRotate(ship.getRotate() + 5);
            }
        });

        stage.setTitle("Asteroids!");
        stage.setScene(scene);
        stage.show();
    }
}
