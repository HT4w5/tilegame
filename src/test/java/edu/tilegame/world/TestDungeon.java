package edu.tilegame.world;

import org.junit.jupiter.api.Test;

import com.googlecode.lanterna.terminal.Terminal;

import edu.tilegame.world.World;
import edu.tilegame.tengine.*;

public class TestDungeon {
    public static void main(String[] args) {
        System.out.println("Beginning Maze generation test.\n");
        int w = 81;
        int h = 45;

        CustomTerminalFactory ctf = new CustomTerminalFactory();
        ctf.setTerminalSize(w, h);
        Terminal term = ctf.createCustomTerminal();
        TileRenderer tr = new TileRenderer(w, h, term);
        World myWorld = new World(w, h, tr, 1919);

        Dungeon mg = new Dungeon(myWorld, 200, 3, 10, 3, 6);
        mg.generate();

        myWorld.tick();
    }
}