package hr.algebra.game.model;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.List;
import hr.algebra.game.view.CityRenderer;
public final class GameBoard {

    private static final double INNER_SQUARE_SIZE = Constants.INNER_SQUARE_SIZE;
    private static final double OUTER_SQUARE_SIZE = Constants.OUTER_SQUARE_SIZE;

    private final Routes routes;
    private final GridPane mainGrid;


    public void setGame(Game game) {
        this.game = game;
        setupGameBoard();
    }

    private Game game;

    private final CityRenderer cityRenderer = new CityRenderer();
    public GameBoard(GridPane mainGrid, Game game) {
        this.mainGrid = mainGrid;
        this.game = game;
        this.routes = new Routes(this);

    }

    public void setupGameBoard() {
        clearBoard();
        setupOuterGrid();
        setupInnerGrid();
        routes.setAllRoutes();

    }

    private void clearBoard() {
        mainGrid.getChildren().clear();
    }

    private void setupOuterGrid() {

        int currentNumber = 80;

        // Top row: left to right, starting from 80, then 1 to 21
        for (int col = 0; col < 21; col++) {
            if (col == 0) {
                // square on the top left is 80
                createNumberedSquare(col, 0, currentNumber);
            } else {
                // after the first square, reset the number to 1
                createNumberedSquare(col, 0, col);
            }
        }

        // Increment for the next number on the right column
        currentNumber = 21;

        // Right column: top to bottom, starting from 22
        for (int row = 1; row < 20; row++) {
            createNumberedSquare(20, row, currentNumber++);
        }

        // Bottom row: right to left, starting from 61
        currentNumber = 40;
        for (int col = 20; col >= 0; col--) {
            createNumberedSquare(col, 20, currentNumber++);
        }

        // Left column: bottom to top, starting from 60
        currentNumber = 61;
        for (int row = 19; row > 0; row--) {
            createNumberedSquare(0, row, currentNumber++);
        }
    }

    private void createNumberedSquare(int col, int row, int number) {
        Rectangle square = createBasicSquare(OUTER_SQUARE_SIZE);
        Text text = createCenteredText(String.valueOf(number));

        // StackPane to center the text
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(square, text);


        stackPane.setMaxSize(OUTER_SQUARE_SIZE, OUTER_SQUARE_SIZE);

        //  StackPane to the GridPane
        GridPane.setConstraints(stackPane, col, row);
        mainGrid.getChildren().add(stackPane);
    }
    private Text createCenteredText(String content) {
        Text text = new Text(content);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(Font.font(OUTER_SQUARE_SIZE / 2));


        return text;
    }


    private void setupInnerGrid() {
        for (int col = 1; col < 20; col++) {
            for (int row = 1; row < 20; row++) {
                createSquare(col, row);
            }
        }


        List<City> cities = Game.getCities();
        for (City city : cities) {
            placeCity(city.col(), city.row());
        }
    }

    private void createSquare(int col, int row) {
        Rectangle square = createBasicSquare(GameBoard.INNER_SQUARE_SIZE);
        GridPane.setConstraints(square, col, row);
        mainGrid.getChildren().add(square);
    }

    private Rectangle createBasicSquare(double size) {
        Rectangle square = new Rectangle(size, size);
        square.setFill(Color.WHITE);
        square.setStroke(Color.BLACK);
        return square;
    }

    private void placeCity(int col, int row) {
        // Remove the squares
        mainGrid.getChildren().removeIf(child ->
                GridPane.getColumnIndex(child) == col &&
                        GridPane.getRowIndex(child) == row &&
                        child instanceof Rectangle
        );


        Circle cityCircle = cityRenderer.renderCity(GameBoard.INNER_SQUARE_SIZE);
        StackPane stackPane = new StackPane(cityCircle);
        stackPane.setMaxSize(GameBoard.INNER_SQUARE_SIZE, GameBoard.INNER_SQUARE_SIZE);


        mainGrid.add(stackPane, col, row);


    }

    public void highlightSquare(int x, int y, Color color) {

        Rectangle square = (Rectangle) getNodeFromGridPane(mainGrid, x, y);
        if (square != null) {
            square.setFill(color);
        }
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }



        return null;
    }





}