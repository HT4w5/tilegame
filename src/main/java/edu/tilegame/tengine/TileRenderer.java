package edu.tilegame.tengine;

import java.io.IOException;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import edu.tilegame.exceptions.ScreenTooSmallException;
import edu.tilegame.Properties;

public class TileRenderer {
    private int width;
    private int height;
    private Terminal term;

    public TileRenderer(int w, int h) {
        width = w;
        height = h;

        // Initialize a screen with emulated terminal.
        DefaultTerminalFactory dtf = new DefaultTerminalFactory();
        dtf.setPreferTerminalEmulator(true);
        dtf.setTerminalEmulatorTitle(Properties.GAME_TITLE + " v" + Properties.GAME_VERSION);
        dtf.setInitialTerminalSize(new TerminalSize(w, h));

        try {
            term = dtf.createTerminal();
        } catch (IOException e) {
            System.err.println("Failed to create terminal");
            e.printStackTrace();
            throw new RuntimeException();
        }

        // Validate specified stage size.
        TerminalSize ts;
        try {
            ts = term.getTerminalSize();
        } catch (IOException e) {
            System.err.println("Failed to get terminal size.");
            e.printStackTrace();
            throw new RuntimeException();
        }
        if (ts.getColumns() < width || ts.getRows() < height) {
            throw new ScreenTooSmallException();
        }

        try {
            term.enterPrivateMode();
            term.setCursorVisible(false);
        } catch (IOException e) {
        }
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

    public static void main(String[] args) {
        TileRenderer tr = new TileRenderer(101, 45);
    }

}
