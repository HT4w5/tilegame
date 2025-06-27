package edu.tilegame.tengine;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Tile {
    private final char character;
    private final TextColor fColor;
    private final TextColor bColor;

    public Tile(char c, TextColor fColor, TextColor bColor) {
        character = c;
        this.fColor = fColor;
        this.bColor = bColor;
    }

    public void draw(Terminal term) throws IOException {
        term.setBackgroundColor(bColor);
        term.setForegroundColor(fColor);
        term.putCharacter(character);
    }
}
