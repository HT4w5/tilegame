package edu.tilegame.tengine;

import java.io.IOException;
import java.io.InputStream;
import java.awt.Font;
import java.awt.FontFormatException;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

import edu.tilegame.exceptions.ScreenTooSmallException;
import edu.tilegame.Properties;

public class TileRenderer {
    private int width;
    private int height;
    private Terminal term;

    public TileRenderer(int w, int h, Terminal term) {
        width = w;
        height = h;
        this.term = term;
    }

    public void renderFrame(Tile[][] stage) {
        int xLen = stage.length;
        int yLen = stage[0].length;

        // Draw every character to terminal.
        try {
            term.setCursorPosition(0, 0);
            for (int y = 0; y < yLen; ++y) {
                term.setCursorPosition(0, y);
                for (int x = 0; x < xLen; ++x) {
                    stage[x][y].draw(term);
                }

            }
            term.flush();
        } catch (IOException e) {
            System.err.println("Failed to render frame");
        }
    }
}
