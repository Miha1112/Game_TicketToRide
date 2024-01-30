package hr.algebra.game.helpers;

import hr.algebra.game.controller.GameBoardController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.rmi.RemoteException;
import java.util.List;

public class WebUpdate {
    public static Timeline timelineStart;
    public static Timeline timelineMove;
    public static Timeline timelineWinner;
    public static void startChatMessagesRefreshThread(GameBoardController controller) {
       final Timeline timelineMessage = new Timeline(
                new KeyFrame(
                        Duration.millis(2000),
                        event -> {
                            WebConnector.getMessages(controller);
                        }
                )
        );
        timelineMessage.setCycleCount(Animation.INDEFINITE);
        timelineMessage.play();
    }
    public static void startGameChecker(GameBoardController controller) {
        timelineStart = new Timeline(
                new KeyFrame(
                        Duration.millis(2000),
                        event -> {
                            WebConnector.isGameReady(controller);
                        }
                )
        );
        timelineStart.setCycleCount(Animation.INDEFINITE);
        timelineStart.play();
    }
    public static void canMakeMove(GameBoardController controller) {

        timelineMove = new Timeline(
                new KeyFrame(
                        Duration.millis(1000),
                        event -> {
                            WebConnector.checkedMove(controller);
                        }
                )
        );
        if (timelineMove.getStatus() != Animation.Status.RUNNING) {
            timelineMove.setCycleCount(Animation.INDEFINITE);
            timelineMove.play();
        }
    }
    public static void isExistRoute(GameBoardController controller) {

        timelineWinner = new Timeline(
                new KeyFrame(
                        Duration.millis(2000),
                        event -> {
                            WebConnector.isNewRouteExist(controller);
                        }
                )
        );
        if (timelineWinner.getStatus() != Animation.Status.RUNNING) {
            timelineWinner.setCycleCount(Animation.INDEFINITE);
            timelineWinner.play();
        }
    }


}
