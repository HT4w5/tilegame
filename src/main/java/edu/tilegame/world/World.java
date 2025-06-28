package edu.tilegame.world;

import edu.tilegame.tengine.Tile;
import edu.tilegame.tengine.TileRenderer;
import edu.tilegame.tengine.Tileset;
import edu.tilegame.utils.Position;

// World class that represents terrain data.
public class World {

    //////////////////
    // BEGIN FIELDS //
    //////////////////

    public final int WIDTH;
    public final int HEIGHT;
    public final long SEED;
    private Tile[][] world;

    private TileRenderer tr;

    ////////////////
    // END FIELDS //
    ////////////////

    ////////////////////////
    // BEGIN CONSTRUCTORS //
    ////////////////////////

    /**
     * Counstructor specifying width and height of world.
     * 
     * @param w
     * @param h
     */
    public World(int w, int h, TileRenderer tr, long seed) {
        WIDTH = w;
        HEIGHT = h;
        this.tr = tr;
        SEED = seed;

        world = new Tile[WIDTH][HEIGHT];

        // Initialize void world.
        clear();
    }

    public World(int w, int h, TileRenderer ter) {
        this(w, h, ter, System.currentTimeMillis());
    }

    //////////////////////
    // END CONSTRUCTORS //
    //////////////////////

    /////////////////////////////////
    // BEGIN WORLD ARRAY MODIFIERS //
    /////////////////////////////////

    // Set single tile in world array.
    public void setTile(int xPos, int yPos, Tile t) {
        // Check tile.
        if (t == null) {
            throw new IllegalArgumentException("Tile mustn't be null");
        }
        // Check bounds.
        if (xPos < 0 || yPos < 0 || xPos >= WIDTH || yPos >= HEIGHT) {
            return;
        }
        world[xPos][yPos] = t;
    }

    // Shim method.
    public void setTile(Position pos, Tile t) {
        setTile(pos.getX(), pos.getY(), t);
    }

    // Set single tile in world array if previous tile is prev.
    public void setTileIf(int xPos, int yPos, Tile t, Tile prev) {
        // Check tile.
        if (t == null || prev == null) {
            throw new IllegalArgumentException("Tile mustn't be null");
        }
        // Check bounds.
        if (xPos < 0 || yPos < 0 || xPos >= WIDTH || yPos >= HEIGHT) {
            return;
        }
        if (world[xPos][yPos] == prev) {
            world[xPos][yPos] = t;
        }
    }

    // Set horizontal line of tiles in world array. Truncates out of bounds parts.
    public void setTileRow(int xPos, int yPos, int xEnd, Tile t) {
        // Check tile.
        if (t == null) {
            throw new IllegalArgumentException("Tile mustn't be null");
        }
        // Basic bounds check.
        // Check yPos.
        if (yPos < 0 || yPos >= HEIGHT) {
            return; // Not in bounds. Do nothing.
        }
        // Limit yPos and xEnd to bounds first.
        // Then check whether combination is valid.
        if (xPos < 0) {
            xPos = 0;
        }
        if (xEnd >= WIDTH) {
            xEnd = WIDTH - 1;
        }
        if (xEnd < xPos) {
            return; // Empty row. Do nothing.
        }

        for (; xPos <= xEnd; ++xPos) {
            world[xPos][yPos] = t;
        }
    }

    // Set horizontal line of tiles in world array if previous tile is prev.
    // Truncates out of bounds parts.
    public void setTileRowIf(int xPos, int yPos, int xEnd, Tile t, Tile prev) {
        // Check tile.
        if (t == null || prev == null) {
            throw new IllegalArgumentException("Tile mustn't be null");
        }
        // Basic bounds check.
        // Check yPos.
        if (yPos < 0 || yPos >= HEIGHT) {
            return; // Not in bounds. Do nothing.
        }
        // Limit yPos and xEnd to bounds first.
        // Then check whether combination is valid.
        if (xPos < 0) {
            xPos = 0;
        }
        if (xEnd >= WIDTH) {
            xEnd = WIDTH - 1;
        }
        if (xEnd < xPos) {
            return; // Empty row. Do nothing.
        }

        for (; xPos <= xEnd; ++xPos) {
            if (world[xPos][yPos] == prev) {
                world[xPos][yPos] = t;
            }
        }
    }

