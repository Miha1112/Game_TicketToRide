package hr.algebra.game.model;

import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
public class WebSocketClientEndpoint {

    private Session userSession = null;

    public WebSocketClientEndpoint(URI endpointURI) {
        try {
            WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
            webSocketContainer.connectToServer(this,endpointURI);
//            ClientManager client = ClientManager.createClient();
//            client.connectToServer(this, endpointURI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        this.userSession = userSession;
        System.out.println("Connection opened");
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        this.userSession = null;
        System.out.println("Connection closed: " + reason.getReasonPhrase());
    }

    @OnMessage
    public void onMessage(String message) {
        // Обробка отриманих повідомлень від сервера
        System.out.println("Received message: " + message);
    }

    public void sendMessage(String message) {
        try {
            this.userSession.getAsyncRemote().sendText(message);
        }catch (Exception e){
            throw new IllegalStateException("Error in connection to user session: " + userSession);
        }
    }
}