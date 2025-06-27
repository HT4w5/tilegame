package edu.tilegame.structures;

import edu.tilegame.tengine.*;
import edu.tilegame.world.World;

public class Rect extends AbstractStructure {
    private int width;
    private int height;
    Tile tile;

    public Rect(int xPos, int yPos, int width, int height, Tile tile) {
        super(xPos, yPos);
        this.width = width;
        this.height = height;
        this.tile = tile;
    }

    @Override
    public void clip(World world) {
        world.fillTiles(xPos, yPos, xPos + width - 1, yPos + height - 1, tile);
    }

    public void clipMask(int[][] regionMask, int num) {
        for (int i = xPos; i < xPos + width - 1; ++i) {
            for (int j = yPos; j < xPos + width - 1; ++j) {
                regionMask[i][j] = num;
            }
        }
    }

    @Override
    public boolean overlapsWith(Structure other) {
        if (other instanceof Rect) {
            Rect otherRect = (Rect) other;
            // Calculate boundaries
            int thisX1 = xPos - 1;
            int thisY1 = yPos - 1;
            int thisX2 = thisX1 + width;
            int thisY2 = thisY1 + height;

            int otherX1 = otherRect.xPos - 1;
            int otherY1 = otherRect.yPos - 1;
            int otherX2 = otherX1 + otherRect.width;
            int otherY2 = otherY1 + otherRect.height;

            return !(thisX2 < otherX1 ||
                    otherX2 < thisX1 ||
                    thisY2 < otherY1 ||
                    otherY2 < thisY1);
        } else {
            // Silently fail.
            // TODO: implement checks for other necessary types.
            return false;
        }

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
