package hr.algebra.game.view;


import hr.algebra.game.model.TrainCard;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;


public class TrainCardsImageRenderer {

    private final GridPane trainCardsGrid;
    private final int numRows;
    private final int numCols;
    private final double cardWidth;
    private final double cardHeight;
    private boolean[][] gridState;

    public TrainCardsImageRenderer(GridPane trainCardsGrid, int numRows, int numCols, double cardWidth, double cardHeight) {
        this.trainCardsGrid = trainCardsGrid;
        this.numRows = numRows;
        this.numCols = numCols;
        this.cardWidth = cardWidth;
        this.cardHeight = cardHeight;
        this.gridState = new boolean[numRows][numCols];
        setupTrainCardsGrid();
    }

    private void setupTrainCardsGrid() {
        trainCardsGrid.getRowConstraints().clear();
        trainCardsGrid.getColumnConstraints().clear();

        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(cardHeight);
            trainCardsGrid.getRowConstraints().add(rowConstraints);
        }

        for (int j = 0; j < numCols; j++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPrefWidth(cardWidth);
            trainCardsGrid.getColumnConstraints().add(colConstraints);
        }


        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                gridState[row][col] = false;

                // Add a default card to each cell
                TrainCard defaultCard = new TrainCard(null);
                displayCard(defaultCard, row, col);
            }
        }
    }

    public void displayCard(TrainCard card, int row, int col) {

        if (card == null) {
            trainCardsGrid.getChildren().removeIf(node ->
                    GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col);
            gridState[row][col] = false;
            return;
        }


        String imagePath = card.getImagePath();
        Image image = new Image(imagePath);
        ImageView cardImageView = new ImageView(image);


        cardImageView.setFitWidth(cardWidth);
        cardImageView.setFitHeight(cardHeight);


        trainCardsGrid.add(cardImageView, col, row);

        gridState[row][col] = true;
    }
}

