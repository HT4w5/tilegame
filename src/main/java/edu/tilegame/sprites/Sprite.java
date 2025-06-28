package edu.tilegame.sprites;

import edu.tilegame.utils.Position;
import edu.tilegame.tengine.*;

public interface Sprite {
    /**
     * Update sprite by 1 tick.
     */
    public void tick();

    /**
     * Remove from world.
     */
    public void despawn();

    public Position getPosition();
    public Tile getCurrentTile();
}
