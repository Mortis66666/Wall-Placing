package com.codingame.game;

import java.util.ArrayList;

public class Coord {
    public int x, y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return String.format("%d %d", x, y);
    }

    public boolean equals(Object oth) {
        Coord other = (Coord) oth;
        return x == other.x && y == other.y;
    }

    public ArrayList<Coord> neighbours(int width, int height) {
        ArrayList<Coord> res = new ArrayList<Coord>();

        if (x > 0) {
            res.add(new Coord(x - 1, y));
        }
        if (x < width - 1) {
            res.add(new Coord(x + 1, y));
        }
        if (y > 0) {
            res.add(new Coord(x, y - 1));
        }
        if (y < height - 1) {
            res.add(new Coord(x, y + 1));
        }

        return res;
    }

    public Coord add(Coord vec) {
        return new Coord(x + vec.x, y + vec.y);
    }

    public boolean isValid(int width, int height) {
        return x > -1 && y > -1 && x < width && y < height;
    }
}
