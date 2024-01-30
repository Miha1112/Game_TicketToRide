package hr.algebra.game.model;

import javafx.scene.control.ListCell;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatMessageType extends ListCell<ChatMessage> {
    private String sender;
    private String message;
    @Override
    public String toString() {
        return sender + ": " + message;
    }
}
