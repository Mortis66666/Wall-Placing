package com.codingame.game;

import com.codingame.gameengine.module.entities.GraphicEntityModule;

public class Wall {
    public Coord a, b;
    private final int color;

    public Wall(Coord a, Coord b, int color, GraphicEntityModule graphicEntityModule, double xUnitsPerCell, double yUnitsPerCell) {
        this.a = a;
        this.b = b;
        this.color = color;
        draw(graphicEntityModule, xUnitsPerCell, yUnitsPerCell);
    }

    public boolean allow(Coord cellA, Coord cellB) {
        return !((cellA.equals(a) && cellB.equals(b)) || (cellA.equals(b) && cellB.equals(a)));
    }

    public String toString() {
        return a.toString() + " " + b.toString();
    }

    public void draw(GraphicEntityModule graphicEntityModule, double xUnitsPerCell, double yUnitsPerCell) {
        int xupc = (int) Math.round(xUnitsPerCell);
        int yupc = (int) Math.round(yUnitsPerCell);

        if (a.x != b.x) {
            int mx = Math.max(a.x, b.x) * xupc;
            graphicEntityModule.createLine()
                    .setX(mx)
                    .setY(a.y * yupc)
                    .setX2(mx)
                    .setY2((a.y + 1) * yupc)
                    .setLineWidth(5)
                    .setLineColor(color);
        }

        else if (a.y != b.y) {
            int my = Math.max(a.y, b.y) * yupc;
            graphicEntityModule.createLine()
                    .setX(a.x * xupc)
                    .setY(my)
                    .setX2((a.x + 1) * yupc)
                    .setY2(my)
                    .setLineWidth(5)
                    .setLineColor(color);
        }


    }
}
