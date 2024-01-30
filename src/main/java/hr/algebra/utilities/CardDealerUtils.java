package hr.algebra.utilities;

import hr.algebra.game.helpers.GameHelper;
import hr.algebra.game.helpers.DestinationCardHelper;
import hr.algebra.game.helpers.TrainCardHelper;
import hr.algebra.game.model.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class CardDealerUtils {

    // Method to always draw two train cards
    public static void drawAndSetTrainCard(Player player) {
        for (int i = 0; i < 2; i++) {
            TrainCard drawnCard = TrainCardHelper.drawCard();
            player.drawTrainCard(drawnCard);
            // Set the card on the board without a destination card
            player.setCard(drawnCard, player.getLatestDestinationCard());
        }
    }
    public static void drawAndSetDestinationCard(Player player) {
        DestinationCard drawnDestinationCard = DestinationCardHelper.drawCard();
        player.drawDestinationCard(drawnDestinationCard);
            // Set the card on the board without a destination card
        player.setCard(null, drawnDestinationCard);
        GameHelper.getGameBoardController().updateRouteColorIndicator();

    }

    // Method for the first move of each player
    public static void firstMove(Player player) {
        // Draw a destination card and set it
        drawAndSetDestinationCard(player);

        // Draw and set two train cards
        drawAndSetTrainCard(player);
    }

    // Existing method refactored to use the new methods
    public static void dealCardsToPlayer() {
        Player nextPlayer = GameHelper.getGame().getCurrentPlayer();

        if (nextPlayer.getTrainCardsHand().size() >= Constants.DEFAULT_HANDCARDMAX) {
            handleMaxCardLimit(nextPlayer);
        }
        else if (nextPlayer.getTrainCardsHand().isEmpty()) {
            firstMove(nextPlayer);
        } else {

            drawAndSetTrainCard(nextPlayer);
        }

            System.out.println(nextPlayer);

        switchTurns();
    }
    public static void switchTurns(){
        GameHelper.getGameBoardController().enableSwitchTurnButton();
    }


    private static void handleMaxCardLimit(Player player) {
        // Show a message to the player
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Max Card Limit Reached");
            alert.setHeaderText(null);
            alert.setContentText("You have reached the maximum limit of train cards. Claim a route or discard some cards.");

            alert.showAndWait();
        });
    }
}