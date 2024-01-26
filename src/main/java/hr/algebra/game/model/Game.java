package hr.algebra.game.model;


import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {
    @Serial
    private static final long serialVersionUID=4L;
    private static final List<City> cities = City.getCroatianCities();
    private List<Player> players;
    private int currentPlayerIndex;
    private int turnCounter = 0;

    public void incrementTurnCounter() {
        turnCounter++;
    }

    public boolean isFirstTurn() {
        return turnCounter == 0;
    }

    @Override
    public String toString() {
        return "Game{" +
                "cities=" + cities +
                ", players=" + players +
                ", currentPlayerIndex=" + currentPlayerIndex +
                '}';
    }

    public Game() {
        players = new ArrayList<>();
        currentPlayerIndex = 0;
        initializeGame();
    }

    private void initializeGame() {
    }

    public void addPlayer(Player player) {
        players.add(player);
    }


    public static List<City> getCities() {
        return cities;
    }

    public Player switchToNextPlayer() {

        if (players.isEmpty()) {
            throw new IllegalStateException("No players are set for the game");
        }

        // Move to the next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        return getCurrentPlayer();
    }

    public Player getCurrentPlayer() {
        if (players.isEmpty()) {
            throw new IllegalStateException("No players are set for the game");
        }


        return players.get(currentPlayerIndex);
    }

    public void removePlayers() {
        players.clear();
    }


}