package hr.algebra.game;

import hr.algebra.game.controller.GameBoardController;
import hr.algebra.game.helpers.GameHelper;
import hr.algebra.game.helpers.WebConnector;
import hr.algebra.game.helpers.WebUpdate;
import hr.algebra.game.model.Game;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameApplication extends Application {

    private static final String GAME_TITLE = "Ticket to Ride Game";
    private static final String FXML_PATH = "view/game.fxml";
    private  GameBoardController controller;


    @Override
    public void start(Stage stage) throws Exception {
        Game game = new Game();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_PATH));
        Scene scene = new Scene(fxmlLoader.load(),1100,720);



        controller = fxmlLoader.getController();

        controller.initGame(game);
        WebUpdate.startGameChecker(controller);
        GameHelper.init(controller);
        WebConnector.startSendHttpRequest(controller);

        stage.setTitle(GAME_TITLE);
        stage.setScene(scene);
       //stage.setFullScreen(true);
        stage.setOnCloseRequest(this::appClose);
        stage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }



    private void appClose(javafx.stage.WindowEvent event){
        WebConnector.appClose();
    }

}
