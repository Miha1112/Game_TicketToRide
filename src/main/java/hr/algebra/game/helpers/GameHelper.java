package hr.algebra.game.helpers;

import hr.algebra.game.controller.GameBoardController;
import hr.algebra.game.model.Game;
import hr.algebra.game.model.Player;
import hr.algebra.game.model.PlayerColor;
import hr.algebra.game.model.TrainCard;
import hr.algebra.utilities.CardDealerUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameHelper {

    public static Game getGame() {
        return game;
    }

    private static Game game;
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
        gameBoardController.updatePlayerIndicator(currentPlayer);
    }

    private static void setupPlayers() {
        GameBoardController gameBoardController = GameHelper.getGameBoardController();
        game.removePlayers();
        game.addPlayer(new Player(availableColors.get(0), gameBoardController));
        game.addPlayer(new Player(availableColors.get(1), gameBoardController));
    }


    public static void switchTurns() {
        Player nextPlayer = game.switchToNextPlayer();
        CardDealerUtils.dealCardsToPlayer();
        gameBoardController.updatePlayerIndicator(nextPlayer);
    }

    public static GameBoardController getGameBoardController() {
        return gameBoardController;
    }

}