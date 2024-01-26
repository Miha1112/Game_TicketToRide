package hr.algebra.game.view;

import javafx.scene.shape.Circle;

import javafx.scene.paint.Color;
public class CityRenderer {


    public Circle renderCity(double squareSize) {
        Circle cityCircle = new Circle(squareSize / 2);
        cityCircle.setFill(Color.RED);
        return cityCircle;
    }

}
