package com.codingame.game;

public class Wall {
    public Coord a, b;

    public Wall(Coord a, Coord b) {
        this.a = a;
        this.b = b;
    }

    public boolean allow(Coord cellA, Coord cellB) {
        return (cellA.equals(a) && cellB.equals(b)) || (cellA.equals(b)) && cellB.equals(a);
    }

    public String toString() {
        return a.toString() + " " + b.toString();
    }
}
