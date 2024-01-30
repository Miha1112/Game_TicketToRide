package hr.algebra.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.game.controller.GameBoardController;
import hr.algebra.game.helpers.GameHelper;
import hr.algebra.game.model.ChatMessage;
import hr.algebra.game.model.Game;
import hr.algebra.game.model.Player;
import hr.algebra.game.model.PlayerColor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static hr.algebra.game.helpers.GameHelper.game;

public class GameApplication extends Application {

    private static final String GAME_TITLE = "Ticket to Ride Game";
    private static final String FXML_PATH = "view/game.fxml";
    private  GameBoardController controller;

    private final TextArea chatArea = new TextArea();
    private final TextField messageInput = new TextField();


    @Override
    public void start(Stage stage) throws Exception {
        Game game = new Game();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_PATH));
        Scene scene = new Scene(fxmlLoader.load(),1100,720);

        sendHttpRequest();

        controller = fxmlLoader.getController();

        controller.initGame(game);


        GameHelper.init(controller);


        stage.setTitle(GAME_TITLE);
        stage.setScene(scene);
       // stage.setFullScreen(true);
        stage.setOnCloseRequest(this::appClose);
        stage.show();
       // WebSocketClientEndpoint clientEndpoint = new WebSocketClientEndpoint(URI.create("http://localhost:8080/message/1"));
        //clientEndpoint.sendMessage("Hello, WebSocket Server!");
        //GameHelper.startGame();
    }



    public static void main(String[] args) {
        launch(args);
    }

    private void sendHttpRequest() {
        String serverUrl = "http://localhost:8080/game/start";
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl))
                .GET()
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture =
                httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
        responseFuture.thenAccept(response -> {
            int statusCode = response.statusCode();
            String responseBody = response.body();
            System.out.println("Status Code: " + statusCode);
            System.out.println("Response Body: " + responseBody);
            processJson(responseBody);
        });
    }

    private void processJson(String json){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            ChatMessage chatMessage = objectMapper.readValue(json,ChatMessage.class);
            Player player = game.getCurrentPlayer();
            Game.setGameId(chatMessage.getUserId());
            Game.setGameRoomId(chatMessage.getRoomId());
            String color = chatMessage.getContent();
            switch (color){
                case "Blue":
                    player.setColor(PlayerColor.BLUE);
                    Game.setGameColor(PlayerColor.BLUE.getFxColor());
                break;
                case "Brown":
                    player.setColor(PlayerColor.BROWN);
                    Game.setGameColor(PlayerColor.BROWN.getFxColor());
                    break;
            }
            controller.updatePlayerIndicator(player);
            System.out.println("Set player info: " + player.getId() + " player room: " + player.getRoomId() + " player color set: " + player.getColor());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in decoding json from server");
        }
    }

    private void appClose(javafx.stage.WindowEvent event){
        String serverUrl = "http://localhost:8080/game/remove/" + game.getCurrentPlayer().getRoomId();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture =
                httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

}
