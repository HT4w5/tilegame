package edu.tilegame.sprites;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
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
    private Direction facing;

    public Player(World world, Position pos, Terminal term) {
        super(world, pos, Tileset.PLAYER_N);
        this.term = term;

        // Define walkable tiles.
        walkables.add(Tileset.ENTRANCE);
        walkables.add(Tileset.EXIT);
        walkables.add(Tileset.UNLOCKED_DOOR);
        walkables.add(Tileset.FLOOR);

        // Default facing.
        facing = Direction.DOWN;
    }

    /**
     * Read user input. Blocking.
     */
    @Override
    public void tick() {
        // Get key.
        KeyType kt = KeyType.Unknown;
        KeyStroke ks = new KeyStroke(kt);
        try {
            ks = term.readInput(); // Blocking.
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

            case KeyType.Character:
                switch (ks.getCharacter()) {
                    case 'z':
                        interact();
                        break;

                    default:
                        break;
                }

            default:
                break;
        }

    }

    /**
     * Walk in specified direcion on walkable tiles.
     * Does nothing if tile is not walkable.
     * Turn to direction if not facing.
     * 
     * @param dir
     */
    private void walk(Direction dir) {
        setDirection(dir);
        // Check destination.
        Position newPos = pos.add(dir);
        if (walkables.contains(world.getTile(newPos))) {
            moveTo(newPos);
        } else {
            world.setTile(pos, tile);
        }
    }

    private void setDirection(Direction dir) {
        facing = dir;
        switch (dir) {
            case Direction.DOWN:
                tile = Tileset.PLAYER_N;
                break;

            case Direction.UP:
                tile = Tileset.PLAYER_S;
                break;

            case Direction.LEFT:
                tile = Tileset.PLAYER_W;
                break;

            case Direction.RIGHT:
                tile = Tileset.PLAYER_E;
                break;

            default:
                break;
        }
    }

    private void interact() {
        // Check target.
        Position tgt = pos.add(facing);
        if (world.getTile(tgt) == Tileset.LOCKED_DOOR) {
            world.setTile(tgt, Tileset.UNLOCKED_DOOR);
        } else if (world.getTile(tgt) == Tileset.UNLOCKED_DOOR) {
            world.setTile(tgt, Tileset.LOCKED_DOOR);
        }
    }

}
