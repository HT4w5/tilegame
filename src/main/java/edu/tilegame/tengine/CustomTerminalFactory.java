package edu.tilegame.tengine;

import java.io.File;
import java.io.IOException;
import java.awt.Font;
import java.awt.FontFormatException;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

import edu.tilegame.Properties;
import edu.tilegame.exceptions.ScreenTooSmallException;

public class CustomTerminalFactory {
    private int w;
    private int h;

    public void setTerminalSize(int w, int h) {
        this.w = w;
        this.h = h;
    }

    public Terminal createCustomTerminal() {
        DefaultTerminalFactory dtf = new DefaultTerminalFactory();
        dtf.setPreferTerminalEmulator(true);
        dtf.setTerminalEmulatorTitle(Properties.GAME_TITLE + " v" + Properties.GAME_VERSION);

        // Use custom font on swing.
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/AcPlus_ToshibaSat_8x8.ttf"))
                    .deriveFont(Font.PLAIN, 20);
            dtf.setTerminalEmulatorFontConfiguration(
                    SwingTerminalFontConfiguration.newInstance(customFont));

        } catch (FontFormatException | IOException e) {
            System.err.println("Failed to load custom font");
            e.printStackTrace();
        }

        dtf.setInitialTerminalSize(new TerminalSize(w, h));

        Terminal term;
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
        if (ts.getColumns() < w || ts.getRows() < h) {
            throw new ScreenTooSmallException();
        }

        try {
            term.enterPrivateMode();
            term.setCursorVisible(false);
        } catch (IOException e) {
        }

        return term;
    }
}
