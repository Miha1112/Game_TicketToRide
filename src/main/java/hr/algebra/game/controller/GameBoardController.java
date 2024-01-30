package hr.algebra.game.controller;

import hr.algebra.game.helpers.GameHelper;
import hr.algebra.game.helpers.WebConnector;
import hr.algebra.game.helpers.WebUpdate;
import hr.algebra.game.model.*;
import hr.algebra.game.view.TrainCardsImageRenderer;
import hr.algebra.utilities.CardDealerUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class GameBoardController {
    @FXML
    private ListView<ChatMessageType> chatListView;

    @FXML
    private TextArea messageInputTextArea;

    private ObservableList<ChatMessageType> chatMessages = FXCollections.observableArrayList();
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
    @Getter
    private GameBoard gameBoard;
    private TrainCardsImageRenderer imageRenderer;
    private DestinationCard destinationCardInstance;
    private Route currentRoute = null;
    @Setter
    @Getter
    private Player player;

    public boolean gameReady;
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
        chatListView.setItems(chatMessages);


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



    @FXML
    private void handleSwitchTurn(ActionEvent event) {
        GameHelper.switchTurns();
        updateRouteColorIndicator();
        WebConnector.makeMove(this);
        // Any additional logic you want to execute when the turn switches
    }

    public void enableSwitchTurnButton() {
        if (switchTurnButton.isDisable()) {
            System.out.println("Button enable to: " + Game.getGameColor());
            Platform.runLater(() -> switchTurnButton.setDisable(false));
        }
    }
    public void disEnableSwitchTurnButton(){
        if (!switchTurnButton.isDisable()) {
            System.out.println("Button dis enable to: " + Game.getGameColor());
            Platform.runLater(() -> switchTurnButton.setDisable(true));
        }
    }

    @FXML
    public void handleClaimRoute(ActionEvent actionEvent) {
        WebConnector.claimRouteHttp(currentRoute);
        gameBoard.setToRoutePlayerColor(Game.getGameColor(),currentRoute);
        updatePlayerCard(currentRoute);
        activateDeactivateClaimRouteButton(true,null);
    }
    public void updatePlayerCard(Route route){
        player.getNextDestinationCard();
        resetPlayerCards(player);
    }
    public void resetPlayerCards(Player player) {
        player.getTrainCards().clear();
        for (int i = 0; i < player.getTrainCardGrid().length; i++) {
            for (int j = 0; j < player.getTrainCardGrid()[i].length; j++) {
                player.getTrainCardGrid()[i][j] = new TrainCard(RouteColor.EMPTY);
                updateCardOnBoard(new TrainCard(RouteColor.EMPTY), i, j);
            }
        }
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
        String playerMessage = messageInputTextArea.getText();
        if (!playerMessage.isEmpty()) {
            PlayerColor color = Game.getPlayerColor(Game.getGameColor());
            ChatMessage chatMessage = new ChatMessage(color, playerMessage);
            WebConnector.sendMessage(chatMessage);
            ChatMessageType messageType = new ChatMessageType(chatMessage.getColor().toString(),playerMessage);
            chatMessages.add(messageType);
            chatListView.setCellFactory(new Callback<ListView<ChatMessageType>, ListCell<ChatMessageType>>() {
                @Override
                public ListCell<ChatMessageType> call(ListView<ChatMessageType> param) {
                    return new ListCell<ChatMessageType>() {
                        @Override
                        protected void updateItem(ChatMessageType item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item == null || empty) {
                                setText(null);
                            } else {
                                setText(item.getSender() + ": " + item.getMessage());
                            }
                        }
                    };
                }
            });
            messageInputTextArea.clear();
        }
    }
    public void updateMessage(List<ChatMessage> list){
        ChatMessage chatMessage = list.getLast();
        ChatMessageType messageType = new ChatMessageType(chatMessage.getColor().toString(),chatMessage.getContent());
        System.out.println("Message get: " + messageType);
        chatMessages.add(messageType);
        chatListView.setCellFactory(new Callback<ListView<ChatMessageType>, ListCell<ChatMessageType>>() {
            @Override
            public ListCell<ChatMessageType> call(ListView<ChatMessageType> param) {
                return new ListCell<ChatMessageType>() {
                    @Override
                    protected void updateItem(ChatMessageType item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getSender() + ": " + item.getMessage());
                        }
                    }
                };
            }
        });
    }


    public void setCard(TrainCard card,DestinationCard destinationCard, Player player) {
        TrainCard[][] trainCardGrid = player.getTrainCardGrid();
        int[] nextPosition = findNextAvailablePosition(player);
        if (nextPosition[0] != -1) { // Check if a valid position was found
            int row = nextPosition[0];
            int col = nextPosition[1];

            if (card != null) {
               trainCardGrid[row][col] = card;
                updateCardOnBoard(card, row, col);
            }
            if (destinationCard != null ) {
                updateDestinationCardOnBoard(destinationCard);
            }
        }
    }
    private int[] findNextAvailablePosition(Player player) {
        TrainCard[][] trainCards = player.getTrainCardGrid();
        for (int row = 0; row < trainCards.length; row++) {
            for (int col = 0; col < trainCards[row].length; col++) {
                if (trainCards[row][col].getColor()==RouteColor.EMPTY) {
                    return new int[]{row, col};
                }
            }
        }
        return new int[]{-1, -1}; // Return an invalid position if the grid is full
    }

    public void closeGame(ActionEvent actionEvent) {
        Platform.exit();
    }
}
