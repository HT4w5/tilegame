package edu.tilegame.tengine;

import org.junit.jupiter.api.Test;

public class TileRendererTest {
    public static void main(String[] args) {
        int w = 50;
        int h = 20;

        Tile[][] stage = new Tile[w][h];

        TileRenderer tr = new TileRenderer(w, h);

        // Fill stage with AIR.
        for (int i = 0; i < stage.length; ++i) {
            for (int j = 0; j < stage[0].length; ++j) {
                stage[i][j] = Tileset.AIR;
            }
        }

        stage[0][0] = Tileset.WALL;
        stage[49][19] = Tileset.WALL;
        stage[0][1] = Tileset.PLAYER;
        stage[0][2] = Tileset.FLOOR;

        tr.renderFrame(stage);

    }
}
