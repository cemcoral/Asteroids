package com.game;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public abstract class Character {

    private Polygon character;
    private Point2D movement;

    public Character (Polygon polygon, int x, int y) {
        this.character = polygon;
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

    public Point2D getMovement () {
        return movement;
    }

    public void setMovement (Point2D movement) {
        this.movement = movement;
    }

    public void move () {
        // hareket koordinatları geminin varolan koordinatlarıyla toplanır yeni gemi koordinatları hesaplanır
        this.character.setTranslateX(this.character.getTranslateX() + this.movement.getX());
        this.character.setTranslateY(this.character.getTranslateY() + this.movement.getY());

        // eğer karakter ekranın solundan çıkmışsa

        if (this.character.getTranslateX() < 0) {
            this.character.setTranslateX(this.character.getTranslateX() + AsteroidsApplication.WIDTH);
        }

        // eğer karakter ekranın sağından çıkmışsa
        if (this.character.getTranslateX() > AsteroidsApplication.WIDTH) {
            this.character.setTranslateX(this.character.getTranslateX() % AsteroidsApplication.WIDTH);
        }

        // eğer karakter ekranın üstünden çıkmışsa
        if (this.character.getTranslateY() < 0) {
            this.character.setTranslateY(this.character.getTranslateY() + AsteroidsApplication.HEIGHT);
        }

        // eğer karakter ekranın aşağısından çıkmışsa
        if (this.character.getTranslateY() > AsteroidsApplication.HEIGHT) {
            this.character.setTranslateY(this.character.getTranslateY() % AsteroidsApplication.HEIGHT);
        }
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

    public boolean collide (Character other) {
        Shape collisionArea = Shape.intersect(this.getCharacter(), other.getCharacter());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }
}
