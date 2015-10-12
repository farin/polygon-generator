package com.jcloisterzone.polygen;

import com.jcloisterzone.board.Rotation;

//import java.util.WeakHashMap;

public class Point {

    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point scale(int squareSize) {
        return scale(squareSize, 0, 0);
    }

    public Point scale(int squareSize, int boxSize) {
        return scale(squareSize, boxSize,boxSize);
    }

    public Point scale(int squareSize, int xSize, int ySize) {
        return new Point(
                (int) (squareSize * (x / 100.0))
                    - xSize / 2,

                (int) (squareSize * (y / 100.0))
                    - ySize / 2);
    }

    public Point mirrorX() {
        return new Point(100-x,y);
    }
    public Point mirrorY() {
        return new Point(x,100-y);
    }
    public Point rotate() {
        return new Point(100-y,x);
    }

    /**
     * Rotates ImmutablePoint on initial sized tile.
     * @param p ImmutablePoint to rotate
     * @param d how roatate
     * @return rotated ImmutablePoint
     */
    public Point rotate100(Rotation r) {
        int x = this.x, y = this.y, _y;
        for (int i = 0; i < r.ordinal(); i++) {
            _y = y;
            y = x;
            x  = 100-_y;
        }
        return new Point(x, y);
    }

    /**Rotate around [0,0]*/
    public Point rotate(Rotation r) {
        int x = this.x, y = this.y, _y;
        for (int i = 0; i < r.ordinal(); i++) {
            _y = y;
            y = x;
            x  = -_y;
        }
        return new Point(x, y);
    }

    public Point translate(int dx, int dy) {
        return new Point(x + dx, y + dy);
    }

    @Override
    public int hashCode() {
        return (x << 16) ^ y;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point p = (Point)obj;
            return (x == p.x) && (y == p.y);
        }
        return false;
    }

    public String toString() {
        return getClass().getName() + "[x=" + x + ",y=" + y + "]";
    }
}