package edu.tilegame.sprites;

public interface Sprite {
    /**
     * Update sprite by 1 tick.
     */
    public void tick();

    /**
     * Remove from world.
     */
    public void despawn();
}
