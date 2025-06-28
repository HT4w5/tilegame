package edu.tilegame.sprites;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;

import edu.tilegame.tengine.*;
import edu.tilegame.utils.Direction;
import edu.tilegame.utils.Position;
import edu.tilegame.world.World;

public class Player extends MovableSprite {
    // For user input.
    private Terminal term;
    private Set<Tile> walkables = new HashSet<>();

    public Player(World world, Position pos, Terminal term) {
        super(world, pos, Tileset.PLAYER);
        this.term = term;

        // Define walkable tiles.
        walkables.add(Tileset.ENTRANCE);
        walkables.add(Tileset.EXIT);
        walkables.add(Tileset.UNLOCKED_DOOR);
        walkables.add(Tileset.FLOOR);
    }

    /**
     * Read user input. Blocking.
     */
    @Override
    public void tick() {
        // Get key.
        KeyType kt = KeyType.Unknown; // Default.
        try {
            KeyStroke ks = term.readInput(); // Blocking.
            kt = ks.getKeyType();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Determine action.
        // Mapping: Direction.UP is positive y, which means down on screen, lol.
        switch (kt) {
            case KeyType.ArrowUp:
                walk(Direction.DOWN);
                break;

            case KeyType.ArrowDown:
                walk(Direction.UP);
                break;

            case KeyType.ArrowLeft:
                walk(Direction.LEFT);
                break;

            case KeyType.ArrowRight:
                walk(Direction.RIGHT);
                break;

            default:
                break;
        }

    }

    /**
     * Walk in specified direcion on walkable tiles.
     * Does nothing if tile is not walkable.
     * 
     * @param dir
     */
    private void walk(Direction dir) {
        // Check destination.
        Position newPos = pos.add(dir);
        if (walkables.contains(world.getTile(newPos))) {
            moveTo(newPos);
        }
    }

}
