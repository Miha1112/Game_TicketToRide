package hr.algebra.game.controller;

import hr.algebra.game.helpers.GameHelper;
import hr.algebra.game.model.*;
import hr.algebra.game.view.TrainCardsImageRenderer;
import hr.algebra.utilities.CardDealerUtils;
import hr.algebra.utilities.SerializationUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

public class GameBoardController {
    @FXML
    public GridPane trainCardsImageView;
    public ImageView destinationCard;
    public Button btnClaimRoute;
    @FXML
    public Circle routeColorIndicator;

    @FXML
    private Button switchTurnButton;

    @FXML private Circle playerIndicator;
    public BorderPane borderPane;
    @FXML
    private GridPane mainGrid;
    private GameBoard gameBoard;
    private TrainCardsImageRenderer imageRenderer;
    private DestinationCard destinationCardInstance;
    @FXML
    public void initialize() {

        // Initialize the destinationCardInstance
        // Replace Collections.emptySet() with actual routes as per your game logic
      destinationCardInstance = new DestinationCard(Collections.emptySet());

        // Get the image path from the DestinationCard instance
        updateDestinationCardOnBoard(destinationCardInstance);

       destinationCard.setPreserveRatio(true);

        // Initialize and set up the image renderer
        imageRenderer = new TrainCardsImageRenderer(trainCardsImageView, Constants.DEFAULT_GRID_ROWS, Constants.DEFAULT_GRID_COLS, 100, 100);



        GameHelper.init(this);
        switchTurnButton.setPrefWidth(200);
        btnClaimRoute.setPrefWidth(200);
        btnClaimRoute.setDisable(true);
        switchTurnButton.setDisable(true);
    }

    public void startGame(ActionEvent actionEvent) {

        GameHelper.startGame();
        CardDealerUtils.dealCardsToPlayer();
    }


    public void initGame(Game game) {
        if (gameBoard == null) {
            gameBoard = new GameBoard(mainGrid, game);
            gameBoard.setupGameBoard();

        }

    }

    public void updateGame(Game game) {
        gameBoard.setGame(game);
    }


    public void updatePlayerIndicator(Player player) {
        Color fxColor = player.getColor().getFxColor();
        playerIndicator.setFill(fxColor);
        playerIndicator.setVisible(true);
    }


    public void updateCardOnBoard(TrainCard card, int row, int col) {
        imageRenderer.displayCard(card, row, col);
    }

    public void updateDestinationCardOnBoard(DestinationCard destinationCard) {
        // Get the image path from the DestinationCard instance
        String destinationImagePath = destinationCard.getDestinationImagePath();

        // Load the destination card image
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(destinationImagePath)));

        // Set the image to the destination card
        this.destinationCard.setImage(image);

        // Adjust the sizing and ratio
        if (image != null && image.getWidth() > 0 && image.getHeight() > 0) {
            this.destinationCard.setFitWidth(image.getWidth() / 2);
            this.destinationCard.setFitHeight(image.getHeight() / 2);
            this.destinationCard.setPreserveRatio(true);
        }
    }

    public void updateRouteColorIndicator() {
        Player currentPlayer = GameHelper.getGame().getCurrentPlayer();
        if (currentPlayer != null) {
            RouteColor latestRouteColor = currentPlayer.getLatestCardDefaultRouteColor();
            if (latestRouteColor != null) {
                Platform.runLater(() -> {
                    routeColorIndicator.setFill(Color.valueOf(latestRouteColor.name()));
                    routeColorIndicator.setVisible(true);
                });
            } else {
                // Handle the case where latest route color is null
                Platform.runLater(() -> routeColorIndicator.setVisible(false));
            }
        }
    }

    // Rest of your controller code...



    @FXML
    private void handleSwitchTurn(ActionEvent event) {

        GameHelper.switchTurns();
        updateRouteColorIndicator();
        // Any additional logic you want to execute when the turn switches
    }

    public void enableSwitchTurnButton() {
        Platform.runLater(() -> switchTurnButton.setDisable(false));
    }

        @FXML
    public void handleClaimRoute(ActionEvent actionEvent) {
        Player currentPlayer = GameHelper.getGame().getCurrentPlayer();
    }

    public void updateClaimRouteButton(boolean canClaim) {
        btnClaimRoute.setDisable(!canClaim);
    }

    public void serializeGame(ActionEvent actionEvent) {

        System.out.println(GameHelper.getGame());
        try {
            SerializationUtils.write(GameHelper.getGame(),"game.ser");

        } catch (IOException e) {
           e.printStackTrace();

        }
    }

    public void deserializeGame(ActionEvent actionEvent) {

        try {

            Game game=SerializationUtils.read("game.ser");
            updateGame(game);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

        }
    }



}
