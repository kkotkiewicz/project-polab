package agh.ics.oop.attributes;


public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    public int toInt() {
        switch (this) {
            case NORTH: return 0;
            case NORTHEAST: return 1;
            case EAST: return 2;
            case SOUTHEAST: return 3;
            case SOUTH: return 4;
            case SOUTHWEST: return 5;
            case WEST: return 6;
            case NORTHWEST: return 7;
            default: return 0;
        }
    }

    public MapDirection intToDirection(int value) {
        switch (value) {
            case 0: return NORTH;
            case 1: return NORTHEAST;
            case 2: return EAST;
            case 3: return SOUTHEAST;
            case 4: return SOUTH;
            case 5: return SOUTHWEST;
            case 6: return WEST;
            case 7: return NORTHWEST;
            default: return null;
        }
    }

    public MapDirection changeDirection(int value) {
        return this.intToDirection((this.toInt() + value) % 8);
    }

    public Vector2d toUnitVector() {
        switch (this) {
            case NORTH: return new Vector2d(0, 1);
            case NORTHEAST: return new Vector2d(1, 1);
            case EAST: return new Vector2d(1, 0);
            case SOUTHEAST: return new Vector2d(1, -1);
            case SOUTH: return new Vector2d(0, -1);
            case SOUTHWEST: return new Vector2d(-1, -1);
            case WEST: return new Vector2d(-1, 0);
            case NORTHWEST: return new Vector2d(-1, 1);
            default: return null;
        }
    }
}
