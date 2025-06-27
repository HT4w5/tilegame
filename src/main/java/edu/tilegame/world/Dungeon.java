package edu.tilegame.world;

import java.nio.file.DirectoryIteratorException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import edu.tilegame.tengine.*;
import edu.tilegame.structures.Rect;
import edu.tilegame.utils.*;

/**
 * Generation algorithum from https://github.com/munificent/hauberk.
 */
public class Dungeon extends AbstractWorldGenerator {
    private final int ROOM_GEN_TRIALS;

    private final int ROOM_MIN_WIDTH;
    private final int ROOM_MAX_WIDTH;
    private final int ROOM_MIN_HEIGHT;
    private final int ROOM_MAX_HEIGHT;
    private final Tile FLOOR;
    private final Tile WALL;
    private final Tile DOOR;

    private final int WINDING_PERCENT;
    private final int EXTRA_CONNECTOR_CHANCE;

    private Vector<Rect> rooms;
    private int[][] regionMask;
    private int currentRegion;

    public Dungeon(World world, int roomGenTrials, int minWidth, int maxWidth, int minHeight,
            int maxHeight, Tile floor, Tile wall) {
        super(world);
        // Check world size: must be odd.
        if (world.WIDTH % 2 == 0 || world.HEIGHT % 2 == 0) {
            throw new IllegalArgumentException("Maze generator only works with odd-sized worlds");
        }
        ROOM_GEN_TRIALS = roomGenTrials;
        ROOM_MIN_WIDTH = minWidth;
        ROOM_MAX_WIDTH = maxWidth;
        ROOM_MIN_HEIGHT = minHeight;
        ROOM_MAX_HEIGHT = maxHeight;
        FLOOR = floor;
        WALL = wall;

        WINDING_PERCENT = 50; // TODO: Add to params.
        EXTRA_CONNECTOR_CHANCE = 10000;
        DOOR = Tileset.LOCKED_DOOR;

        rooms = new Vector<>(ROOM_GEN_TRIALS / 3); // TODO: Optimize pre-allocation.
        regionMask = new int[world.WIDTH][world.HEIGHT];

        currentRegion = 0; // Start from one.
    }

    public Dungeon(World world, int roomGenTrials, int minWidth, int maxWidth, int minHeight,
            int maxHeight) {
        this(world, roomGenTrials, minWidth, maxWidth, minHeight, maxHeight, Tileset.FLOOR,
                Tileset.WALL);
    }

    public Dungeon(World world) {
        this(world, 200, 3, 8, 3, 8);
    }

    @Override
    public void generate() {
        // Fill world with walls.
        world.fillTiles(0, 0, world.WIDTH - 1, world.HEIGHT - 1, WALL);

        // Generate rooms.
        generateRooms();
        for (Rect r : rooms) {
            r.clip(world);
            startRegion();
            fillRegionMask(r, currentRegion);
        }

        // Generate maze.
        for (int i = 1; i < world.WIDTH; i += 2) {
            for (int j = 1; j < world.HEIGHT; j += 2) {
                if (world.getTile(i, j) != WALL) {
                    continue;
                }
                generateMaze(new Position(i, j));
            }
        }

        // Connect regions.
        connectRegions();

        removeDeadEnds();
        removeExtraWalls();
    }

    private void generateRooms() {
        for (int i = 0; i < ROOM_GEN_TRIALS; ++i) {
            // Generate random size and position for each room.
            // Make sure size and pos are odd.
            int w = makeOdd(RandomUtils.uniform(random, ROOM_MIN_WIDTH, ROOM_MAX_WIDTH + 1));
            int h = makeOdd(RandomUtils.uniform(random, ROOM_MIN_HEIGHT, ROOM_MAX_HEIGHT + 1));

            int x = makeOdd(RandomUtils.uniform(random, world.WIDTH - 1 - w));
            int y = makeOdd(RandomUtils.uniform(random, world.HEIGHT - 1 - h));

            Rect room = new Rect(x, y, w, h, FLOOR);

            boolean overlaps = false;
            for (Rect r : rooms) {
                if (r.overlapsWith(room)) {
                    overlaps = true;
                    break;
                }
            }

            if (overlaps) {
                continue;
            }

            rooms.add(room);
        }
    }

    private int makeOdd(int n) {
        if ((n & 1) == 0) {
            return n + 1;
        } else {
            return n;
        }
    }

