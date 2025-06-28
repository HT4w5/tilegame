package edu.tilegame.sprites;

import edu.tilegame.tengine.Tile;
import edu.tilegame.utils.Position;
import edu.tilegame.world.World;

public abstract class AbstractSprite implements Sprite {
    protected World world;
    protected Tile tile;
    protected Position pos;
    protected Tile currentTile;

    public AbstractSprite(World world, Position pos, Tile t) {
        this.world = world;
        this.tile = t;
        this.pos = pos;

        // Spawn in world.
        currentTile = world.getTile(pos);
        world.setTile(pos, t);
    }

    /**
     * Remove from world.
     */
    @Override
    public void despawn() {
        world.setTile(pos, currentTile);
    }
}
