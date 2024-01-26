package hr.algebra.game.model;

import javafx.scene.paint.Color;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public record City(String name, Color color, int col, int row) {

    public static List<City> getCroatianCities() {
        return List.of(
                new City("Zagreb", Color.RED, 3, 4),
                new City("Čakovec", Color.RED, 2, 1),
                new City("Pula", Color.RED, 1, 9),
                new City("Rijeka", Color.RED, 3, 8),
                new City("Zadar", Color.RED, 6, 13),
                new City("Osijek", Color.RED, 14, 4),
                new City("Slavonski Brod", Color.RED, 15, 7),
                new City("Šibenik", Color.RED, 8, 15),
                new City("Split", Color.RED, 9, 17),
                new City("Dubrovnik", Color.RED, 16, 19),
                new City("Karlovac", Color.RED, 10, 10),
                new City("Vukovar", Color.RED, 19, 1),
                new City("Makarska", Color.RED, 17, 12),
                new City("Imotski", Color.RED, 14, 15),
                new City("Poreč", Color.RED, 2, 18),
                new City("Varaždin", Color.RED, 8, 2)
        );
    }
}