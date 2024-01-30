package hr.algebra.game.controller;

import hr.algebra.game.helpers.GameHelper;
import hr.algebra.game.model.*;
import hr.algebra.game.view.TrainCardsImageRenderer;
import hr.algebra.utilities.CardDealerUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;


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
    private Route currentRoute = null;
    @Setter
    @Getter
    private Player player;
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
            gameBoard = new GameBoard(mainGrid,this, game);
            gameBoard.setupGameBoard();
        }

    }

    public void updateGame(Game game) {
        gameBoard.setGame(game);
    }


    public void updatePlayerIndicator(Player player) {
        playerIndicator.setFill(Game.getGameColor());
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
        try {
            Player currentPlayer = GameHelper.getGame().getCurrentPlayer();
            String serverUrl = "http://localhost:8080/game/choose-route";

            // Переконайтеся, що об'єкт ChatMessage реалізований правильно і очікує параметри в правильному порядку
            ChatMessage chatMessage = new ChatMessage(Game.getGameRoomId(), Game.getGameId(), currentRoute.name());
            System.out.println("Send information: " + Game.getGameRoomId() + "  " + Game.getGameId() + "  " + currentRoute.name());
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl))
                    .header("Content-Type", "application/json") // Додайте необхідний заголовок
                    .POST(HttpRequest.BodyPublishers.ofString(chatMessage.toJson())) // Передача даних у вигляді строки
                    .build();

            CompletableFuture<HttpResponse<String>> responseFuture =
                    httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());

            responseFuture.thenAccept(response -> {
                int statusCode = response.statusCode();
                String responseBody = response.body();
                System.out.println("Status Code: " + statusCode);
                System.out.println("Response Body: " + responseBody);
            }).exceptionally(throwable -> {
                // Обробка виняткових ситуацій
                throwable.printStackTrace();
                return null;
            });
        }catch (Exception e){
            System.out.println("Can`t connect to server");
        }
        gameBoard.setToRoutePlayerColor(Game.getGameColor(),currentRoute);
        updatePlayerCard(currentRoute);
        activateDeactivateClaimRouteButton(true,null);
    }
    public void updatePlayerCard(Route route){
        player.getNextDestinationCard();
        player.resetPlayerCards(player,this);
    }


    public void updateClaimRouteButton(boolean canClaim) {
        btnClaimRoute.setDisable(!canClaim);
    }

    public void serializeGame(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save GameBoard Data");

        // Задайте фільтр розширення файлу, якщо необхідно
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Показати діалогове вікно вибору файлу
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            Task<Void> exportTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    //XMLExporter.exportToXML(gameBoard, file.getAbsolutePath());
                    gameBoard.exportToXML(file.getAbsolutePath());
                    return null;
                }
            };

            exportTask.setOnSucceeded(event -> {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Save data");
                    alert.setHeaderText(null);
                    alert.setContentText("Save date done");
                    alert.showAndWait();
                });
            });

            exportTask.setOnFailed(event -> {
                // Обробка помилок, якщо необхідно
                exportTask.getException().printStackTrace();
            });

            // Запустити завдання у окремому потоці
            Thread exportThread = new Thread(exportTask);
            exportThread.setDaemon(true); // Якщо головний потік завершиться, цей теж завершиться
            exportThread.start();
        }
    }


    public void deserializeGame(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(null);
        gameBoard.importFromXML(selectedFile.getAbsolutePath());
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Load data");
            alert.setHeaderText(null);
            alert.setContentText("Load date done");
            alert.showAndWait();
        });

    }

    public void activateDeactivateClaimRouteButton(Boolean status, Route route){
        btnClaimRoute.setDisable(status);
        currentRoute = route;
    }

    public void sendMessage(ActionEvent actionEvent) {

    }

    public void closeGame(ActionEvent actionEvent) {
        Platform.exit();
    }
}
