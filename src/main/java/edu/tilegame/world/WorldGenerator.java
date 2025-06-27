package edu.tilegame.world;

import edu.tilegame.utils.Position;

public interface WorldGenerator {
    /**
     * Generate world in world object.
     */
    public void generate();

    /**
     * Get Player Initial Position.
     */
    public Position getPlayerInitialPosition();

    /**
     * Get Exit Position.
     */
    public Position getExitPosition();
}
