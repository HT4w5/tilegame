package edu.tilegame.tengine;

import com.googlecode.lanterna.TextColor.ANSI;

public class Tileset {
    public static final Tile AIR = new Tile(' ', ANSI.BLACK, ANSI.BLACK);
    public static final Tile WALL = new Tile('#', ANSI.WHITE, ANSI.BLACK_BRIGHT);
    public static final Tile PLAYER = new Tile('@',ANSI.WHITE,ANSI.BLACK);
    public static final Tile FLOOR = new Tile('·',ANSI.GREEN_BRIGHT,ANSI.BLACK);
    public static final Tile LOCKED_DOOR = new Tile('█',ANSI.YELLOW_BRIGHT,ANSI.BLACK);
    public static final Tile UNLOCKED_DOOR = new Tile('▢',ANSI.YELLOW_BRIGHT,ANSI.BLACK);}
