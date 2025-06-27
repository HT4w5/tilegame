package edu.tilegame.world;

import org.junit.jupiter.api.Test;

import edu.tilegame.world.World;
import edu.tilegame.tengine.*;

public class TestDungeon {
    public static void main(String[] args) {
        System.out.println("Beginning Maze generation test.\n");
        TileRenderer tr = new TileRenderer(191,91);
        World myWorld = new World(191, 91, tr, 114514);

        Dungeon mg = new Dungeon(myWorld, 200, 3, 10, 3, 6);
        mg.generate();

        myWorld.tick();
    }

    @Test
    public void stressTest() {
        System.out.println("Beginning Maze generation test.\n");
        TileRenderer tr = new TileRenderer(81,45);
        World myWorld = new World(81, 45, tr);

        for (int i = 0; i < 100; ++i) {
            Dungeon mz = new Dungeon(myWorld);
            mz.generate();
            myWorld.clear();
        }
    }
}