package hr.algebra.game.model;

import javafx.scene.paint.Color;

public enum RouteColor {
    YELLOW(Color.YELLOW),
    GREEN(Color.GREEN),
    ORANGE(Color.ORANGE),
    EMPTY(Color.TRANSPARENT); // or any other default color

    private final Color fxColor;

    RouteColor(Color fxColor) {
        this.fxColor = fxColor;
    }

    public Color getFxColor() {
        return fxColor;
    }
}