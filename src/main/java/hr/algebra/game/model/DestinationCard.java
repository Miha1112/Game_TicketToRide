package hr.algebra.game.model;

import javafx.scene.image.Image;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

public class DestinationCard implements Serializable {

    @Serial
    private static final long serialVersionUID=2L;
    private final Set<Route> requiredRoutes;

    private int points;
    public DestinationCard(Set<Route> requiredRoutes) {
        this.requiredRoutes = requiredRoutes;
        computePoints();
    }

    @Override
    public String toString() {
        return "DestinationCard{" +
                "requiredRoutes=" + requiredRoutes +
                ", points=" + points +
                '}';
    }

    private void computePoints() {

        points = requiredRoutes.stream()
                .mapToInt(Routes::getRoutePathLength)
                .sum();
    }

    public Set<Route> getRequiredRoutes() {
        return requiredRoutes;
    }


    public String getDestinationImagePath() {
        // Check if the requiredRoutes set is not empty and get the first route
        if (!requiredRoutes.isEmpty()) {
            Route firstRoute = requiredRoutes.iterator().next();
            // Check if the route has a default color
            if (firstRoute.getDefaultColor() != null) {
                return "/hr/algebra/game/view/images/DirectionCards/" + firstRoute.name() + ".jpg";
            }
        }
        // Return the default image path if the set is empty or the route has no default color
        return getDefaultDestinationImagePath();
    }

    public static String getDefaultDestinationImagePath() {
        return "/hr/algebra/game/view/images/DirectionCards/RouteDeck.jpg";
    }

    public RouteColor getDefaultRouteColor() {
        if (!requiredRoutes.isEmpty()) {
            Route firstRoute = requiredRoutes.iterator().next();
            return firstRoute.getDefaultColor(); 
        }
        return null;
    }
    public int getPoints() {
        return points;
    }

    public void setImage(Image image) {
    }
}