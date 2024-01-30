package hr.algebra.game.model;

import com.google.gson.Gson;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatMessage {
    private PlayerColor color;
    private String content;
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}