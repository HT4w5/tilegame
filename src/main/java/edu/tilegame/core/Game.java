package edu.tilegame.core;

import edu.tilegame.sprites.Player;
import edu.tilegame.tengine.CustomTerminalFactory;
import edu.tilegame.tengine.TileRenderer;
import edu.tilegame.utils.Position;
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

    private Player player;

    public Game() {
        // Initialize terminal.
        CustomTerminalFactory ctf = new CustomTerminalFactory();
        ctf.setTerminalSize(WIDTH, HEIGHT);
        term = ctf.createCustomTerminal();

        tr = new TileRenderer(WIDTH, HEIGHT, term);
        world = new World(WIDTH, HEIGHT, tr);
    }

    public void start() {
        startStage();
    }

    /**
     * Start a stage where the world is generated once.
     */
    private void startStage() {
        Dungeon dg = new Dungeon(world);
        dg.generate();

        spawnPlayer(dg.getPlayerInitialPosition());
        world.tick();


        // Main loop.
        boolean exit = false;
        while(!exit) {
            // Update sprites.
            player.tick();

            // Update world.
            world.tick();
        }
    }

    private void spawnPlayer(Position pos) {
        player = new Player(world, pos, term);
    }
}