    private void generateMaze(Position start) {
        Queue<Position> cells = new LinkedList<>();
        Direction lastDir = null;

        startRegion();
        carve(start);

        cells.add(start);
        while (!cells.isEmpty()) {
            Position cell = cells.peek();
            cells.remove();

            Vector<Direction> unmadeCells = new Vector<>();

            for (Direction dir : Direction.values()) {
                if (canCarve(cell, dir)) {
                    unmadeCells.add(dir);
                }

            }
            if (!unmadeCells.isEmpty()) {
                Direction dir;
                if (unmadeCells.contains(lastDir)
                        && RandomUtils.uniform(random, 100) > WINDING_PERCENT) {
                    dir = lastDir;
                } else {
                    int index = RandomUtils.uniform(random, unmadeCells.size());
                    dir = unmadeCells.get(index);
                }

                carve(cell.add(dir, 1));
                carve(cell.add(dir, 2));

                cells.add(cell.add(dir, 2));
                lastDir = dir;

            } else {
                lastDir = null;
            }

        }

    }

    private void carve(Position pos) {
        carve(pos, FLOOR);
    }

    private void carve(Position pos, Tile tile) {
        regionMask[pos.getX()][pos.getY()] = currentRegion;
        world.setTile(pos.getX(), pos.getY(), tile);
    }

    private boolean canCarve(Position pos, Direction dir) {
        // Must end in bounds
        if (!world.contains(pos.add(dir, 3))) {
            return false;
        } else {
            return world.getTile(pos.add(dir, 2)) == WALL;
        }
    }

    private void startRegion() {
        ++currentRegion;
    }

    private int getRegion(Position pos) {
        return regionMask[pos.getX()][pos.getY()];
    }

    private void connectRegions() {
        Map<Position, Set<Integer>> connectorsMap = new HashMap<>(); // TODO: Optimize allocation.
        for (int i = 0; i < world.WIDTH; ++i) {
            for (int j = 0; j < world.HEIGHT; ++j) {
                if (world.getTile(i, j) != WALL) {
                    continue;
                }
                // Count adjacent regions.
                Position pos = new Position(i, j);
                Set<Integer> adjRegions = new HashSet<>();
                for (Direction dir : Direction.values()) {
                    Position tmpPos = pos.add(dir);
                    if (world.contains(tmpPos)) {
                        int n = getRegion(tmpPos);
                        if (n == 0) {
                            continue;
                        } else {
                            adjRegions.add(n);
                        }
                    }
                }
                // Filter out region connectors.
                if (adjRegions.size() < 2) {
                    continue;
                }
                connectorsMap.put(pos, adjRegions);

            }
        }

        Position[] connectors = connectorsMap.keySet().toArray(new Position[0]);

        // for (int i = 0; i < connectors.length; ++i) {
        // world.setTile(connectors[i].getX(), connectors[i].getY(), DOOR);
        // }

        Set<Integer> openRegions = new HashSet<>();
        int[] merged = new int[currentRegion + 1];

        for (int i = 0; i < currentRegion; ++i) {
            merged[i] = i;
            openRegions.add(i);
        }

        RandomUtils.uniformShuffle(random, connectors);

        int index = 0;
        while (openRegions.size() > 1) {
            Position connector = connectors[index];
            if (connector == null) {
                ++index;
                continue;
            }
            addJunction(connector);

            Set<Integer> sources = new HashSet<>();
            for (Integer i : connectorsMap.get(connector)) {
                sources.add(merged[i]);
            }

            int dest = sources.iterator().next().intValue();
            sources.remove(dest);

            for (int i = 0; i < currentRegion; ++i) {
                if (sources.contains(merged[i])) {
                    merged[i] = dest;
                }
            }

            openRegions.removeAll(sources);

            for (int i = 0; i < connectors.length; ++i) {
                if (connectors[i] == null) {
                    continue;
                }
                // Don't allow adjacent connectors.
                if (connector.isAdjacent(connectors[i])) {
                    connectors[i] = null;
                    continue;
                }
                // Remove connectors no longer connecting different regions.
                Set<Integer> regions = new HashSet<>();
                for (Integer reg : connectorsMap.get(connectors[i])) {
                    regions.add(merged[reg.intValue()]);
                }
                if (regions.size() <= 1) {
                    connectors[i] = null;
                    continue;
                }
                // Occasionally carve out extra connectors.
                int factor = RandomUtils.uniform(random, EXTRA_CONNECTOR_CHANCE);
                if (factor == 1) {
                    addJunction(connectors[i]);
                }
            }

            ++index;
        }

    }

    private void addJunction(Position pos) {
        Tile tile;
        int factor = RandomUtils.uniform(random, 3);
        switch (factor) {
            case 0:
                tile = FLOOR;
                break;
            case 1:
                tile = Tileset.LOCKED_DOOR; // TODO: Add to params.
                break;
            case 2:
                tile = Tileset.UNLOCKED_DOOR;
                break;
            default:
                tile = FLOOR;
                break;
        }
        // Write to world.
        world.setTile(pos.getX(), pos.getY(), tile);
    }

