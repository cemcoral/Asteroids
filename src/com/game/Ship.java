package com.game;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

public class Ship {

    private Polygon character;
    private Point2D movement;

    public Ship (int x, int y) {
        this.character = new Polygon(-5, -5, 10, 0, -5, 5);
        this.character.setTranslateX(x);
        this.character.setTranslateY(y);

        this.movement = new Point2D(0, 0);
    }

    public Polygon getCharacter () {
        return this.character;
    }

    public void turnLeft () {
        this.character.setRotate(this.character.getRotate() - 5);
    }

    public void turnRight () {
        this.character.setRotate(this.character.getRotate() + 5);
    }

    public void move () {
        // hareket koordinatları geminin varolan koordinatlarıyla toplanır yeni gemi koordinatları hesaplanır
        this.character.setTranslateX(this.character.getTranslateX() + this.movement.getX());
        this.character.setTranslateY(this.character.getTranslateY() + this.movement.getY());
    }

    public void accelerate () {
        // geminin dönme açısındaki değişiklik x ve y düzlemlerine  uygun şekilde eklenir
        double changeX = Math.cos(Math.toRadians(this.character.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.character.getRotate()));

        // değişim çok büyüdüğünden sadece %5'i alınır
        changeX *= 0.05;
        changeY *= 0.05;

        // değişimler harekete eklenir
        this.movement = this.movement.add(changeX, changeY);
    }
}
