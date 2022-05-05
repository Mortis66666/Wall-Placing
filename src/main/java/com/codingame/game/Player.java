package com.codingame.game;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Sprite;

import java.util.*;


public class Player extends AbstractMultiplayerPlayer {

    public Coord coord;
    private double xUnitsPerCell, yUnitsPerCell;
    private Sprite sprite;

    @Override
    public int getExpectedOutputLines() {
        return 1;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public void setUPC(double x, double y) {
        this.xUnitsPerCell = x;
        this.yUnitsPerCell = y;
    }

    public boolean canGo(Coord target, ArrayList<Wall> walls, int maxStep, int width, int height, MultiplayerGameManager<Player> gameManager) {
        Queue<Step> q = new LinkedList<Step>();
        Set<Coord> visited = new LinkedHashSet<Coord>();

        q.add(new Step(coord, 0));

        while (!q.isEmpty()) {
            Step cStep = q.remove();
            Coord cCoord = cStep.coord;
            int stepCounts = cStep.stepCounts;

            gameManager.addToGameSummary(cCoord.toString());
            gameManager.addToGameSummary("stepCounts: " + stepCounts);


            if (cCoord.equals(target)) {
                return true;
            }

            if (stepCounts < maxStep) {
                for (Coord neighbor: cCoord.neighbours(width, height)) {
                    if (!visited.contains(neighbor) && allow(walls, cCoord, neighbor)) {
                        q.add(new Step(neighbor, stepCounts + 1));
                        visited.add(neighbor);
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

    public void registerSprite(GraphicEntityModule graphicEntityModule) {
        sprite = graphicEntityModule.createSprite()
                .setImage("BrainBurner.png");
        move();
    }

    public void move() {
        int xupc = (int) Math.round(xUnitsPerCell);
        int yupc = (int) Math.round(yUnitsPerCell);
        sprite.setX(coord.x * xupc).setY(coord.y * yupc);
    }
}
