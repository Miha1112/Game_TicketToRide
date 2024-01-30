package hr.algebra.game.helpers;

import hr.algebra.game.controller.GameBoardController;
import hr.algebra.game.model.Game;
import hr.algebra.game.model.Player;
import hr.algebra.game.model.PlayerColor;
import hr.algebra.game.model.TrainCard;
import hr.algebra.utilities.CardDealerUtils;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GameHelper {

    public static Game getGame() {
        return game;
    }

    public static Game game;
    private static GameBoardController gameBoardController;
    private static List<PlayerColor> availableColors;

    public static void init (GameBoardController controller) {
        gameBoardController = controller;
        availableColors = new ArrayList<>(List.of(PlayerColor.values()));
        game = new Game(); // Initialize game state

        startGame();
    }

    public static void startGame() {
        setupPlayers();
        Player currentPlayer = game.getCurrentPlayer();
        gameBoardController.setPlayer(currentPlayer);
        gameBoardController.updatePlayerIndicator(currentPlayer);
    }

    private static void setupPlayers() {
        GameBoardController gameBoardController = GameHelper.getGameBoardController();
        game.removePlayers();
        game.addPlayer(new Player(availableColors.get(0), gameBoardController));
    }


    public static void switchTurns() {
        Player nextPlayer = game.switchToNextPlayer();
        CardDealerUtils.dealCardsToPlayer();
        int cardsCount = 0;
        Color routeColor = Objects.requireNonNull(game.getCurrentPlayer().getLatestCardDefaultRouteColor()).getFxColor();
        for (int i =0;i<game.getCurrentPlayer().getTrainCardsHand().size();i++){
            if (game.getCurrentPlayer().getTrainCardsHand().get(i)!= null && game.getCurrentPlayer().getTrainCardsHand().get(i).getColor().getFxColor() == routeColor){
                cardsCount++;
                if (cardsCount>= Objects.requireNonNull(game.getCurrentPlayer().getLatestDestinationCard()).getPoints()){
                  //  System.out.println("Enough cards to start route");
                    gameBoardController.activateDeactivateClaimRouteButton(false,game.getCurrentPlayer().getLatestDestinationCard().getRequiredRoutes().stream().findFirst().get());
                    break;
                }else{
                    System.out.println("Not enough cards to start route");
                }
            }
        }
        gameBoardController.updatePlayerIndicator(nextPlayer);
    }

    public static GameBoardController getGameBoardController() {
        return gameBoardController;
    }

}