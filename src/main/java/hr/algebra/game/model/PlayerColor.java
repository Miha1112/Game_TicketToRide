package hr.algebra.game.model;
import javafx.scene.paint.Color;
public enum PlayerColor {
    BLUE, BROWN;
    public Color getFxColor() {

        return switch (this) {
            case BLUE -> Color.BLUE;
            case BROWN -> Color.BROWN;
            default -> throw new IllegalArgumentException("Unknown color");
        };
    }

}
