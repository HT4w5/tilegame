package edu.tilegame.sprites;

import java.util.Random;

import edu.tilegame.world.World;
import edu.tilegame.tengine.*;
import edu.tilegame.utils.Direction;
import edu.tilegame.utils.Position;

public abstract class MovableSprite extends AbstractSprite {
    protected Random random;

    public MovableSprite(World world, Position pos, Tile t) {
        super(world, pos, t);
        random = new Random(System.currentTimeMillis());
    }

    /**
     * Move one tile in specified direction. Does not check environment.
     * Does nothing if out of bounds.
     * 
     * @param dir Move direction.
     */
    protected void move(Direction dir) {
        // Check bounds.
        Position newPos = pos.add(dir);
        if (world.contains(newPos)) {
            world.setTile(pos, currentTile);
            currentTile = world.getTile(newPos);
            world.setTile(newPos, tile);
            pos = newPos;
        }

    }

    /**
     * Move sprite to specified position.
     */
    protected void moveTo(Position newPos) {
        // Check bounds.
        if (world.contains(newPos)) {
            world.setTile(pos, currentTile);
            currentTile = world.getTile(newPos);
            world.setTile(newPos, tile);
            pos = newPos;
        }

    }
}
