package edu.tilegame.tengine;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.Objects;

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

    /* Make hashable for use with HashSet */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Tile))
            return false;
        Tile tile = (Tile) o;
        return character == tile.character &&
                Objects.equals(fColor, tile.fColor) &&
                Objects.equals(bColor, tile.bColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, fColor, bColor);
    }
}
