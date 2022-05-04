package com.codingame.game;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;

import java.util.*;


public class Player extends AbstractMultiplayerPlayer {

    public Coord coord;

    @Override
    public int getExpectedOutputLines() {
        return 1;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public boolean canGo(Coord target, ArrayList<Wall> walls, int maxStep, int width, int height) {
        Queue<Step> q = new LinkedList<Step>();
        Set<Coord> visited = new LinkedHashSet<Coord>();
        q.add(new Step(coord, 0));

        while (!q.isEmpty()) {
            Step cStep = q.remove();
            Coord cCoord = cStep.coord;
            int stepCounts = cStep.stepCounts;

            if (cCoord.equals(target)) {
                return true;
            }

            visited.add(cCoord);
            if (stepCounts < maxStep) {
                for (Coord neighbor: cCoord.neighbours(width, height)) {
                    if (visited.contains(neighbor) && allow(walls, cCoord, neighbor)) {
                        q.add(new Step(neighbor, stepCounts + 1));
                    }
                }
            }
        }

        return false;
    }

    private boolean allow(ArrayList<Wall> walls, Coord cellA, Coord cellB) {
        for (Wall wall: walls) {
            if (!wall.allow(cellA, cellB)) {
                return false;
            }
        }
        return true;
    }
}
