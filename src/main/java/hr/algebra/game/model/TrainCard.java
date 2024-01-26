package hr.algebra.game.model;

import java.io.Serial;
import java.io.Serializable;

public class TrainCard implements Serializable {

    @Serial
    private static final long  serialVersionUID=3L;
    private final RouteColor color;

    public TrainCard(RouteColor color) {
        this.color = color;
    }

    public RouteColor getColor() {
        return color;
    }

    public static String getDefaultImagePath() {
        return "/hr/algebra/game/view/images/TrainCards/TrainCardsDeck.jpg";
    }

    public String getImagePath() {
        if (color != null && color!= RouteColor.EMPTY) {
            return "/hr/algebra/game/view/images/TrainCards/" + color.name() + ".jpg";
        }

        return getDefaultImagePath(); // Return the default image path if color is null
    }


    @Override
    public String toString() {
        return "TrainCard{" + "color=" + color + '}';
    }
}