    // Set vertical collumn of tiles in world array. Truncates out of bounds parts.
    public void setTileColl(int xPos, int yPos, int yEnd, Tile t) {
        // Check tile.
        if (t == null) {
            throw new IllegalArgumentException("Tile mustn't be null");
        }
        // Basic bounds check.
        // Check xPos.
        if (xPos < 0 || xPos >= WIDTH) {
            return; // Not in bounds. Do nothing.
        }
        // Limit xPos and xEnd to bounds first.
        // Then check whether combination is valid.
        if (yPos < 0) {
            yPos = 0;
        }
        if (yEnd >= HEIGHT) {
            yEnd = HEIGHT - 1;
        }
        if (yEnd < yPos) {
            return; // Empty row. Do nothing.
        }

        for (; yPos <= yEnd; ++yPos) {
            world[xPos][yPos] = t;
        }
    }

    // Set vertical collumn of tiles in world array if previous tile is prev.
    // Truncates out of bounds parts.
    public void setTileCollIf(int xPos, int yPos, int yEnd, Tile t, Tile prev) {
        // Check tile.
        if (t == null || prev == null) {
            throw new IllegalArgumentException("Tile mustn't be null");
        }
        // Basic bounds check.
        // Check xPos.
        if (xPos < 0 || xPos >= WIDTH) {
            return; // Not in bounds. Do nothing.
        }
        // Check yPos.
        if (yPos < 0) {
            yPos = 0;
        }
        if (yEnd >= HEIGHT) {
            yEnd = HEIGHT - 1;
        }
        if (yEnd < yPos) {
            return; // Do nothing.
        }

        for (; yPos <= yEnd; ++yPos) {
            if (world[xPos][yPos] == prev) {
                world[xPos][yPos] = t;
            }
        }
    }

    /**
     * Fill rectangular area of tiles in world array. Truncates out of bounds parts.
     * 
     * @param xPos
     * @param yPos
     * @param xLen
     * @param yLen
     * @param t
     */
    public void fillTiles(int xPos, int yPos, int xEnd, int yEnd, Tile t) {
        // Check tile.
        if (t == null) {
            throw new IllegalArgumentException("Tile mustn't be null");
        }
        // Basic bounds check.
        if (xPos < 0) {
            xPos = 0;
        }
        if (yPos < 0) {
            yPos = 0;
        }
        if (xEnd >= WIDTH) {
            xEnd = WIDTH - 1;
        }
        if (yEnd >= HEIGHT) {
            yEnd = HEIGHT - 1;
        }
        if (yEnd < yPos || xEnd < xPos) {
            return;
        }
        for (; xPos <= xEnd; ++xPos) {
            for (int yTmp = yPos; yTmp <= yEnd; ++yTmp) {
                world[xPos][yTmp] = t;
            }
        }
    }

    /**
     * Set all tiles to Tileset.NOTHING.
     */
    public void clear() {
        fillTiles(0, 0, WIDTH - 1, HEIGHT - 1, Tileset.AIR);
    }

    ///////////////////////////////
    // END WORLD ARRAY MODIFIERS //
    ///////////////////////////////

    ////////////////////////////////
    // BEGIN WORLD UPDATE METHODS //
    ////////////////////////////////

    // Render one frame from world array.
    public void tick() {
        tr.renderFrame(world);
    }
    //////////////////////////////
    // END WORLD UPDATE METHODS //
    //////////////////////////////

    ///////////////////
    // BEGIN GETTERS //
    ///////////////////

    public Tile getTile(int xPos, int yPos) {
        return world[xPos][yPos];
    }

    public Tile getTile(Position pos) {
        return world[pos.getX()][pos.getY()];
    }

    /**
     * Checks whether pos is in bounds.
     * @param pos Target position.
     * @return Whether pos is in bounds.
     */
    public boolean contains(Position pos) {
        if (pos.getX() >= 0 &&
                pos.getY() >= 0 &&
                pos.getX() < WIDTH &&
                pos.getY() < HEIGHT) {
            return true;
        } else {
            return false;
        }
    }

    /////////////////
    // END GETTERS //
    /////////////////

    /////////////////////////
    // BEGIN DEBUG METHODS //
    /////////////////////////

    // Get world array.
    public Tile[][] debugGetWorld() {
        return world;
    }

    // Get world width.
    public int debugGetWorldWidth() {
        return WIDTH;
    }

    // Get world height.
    public int debugGetWorldHeight() {
        return HEIGHT;
    }

    ///////////////////////
    // END DEBUG METHODS //
    ///////////////////////

}
