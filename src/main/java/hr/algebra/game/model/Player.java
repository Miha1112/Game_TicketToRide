package hr.algebra.game.model;

import hr.algebra.game.controller.GameBoardController;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
@Getter
@Setter
public final class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private PlayerColor color;
    private Long id;
    private Long roomId;
    private ArrayList<DestinationCard> destinationCards;
    private ArrayList<TrainCard> trainCards; // Player's hand of train cards
    private TrainCard[][] trainCardGrid; // Grid of drawn cards
    private int score;

    public Player(PlayerColor color) {
        this.color = color;
        this.destinationCards = new ArrayList<>();
        this.trainCards = new ArrayList<>();
        this.score = 0;
        this.trainCardGrid = new TrainCard[Constants.DEFAULT_GRID_ROWS][Constants.DEFAULT_GRID_COLS];
        for ( int i = 0; i< trainCardGrid.length; i ++) {
            for (int j = 0; j < Constants.DEFAULT_GRID_COLS; j++) {
                trainCardGrid[i][j]=new TrainCard(RouteColor.EMPTY);
            }
        }
    }

    public void setNextDestinationCard(){
        if (destinationCards.size() >1) {
            destinationCards.removeLast();
        }
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

    public ArrayList<DestinationCard> getDestinationCardsHand() {
        return new ArrayList<>(destinationCards);
    }


    public ArrayList<TrainCard> getTrainCardsHand() {
        return new ArrayList<>(trainCards);
    }

    public void addScore(int points) {
        score += points;
    }
    public DestinationCard getNextDestinationCard() {
        if (!destinationCards.isEmpty()) {
            int index = destinationCards.size() - 1;
            DestinationCard nextCard = destinationCards.get(index);
            destinationCards.remove(index);
            return nextCard;
        }
        return null;
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