package hr.algebra.game.configuration;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class WebSocketClient {
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket connection opened: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message) {
        // Обробка отриманого повідомлення
        System.out.println("Received message: " + message);
        // Оновлення JavaFX інтерфейсу на основі отриманих даних
        // ...
    }
}
