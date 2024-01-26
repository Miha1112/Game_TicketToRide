package hr.algebra.game.model;

import javafx.scene.paint.Color;
import java.util.EnumMap;
import java.util.Map;

public class Routes {
    private final GameBoard gameBoard;
    private static final Map<Route, int[][]> routePaths = new EnumMap<>(Route.class);

    public static int getRoutePathLength(Route route) {
        return routePaths.containsKey(route) ? routePaths.get(route).length : 0;
    }
    public Routes(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        initializeRoutes();
    }

    public static void initializeRoutes() {
       routePaths.put(Route.ZAGREB_TO_CAKOVEC, new int[][] {
                {3, 3},
                {2, 3},
                {1, 3},
                {1, 2},
                {1, 1},
       });

        routePaths.put(Route.ZAGREB_TO_RIJEKA, new int[][] {
                {3, 5},
                {3, 6},
                {3, 7},
        });

        routePaths.put(Route.ZAGREB_TO_KARLOVAC, new int[][] {
                {3, 5},
                {4, 6},
                {5, 7},
                {6, 8},
                {7, 9},
                {8, 10},
                {9, 10},
       });

        routePaths.put(Route.CAKOVEC_TO_VARAZDIN, new int[][] {
                {3, 1},
                {4, 1},
                {4, 2},
                {5, 2},
                {6, 2},
                {7, 2}
        });

        routePaths.put(Route.VARAZDIN_TO_ZAGREB, new int[][] {
                {8, 3},
                {8, 4},
                {7, 4},
                {6, 4},
                {5, 4},
                {4, 4}
        });

        routePaths.put(Route.PULA_TO_RIJEKA, new int[][] {
                {2, 9},
                {3, 9},
        });

        routePaths.put(Route.RIJEKA_TO_ZADAR, new int[][] {
                {4, 8},
                {5, 9},
                {6, 10},
                {6, 11},
                {6, 12},
        });

        routePaths.put(Route.ZADAR_TO_SIBENIK, new int[][] {
                {7, 13},
                {8, 14},
        });

        routePaths.put(Route.SIBENIK_TO_SPLIT, new int[][] {
                {9, 15},
                {9, 16},
        });

        routePaths.put(Route.SPLIT_TO_DUBROVNIK, new int[][] {
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
        });

        routePaths.put(Route.SPLIT_TO_MAKARSKA, new int[][] {
                {9, 18},
                {10, 18},
                {11, 18},
                {12, 18},
                {12, 17},
                {12, 16},
                {12, 15},
                {13, 15},
        });

        routePaths.put(Route.MAKARSKA_TO_IMOTSKI, new int[][] {
                {17, 13},
                {16, 14},
                {15, 14},
        });

        routePaths.put(Route.IMOTSKI_TO_DUBROVNIK, new int[][] {
                {15, 16},
                {16, 17},
                {16, 18},
        });

        routePaths.put(Route.OSIJEK_TO_VUKOVAR, new int[][] {
                {15, 3},
                {14, 3},
                {16, 3},
                {17, 2},
                {18, 1},
        });

        routePaths.put(Route.OSIJEK_TO_SLAVONSKIBROD, new int[][] {
                {14, 5},
                {14, 6},
                {15, 6}
        });

       routePaths.put(Route.PULA_TO_POREC, new int[][] {
                {1, 10},
                {2, 10},
                {2, 11},
                {2, 12},
                {2, 13},
                {2, 14},
                {2, 15},
                {2, 16},
                {2, 17},
        });
    }

    public void setAllRoutes() {
        for (Route route : Route.values()) {
            setRoute(route, route.getColor());
        }
    }
    public void setRoute(Route route, RouteColor color) {
        int[][] path = routePaths.get(route);
        if (path != null) {
            highlightRoute(path, color);
        }
    }
    public static int getPoints(Route route) {
        // Returns the length of the route path, which represents the points.
        return routePaths.containsKey(route) ? routePaths.get(route).length : 0;
    }

    private void highlightRoute(int[][] path, RouteColor color) {
        for (int[] coords : path) {
            gameBoard.highlightSquare(coords[0], coords[1], Color.valueOf(color.name()));
        }
    }
}