    private void fillRegionMask(Rect r, int regionId) {
        int xPos = r.getXPos();
        int yPos = r.getYPos();
        int xEnd = xPos + r.getWidth() - 1;
        int yEnd = yPos + r.getHeight() - 1;
        // Basic bounds check.
        if (xPos < 0) {
            xPos = 0;
        }
        if (yPos < 0) {
            yPos = 0;
        }
        if (xEnd >= world.WIDTH) {
            xEnd = world.WIDTH - 1;
        }
        if (yEnd >= world.HEIGHT) {
            yEnd = world.HEIGHT - 1;
        }
        if (yEnd < yPos || xEnd < xPos) {
            return;
        }
        for (; xPos <= xEnd; ++xPos) {
            for (int yTmp = yPos; yTmp <= yEnd; ++yTmp) {
                regionMask[xPos][yTmp] = regionId;
            }
        }
    }

    private void removeDeadEnds() {
        boolean done = false;

        while (!done) {
            done = true;

            for (int i = 1; i < world.WIDTH - 1; ++i) {
                for (int j = 1; j < world.HEIGHT - 1; ++j) {
                    Position pos = new Position(i, j);
                    if (world.getTile(pos) == WALL) {
                        continue;
                    }

                    int exits = 0;
                    for (Direction dir : Direction.values()) {
                        if (world.getTile(pos.add(dir)) != WALL) {
                            ++exits;
                        }
                    }

                    if (exits != 1) {
                        continue;
                    }

                    // Remove tile.
                    done = false;
                    world.setTile(i, j, WALL);
                }
            }
        }
    }

    private void removeExtraWalls() {
        Vector<Position> extraWalls = new Vector<>();
        for (int i = 0; i < world.WIDTH; ++i) {
            for (int j = 0; j < world.HEIGHT; ++j) {
                Position pos = new Position(i, j);
                if (world.getTile(pos) != WALL) {
                    continue;
                }
                // Add to remove list if surrounded by walls.
                int exits = 0;
                for (Direction dir : Direction.values()) {
                    Position newPos = pos.add(dir);
                    if (world.contains(newPos) && world.getTile(newPos) != WALL) {
                        ++exits;
                    }
                }

                if (exits != 0) {
                    continue;
                }

                extraWalls.add(pos);
            }
        }

        // Remove.
        for (Position pos : extraWalls) {
            world.setTile(pos.getX(), pos.getY(), Tileset.AIR);
        }
    }

    /*
     * Old implemention.
     * public void generate(World world, long seed) {
     * // Generate rooms.
     * Random random = new Random(seed);
     * int xPos, yPos, width, height;
     * Vector<Room> rooms = new Vector<>(40);
     * for (int i = 0; i < ROOM_GEN_TRIALS; ++i) {
     * // Generate room props from seed.
     * xPos = RandomUtils.uniform(random, world.WIDTH - 1);
     * yPos = RandomUtils.uniform(random, world.HEIGHT - 1);
     * width = RandomUtils.uniform(random, MIN_WIDTH, MAX_WIDTH);
     * height = RandomUtils.uniform(random, MIN_HEIGHT, MAX_HEIGHT);
     * rooms.add(new Room(xPos, yPos, width, height));
     * }
     * // Discard 50% overlapping.
     * 
     * double factor;
     * int origSize = rooms.size();
     * for (int i = 0; i < origSize - 1; ++i) {
     * for (int j = i + 1; j < origSize; ++j) {
     * if (rooms.get(i).overlapsWith(rooms.get(j))) {
     * factor = RandomUtils.uniform(random);
     * if (true) {
     * rooms.set(i, null);
     * break;
     * }
     * }
     * }
     * }
     * 
     * // Draw hallways.
     * Room tmp;
     * Vector<Hallway> hallways = new Vector<>(rooms.size() / 2);
     * int maxLen = Math.max(world.WIDTH, world.HEIGHT);
     * for (int i = 0; i < rooms.size(); ++i) {
     * tmp = rooms.get(i);
     * if (tmp == null) {
     * continue;
     * }
     * 
     * // Generate random starting points and lengths.
     * factor = RandomUtils.uniform(random);
     * boolean vert = false;
     * if (factor > 0.5) {
     * vert = true;
     * }
     * 
     * int xStart = tmp.getXPos() + RandomUtils.uniform(random, tmp.getWidth());
     * int yStart = tmp.getYPos() + RandomUtils.uniform(random, tmp.getHeight());
     * 
     * factor = RandomUtils.uniform(random);
     * 
     * if (factor > 0.3) {
     * hallways.add(new Hallway(xStart, yStart, vert,
     * RandomUtils.uniform(random, maxLen), Tileset.FLOOR, Tileset.WALL));
     * }
     * }
     * 
     * // Store structures.
     * for (Room r : rooms) {
     * if (r != null) {
     * world.addStructure(r);
     * }
     * }
     * // for (Hallway h : hallways) {
     * // world.addStructure(h);
     * // }
     * 
     * world.clipStructures();
     * }
     */
}
