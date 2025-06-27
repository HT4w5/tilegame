package edu.tilegame.utils;

public class Position {
    private int xPos;
    private int yPos;

    public Position(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    // Copy contructor.
    public Position(Position other) {
        this(other.xPos, other.yPos);
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public Position add(Direction dir) {
        Position result = new Position(this);
        switch (dir) {
            case UP:
                ++result.yPos;
                break;
            case DOWN:
                --result.yPos;
                break;
            case LEFT:
                --result.xPos;
                break;
            case RIGHT:
                ++result.xPos;
                break;
        }
        return result;
    }

    public Position add(Direction dir, int times) {
        Position result = new Position(this);
        switch (dir) {
            case UP:
                result.yPos += times;
                break;
            case DOWN:
                result.yPos -= times;
                break;
            case LEFT:
                result.xPos -= times;
                break;
            case RIGHT:
                result.xPos += times;
                break;
        }
        return result;
    }

    public boolean isAdjacent(Position other) {
        if (xPos == other.xPos) {
            if (Math.abs(yPos - other.yPos) == 1) {
                return true;
            } else {
                return false;
            }
        } else if (yPos == other.yPos) {
            if (Math.abs(xPos - other.xPos) == 1) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}