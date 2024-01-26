package hr.algebra.game.model;

public enum Route {
    ZAGREB_TO_CAKOVEC(RouteColor.YELLOW),
    ZAGREB_TO_RIJEKA(RouteColor.GREEN),
    ZAGREB_TO_KARLOVAC(RouteColor.ORANGE),
    CAKOVEC_TO_VARAZDIN(RouteColor.ORANGE),
    VARAZDIN_TO_ZAGREB(RouteColor.YELLOW),
    PULA_TO_RIJEKA(RouteColor.GREEN),
    RIJEKA_TO_ZADAR(RouteColor.ORANGE),
    ZADAR_TO_SIBENIK(RouteColor.YELLOW),
    SIBENIK_TO_SPLIT(RouteColor.GREEN),
    SPLIT_TO_DUBROVNIK(RouteColor.ORANGE),
    SPLIT_TO_MAKARSKA(RouteColor.YELLOW),
    MAKARSKA_TO_IMOTSKI(RouteColor.GREEN),
    IMOTSKI_TO_DUBROVNIK(RouteColor.ORANGE),
    OSIJEK_TO_VUKOVAR(RouteColor.YELLOW),
    PULA_TO_POREC(RouteColor.GREEN),
    OSIJEK_TO_SLAVONSKIBROD(RouteColor.ORANGE);
    private final RouteColor defaultColor;

    Route(RouteColor defaultColor) {
        this.defaultColor = defaultColor;
    }

    public RouteColor getDefaultColor() {
        return this.defaultColor;
    }

    public RouteColor getColor() {
        return this.defaultColor;
    }
    public int getPoints() {
        return Routes.getPoints(this);
    }



    public String getRouteName() {
        return this.name(); // This will return the name of the enum constant
    }
}