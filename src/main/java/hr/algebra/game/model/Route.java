package hr.algebra.game.model;

public enum Route {
    ZAGREB_TO_CAKOVEC(RouteColor.YELLOW, new int[][]{{3, 3},{2, 3},{1, 3},{1, 2},{1, 1}}),
    ZAGREB_TO_RIJEKA(RouteColor.GREEN,new int[][] {
            {3, 5},
            {3, 6},
            {3, 7},
    }),
    ZAGREB_TO_KARLOVAC(RouteColor.ORANGE,new int[][] {
            {3, 5},
            {4, 6},
            {5, 7},
            {6, 8},
            {7, 9},
            {8, 10},
            {9, 10},
    } ),
    CAKOVEC_TO_VARAZDIN(RouteColor.ORANGE,new int[][] {
            {3, 1},
            {4, 1},
            {4, 2},
            {5, 2},
            {6, 2},
            {7, 2}
    } ),
    VARAZDIN_TO_ZAGREB(RouteColor.YELLOW, new int[][] {
            {8, 3},
            {8, 4},
            {7, 4},
            {6, 4},
            {5, 4},
            {4, 4}
    }),
    PULA_TO_RIJEKA(RouteColor.GREEN,new int[][] {
            {2, 9},
            {3, 9},
    } ),
    RIJEKA_TO_ZADAR(RouteColor.ORANGE,new int[][] {
            {4, 8},
            {5, 9},
            {6, 10},
            {6, 11},
            {6, 12},
    } ),
    ZADAR_TO_SIBENIK(RouteColor.YELLOW, new int[][] {
            {7, 13},
            {8, 14},
    }),
    SIBENIK_TO_SPLIT(RouteColor.GREEN, new int[][] {
            {9, 15},
            {9, 16},
    }),
    SPLIT_TO_DUBROVNIK(RouteColor.ORANGE, new int[][] {
            {8, 17},
            {8, 18},
            {8, 19},
            {9, 19},
            {10, 19},
            {11, 19},
            {12, 19},
            {13, 19},
            {14, 19},
            {15, 19},
    } ),
    SPLIT_TO_MAKARSKA(RouteColor.YELLOW,new int[][] {
            {9, 18},
            {10, 18},
            {11, 18},
            {12, 18},
            {12, 17},
            {12, 16},
            {12, 15},
            {13, 15},
    } ),
    MAKARSKA_TO_IMOTSKI(RouteColor.GREEN,new int[][] {
            {17, 13},
            {16, 14},
            {15, 14},
    } ),
    IMOTSKI_TO_DUBROVNIK(RouteColor.ORANGE, new int[][] {
            {15, 16},
            {16, 17},
            {16, 18},
    }),
    OSIJEK_TO_VUKOVAR(RouteColor.YELLOW,new int[][] {
            {15, 3},
            {14, 3},
            {16, 3},
            {17, 2},
            {18, 1},
    } ),
    PULA_TO_POREC(RouteColor.GREEN, new int[][] {
            {14, 5},
            {14, 6},
            {15, 6}
    }),
    OSIJEK_TO_SLAVONSKIBROD(RouteColor.ORANGE,new int[][] {
            {1, 10},
            {2, 10},
            {2, 11},
            {2, 12},
            {2, 13},
            {2, 14},
            {2, 15},
            {2, 16},
            {2, 17},
    } );
    private final RouteColor defaultColor;
    private final int[][] path;

    Route(RouteColor defaultColor, int[][] path) {
        this.defaultColor = defaultColor;
        this.path = path;
    }

    public RouteColor getDefaultColor() {
        return this.defaultColor;
    }

    public RouteColor getColor() {
        return this.defaultColor;
    }
    public int[][] getRoutePath(){
        return Routes.getRoutePath(this);
    }



    public String getRouteName() {
        return this.name(); // This will return the name of the enum constant
    }
}