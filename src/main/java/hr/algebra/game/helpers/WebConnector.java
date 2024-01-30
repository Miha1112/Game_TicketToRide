package hr.algebra.game.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.game.controller.GameBoardController;
import hr.algebra.game.model.*;
import javafx.scene.paint.Color;
import org.springframework.http.HttpStatus;

import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static hr.algebra.game.helpers.GameHelper.game;
//-----------------------------Communications with server-------------------------------
public class WebConnector {
    public static void startSendHttpRequest(GameBoardController controller) {
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
//            System.out.println("Status Code: " + statusCode);
//            System.out.println("Response Body: " + responseBody);
            processJson(responseBody,controller);
        });
    }
    public static void getMessages(GameBoardController controller) {
        ChatMessage chatMessage = new ChatMessage(Game.getPlayerColor(Game.getGameColor()), "");
        String serverUrl = "http://localhost:8080/game/get-message";
        System.out.println("Chat message send: " + chatMessage.getColor());
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(chatMessage.toJson()))
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture =
                httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
        responseFuture.thenAccept(response -> {
            int statusCode = response.statusCode();
            String responseBody = response.body();
            System.out.println("Status Code: " + statusCode);
            System.out.println("Response Body: " + responseBody);
            parseMessageJson(responseBody,controller);
        });
    }

    public static void makeMove(GameBoardController controller) {
        ChatMessage chatMessage = new ChatMessage(Game.getPlayerColor(Game.getGameColor()), "");
        String serverUrl = "http://localhost:8080/game/change-turn";
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(chatMessage.toJson()))
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture =
                httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
        responseFuture.thenAccept(response -> {
            controller.disEnableSwitchTurnButton();
            WebUpdate.canMakeMove(controller);
        });
    }
    public static void checkedMove(GameBoardController controller) {
       // System.out.println("Start try spam checker");
        ChatMessage chatMessage = new ChatMessage(Game.getPlayerColor(Game.getGameColor()), "");
        String serverUrl = "http://localhost:8080/game/make-move";
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(chatMessage.toJson()))
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture =
                httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
        responseFuture.thenAccept(response -> {
            //System.out.println("Spamer get answer");
            String responseBody = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            ChatMessage responseBodyMessage;
            try {
                responseBodyMessage = objectMapper.readValue(responseBody, ChatMessage.class);
                //System.out.println("Spammer get response body: " + responseBody + " spammer response body message: " + responseBodyMessage);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            if (responseBodyMessage.getContent().equals("ACCEPTED")) {
                WebUpdate.timelineMove.stop();
                controller.enableSwitchTurnButton();
                System.out.println("Spammer off for: " + Game.getGameColor());
            } else if (responseBodyMessage.getContent().equals("NOT_ACCEPTABLE")) {
                controller.disEnableSwitchTurnButton();
                WebUpdate.isExistRoute(controller);
                System.out.println("Spammer still work: " + Game.getGameColor());
            }
        });
    }
    public static void isNewRouteExist(GameBoardController controller) {
        // System.out.println("Start try spam checker");
        ChatMessage chatMessage = new ChatMessage(Game.getPlayerColor(Game.getGameColor()), "");
        String serverUrl = "http://localhost:8080/game/is-route-get";
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(chatMessage.toJson()))
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture =
                httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
        responseFuture.thenAccept(response -> {
            //System.out.println("Spamer get answer");
            String responseBody = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            ChatMessage responseBodyMessage;
            try {
                responseBodyMessage = objectMapper.readValue(responseBody, ChatMessage.class);
                //System.out.println("Spammer get response body: " + responseBody + " spammer response body message: " + responseBodyMessage);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            if (responseBodyMessage.getContent()!=null){
                List<ChatMessage> messages = new ArrayList<>();
                messages.add(new ChatMessage(responseBodyMessage.getColor(),"get route: "  + responseBodyMessage.getContent()));
                controller.updateMessage(messages);
                Route route = Route.valueOf(responseBodyMessage.getContent());
                Color color = PlayerColor.valueOf(String.valueOf(responseBodyMessage.getColor())).getFxColor();
                controller.getGameBoard().setToRoutePlayerColor(color,route);
                isWinnerExist(controller);
            }
        });
    }
    public static void isWinnerExist(GameBoardController controller) {
        // System.out.println("Start try spam checker");
        ChatMessage chatMessage = new ChatMessage(Game.getPlayerColor(Game.getGameColor()), "");
        String serverUrl = "http://localhost:8080/game/is-route-get";
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl))
                .GET()
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture =
                httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
        responseFuture.thenAccept(response -> {
            //System.out.println("Spamer get answer");
            String responseBody = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            ChatMessage responseBodyMessage;
            try {
                responseBodyMessage = objectMapper.readValue(responseBody, ChatMessage.class);
                //System.out.println("Spammer get response body: " + responseBody + " spammer response body message: " + responseBodyMessage);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            if (responseBodyMessage.getContent()!=null){
                List<ChatMessage> messages = new ArrayList<>();
                messages.add(new ChatMessage(responseBodyMessage.getColor()," IS WINNER \n score "  + responseBodyMessage.getContent()));
                controller.updateMessage(messages);
            }
        });
    }



    public static void isGameReady(GameBoardController controller) {
            ChatMessage chatMessage = new ChatMessage(Game.getPlayerColor(Game.getGameColor()), "");
            String serverUrl = "http://localhost:8080/game/is-ready";
            System.out.println("Chat message send: " + chatMessage.getColor());
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl))
                    //.header("Content-Type", "application/json")
                    .GET()
                    .build();
            CompletableFuture<HttpResponse<String>> responseFuture =
                    httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
            responseFuture.thenAccept(response -> {
                int statusCode = response.statusCode();
                String responseBody = response.body();
                System.out.println("Status Code: " + statusCode);
                System.out.println("Response Body: " + responseBody);
                ObjectMapper objectMapper = new ObjectMapper();
                ChatMessage responseBodyMessage;
                try {
                    responseBodyMessage = objectMapper.readValue(responseBody, ChatMessage.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                PlayerColor playerColor = PlayerColor.valueOf(String.valueOf(responseBodyMessage.getColor()));
                System.out.println("Player color move: " + playerColor);
                if (responseBodyMessage.getContent().equals("ACCEPTED")) {
                    controller.gameReady = true;
                    WebUpdate.timelineStart.stop();
                    WebUpdate.startChatMessagesRefreshThread(controller);
                    if (playerColor == Game.getPlayerColor(Game.getGameColor())){
                        controller.enableSwitchTurnButton();
                    }else {
                        controller.disEnableSwitchTurnButton();
                        WebUpdate.canMakeMove(controller);
                    }
                } else if (responseBodyMessage.getContent().equals("NOT_ACCEPTABLE")) {
                    controller.gameReady = false;
                }
            });
    }

    private static void parseMessageJson(String json, GameBoardController controller){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            if (json.startsWith("[") && json.endsWith("]")) {
                List<ChatMessage> messageList = objectMapper.readValue(json, new TypeReference<List<ChatMessage>>() {});
                if (!messageList.isEmpty()) {
                    controller.updateMessage(messageList);
                }
                System.out.println(messageList);
            } else {
                //System.out.println("Invalid JSON format for a list of ChatMessages.");
            }
        } catch (Exception e) {
            //e.printStackTrace();
//            System.out.println("No new messages");
        }
    }

    private static void processJson(String json, GameBoardController controller){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            ChatMessage chatMessage = objectMapper.readValue(json,ChatMessage.class);
            Player player = game.getCurrentPlayer();
            player.setColor(PlayerColor.valueOf(String.valueOf(chatMessage.getColor())));
            Game.setGameColor(PlayerColor.valueOf(String.valueOf(chatMessage.getColor())).getFxColor());
            controller.updatePlayerIndicator(player);
            System.out.println(" player color set: " + player.getColor());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in decoding json from server");
        }
    }
    public static void appClose(){
        String serverUrl = "http://localhost:8080/game/remove";
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture =
                httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
    }


    public static void claimRouteHttp(Route currentRoute){
        try {
            String serverUrl = "http://localhost:8080/game/choose-route";
            ChatMessage chatMessage = new ChatMessage(Game.getPlayerColor(Game.getGameColor()), currentRoute.name());
           // System.out.println("Send information: " + Game.getGameRoomId() + "  " + Game.getGameId() + "  " + currentRoute.name());
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(chatMessage.toJson()))
                    .build();

            CompletableFuture<HttpResponse<String>> responseFuture =
                    httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());

            responseFuture.thenAccept(response -> {
                int statusCode = response.statusCode();
                String responseBody = response.body();
//                System.out.println("Status Code: " + statusCode);
//                System.out.println("Response Body: " + responseBody);
            }).exceptionally(throwable -> {
                // Обробка виняткових ситуацій
                throwable.printStackTrace();
                return null;
            });
        }catch (Exception e){
            System.out.println("Can`t connect to server");
        }
    }
    public static void sendMessage(ChatMessage chatMessage){
        try {
            Player currentPlayer = GameHelper.getGame().getCurrentPlayer();
            String serverUrl = "http://localhost:8080/game/send-message";
            System.out.println("Send message color: " + chatMessage.getColor());
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(chatMessage.toJson()))
                    .build();

            CompletableFuture<HttpResponse<String>> responseFuture =
                    httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());

            responseFuture.thenAccept(response -> {
                int statusCode = response.statusCode();
                String responseBody = response.body();
//                System.out.println("Status Code: " + statusCode);
//                System.out.println("Response Body: " + responseBody);
            }).exceptionally(throwable -> {
                // Обробка виняткових ситуацій
                throwable.printStackTrace();
                return null;
            });
        }catch (Exception e){
            System.out.println("Can`t connect to server");
        }

    }

}
