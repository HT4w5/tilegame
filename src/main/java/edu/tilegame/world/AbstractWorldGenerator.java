package edu.tilegame.world;

import java.util.Random;

public abstract class AbstractWorldGenerator implements WorldGenerator {
    protected World world;
    protected Random random;

    public AbstractWorldGenerator(World world) {
        this.world = world;
        random = new Random(world.SEED);
    }
}
