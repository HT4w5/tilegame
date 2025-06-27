package edu.tilegame.structures;

import edu.tilegame.world.World;

// Interface for generic structures.
public interface Structure {
    /**
     * Write structure to world object. If out of bounds, discard redundant parts.
     * 
     * @param world
     */
    void clip(World world);

    int getXPos();

    int getYPos();

    /**
     * Check whether this structure overlaps with other structure. Usually only
     * works for same type.
     * 
     * @param other
     */
    boolean overlapsWith(Structure other);
}