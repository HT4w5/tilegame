package edu.tilegame.tengine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Font;
import java.awt.FontFormatException;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

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

        // Use custom font on swing.
        try {
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/AcPlus_ToshibaSat_8x8.ttf");
            if (fontStream != null) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.PLAIN, 10);
                dtf.setTerminalEmulatorFontConfiguration(
                        SwingTerminalFontConfiguration.newInstance(customFont));
            }

        } catch (FontFormatException | IOException e) {
            System.err.println("Failed to load custom font");
            e.printStackTrace();
        }

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
