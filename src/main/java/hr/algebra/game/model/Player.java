package hr.algebra.game.model;

import hr.algebra.game.controller.GameBoardController;
import javafx.application.Platform;
import javafx.scene.image.Image;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public final class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private PlayerColor color;
    private ArrayList<DestinationCard> destinationCards;
    private ArrayList<TrainCard> trainCards; // Player's hand of train cards
    private TrainCard[][] trainCardGrid; // Grid of drawn cards
    private GameBoardController gameBoardController;
    private int score;


    public Player(PlayerColor color, GameBoardController gameBoardController) {
        this.color = color;
        this.destinationCards = new ArrayList<>();
        this.trainCards = new ArrayList<>();
        this.score = 0;
        this.gameBoardController = gameBoardController;
        this.trainCardGrid = new TrainCard[Constants.DEFAULT_GRID_ROWS][Constants.DEFAULT_GRID_COLS];
        for ( int i = 0; i< trainCardGrid.length; i ++) {
            for (int j = 0; j < Constants.DEFAULT_GRID_COLS; j++) {
                trainCardGrid[i][j]=new TrainCard(RouteColor.EMPTY);
            }
        }
        System.out.println();
    }

    public void setCard(TrainCard card,DestinationCard destinationCard) {
        int[] nextPosition = findNextAvailablePosition();
        if (nextPosition[0] != -1) { // Check if a valid position was found
            int row = nextPosition[0];
            int col = nextPosition[1];

            if (card != null) {
                trainCardGrid[row][col] = card;
                gameBoardController.updateCardOnBoard(card, row, col);
            }

            if (destinationCard != null ) {
                gameBoardController.updateDestinationCardOnBoard(destinationCard);
            }
        }
    }

    private int[] findNextAvailablePosition() {
        for (int row = 0; row < trainCardGrid.length; row++) {
            for (int col = 0; col < trainCardGrid[row].length; col++) {
                if (trainCardGrid[row][col].getColor()==RouteColor.EMPTY) {
                    return new int[]{row, col};
                }
            }
        }
        return new int[]{-1, -1}; // Return an invalid position if the grid is full
    }


    public long countTrainCardsByColor(RouteColor color) {
        return trainCards.stream()
                .filter(card -> card != null && card.getColor() == color)
                .count();
    }

    public void drawDestinationCard(DestinationCard card) {
        destinationCards.add(card);
    }
    public DestinationCard getLatestDestinationCard() {
        if (!destinationCards.isEmpty()) {
            return destinationCards.get(destinationCards.size() - 1);
        }
        return null; // Return null if no destination cards are drawn yet
    }
    public RouteColor getLatestCardDefaultRouteColor() {
        DestinationCard latestCard = getLatestDestinationCard();
        if (latestCard != null) {
            return latestCard.getDefaultRouteColor();
        }
        return null; // Or handle this case as needed
    }



    public void drawTrainCard(TrainCard card) {
        trainCards.add(card);
    }


    public PlayerColor getColor() {
        return color;
    }


    public ArrayList<DestinationCard> getDestinationCardsHand() {
        return new ArrayList<>(destinationCards);
    }


    public ArrayList<TrainCard> getTrainCardsHand() {
        return new ArrayList<>(trainCards);
    }

    public void addScore(int points) {
        score += points;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "Player{" +
                "color=" + color +
                ", destinationCardsHand=" + destinationCards +
                ", trainCardsHand=" + trainCards +
                ", score=" + score +
                '}';
    }
}