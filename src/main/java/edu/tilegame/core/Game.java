package edu.tilegame.core;

import edu.tilegame.tengine.CustomTerminalFactory;
import edu.tilegame.tengine.TileRenderer;
import edu.tilegame.world.Dungeon;
import edu.tilegame.world.World;

import com.googlecode.lanterna.terminal.Terminal;

public class Game {
    // Must be odd numbers due to generation algorithum.
    public static final int WIDTH = 81;
    public static final int HEIGHT = 31;
    private Terminal term;
    private TileRenderer tr;
    private World world;

    public Game() {
        // Initialize terminal.
        CustomTerminalFactory ctf = new CustomTerminalFactory();
        ctf.setTerminalSize(WIDTH, HEIGHT);
        term = ctf.createCustomTerminal();

        tr = new TileRenderer(81, 45, term);
        world = new World(WIDTH, HEIGHT, tr);
    }

    public void start() {
        boolean exit = false;
        while(!exit) {
            Dungeon dg = new Dungeon(world);
            dg.generate();
        }
    }
}
