package hr.algebra.game.helpers;

import hr.algebra.game.controller.GameBoardController;
import hr.algebra.game.model.Player;
import hr.algebra.game.model.PlayerColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PlayerHelper {

    private PlayerHelper() {

    }

    public static final List<PlayerColor> availableColors = new ArrayList<>(Arrays.asList(PlayerColor.values()));
    private static final Random random = new Random();
    public static Player createPlayer(GameBoardController gameBoardController) {
        // Assign a random color to the player and remove it from the available list
        PlayerColor color = assignRandomColor();
        return new Player(color); // Assuming Player constructor accepts a PlayerColor
    }
    private static PlayerColor assignRandomColor() {
        if (availableColors.isEmpty()) {
            throw new IllegalStateException("No more colors available.");
        }
        // Get a random index and remove the color from the available list
        int randomIndex = random.nextInt(availableColors.size());
        return availableColors.remove(randomIndex); // Remove and return the color
    }


    // Resets the list of available colors for a new game
    public void resetColors() {
        availableColors.clear();
        availableColors.addAll(Arrays.asList(PlayerColor.values()));
    }
}
