package com.codingame.game;

import java.util.*;
import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.*;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {

    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;

    private final int WIDTH = 9;
    private final int HEIGHT = 9;
    private final ArrayList<Wall> walls = new ArrayList<>();

    @Override
    public void init() {


        drawGridLine();

        // Send init input
        for (int i = 0; i < gameManager.getPlayerCount(); i++) {
            Player player = gameManager.getPlayer(i);
            player.setCoord(new Coord(i * (WIDTH - 1), i * (HEIGHT - 1)));
            player.setUPC(calculateUnitPerCell(World.DEFAULT_WIDTH, WIDTH), calculateUnitPerCell(World.DEFAULT_HEIGHT , HEIGHT));
            player.registerSprite(graphicEntityModule);
            player.sendInputLine(String.format("%d %d", WIDTH, HEIGHT));
        }
    }

    @Override
    public void gameTurn(int turn) {
        Player currentPlayer = gameManager.getPlayer( 1 -(turn % 2));
        Player opponentPlayer = gameManager.getPlayer(turn % 2);
        sendInputs(currentPlayer, opponentPlayer);
        currentPlayer.execute();



        try {
            String[] output = currentPlayer.getOutputs().get(0).split(" ");

            if (output.length != 3) {
                throw new Exception("Invalid output, expected <x> <y> <U|D|L|R>");
            }

            int moveX = Integer.parseInt(output[0]);
            int moveY = Integer.parseInt(output[1]);
            String direction = output[2];
            Coord target = new Coord(moveX, moveY);

            if (currentPlayer.canGo(target, walls, 3, WIDTH, HEIGHT, gameManager)) {
                Coord placeWall = determineCoord(target, direction);
                if (placeWall.isValid(WIDTH, HEIGHT)) {
                    Wall wall = new Wall(target, placeWall, currentPlayer.getColorToken(), graphicEntityModule, calculateUnitPerCell(World.DEFAULT_WIDTH, WIDTH), calculateUnitPerCell(World.DEFAULT_HEIGHT, HEIGHT));
                    walls.add(wall);

                    currentPlayer.setCoord(target);
                    currentPlayer.move();
                } else {
                    throw new Exception("Cannot place wall on the edge");
                }
            } else {
                throw new Exception(String.format("Can't go to position (%d, %d)", moveX, moveY));
            }


        } catch (TimeoutException e) {
            gameManager.addToGameSummary(GameManager.formatErrorMessage("Times up!"));
            handle(currentPlayer, opponentPlayer);
            abort();
        } catch (NumberFormatException e) {
            gameManager.addToGameSummary(GameManager.formatErrorMessage("Invalid move position"));
            handle(currentPlayer, opponentPlayer);
            abort();
        } catch (Exception e) {
            gameManager.addToGameSummary(GameManager.formatErrorMessage(e.getMessage()));
            handle(currentPlayer, opponentPlayer);
            abort();
        }
    }

    private void sendInputs(Player currentPlayer, Player opponentPlayer) {
        currentPlayer.sendInputLine(currentPlayer.coord.toString());
        currentPlayer.sendInputLine(opponentPlayer.coord.toString());
        currentPlayer.sendInputLine(String.valueOf(walls.size()));

        for (Wall wall: walls) {
            currentPlayer.sendInputLine(wall.toString());
        }
    }

    private void abort() {
        gameManager.endGame();
    }

    private Coord determineCoord(Coord current, String direction) throws Exception {
        switch (direction) {
            case "L": return current.add(new Coord(-1, 0));
            case "R": return current.add(new Coord(1, 0));
            case "U": return current.add(new Coord(0, -1));
            case "D": return current.add(new Coord(0, 1));
        }

        throw new Exception("Invalid direction " + direction);
    }

    private void handle(Player looser, Player winner) {
        win(winner);
        loose(looser);
    }

    private void win(Player winner) {
        gameManager.addToGameSummary(GameManager.formatSuccessMessage(winner.getNicknameToken() + " won!"));
        winner.setScore(10);
    }

    private void loose(Player looser) {
        looser.setScore(-1);
        looser.deactivate();
    }

    private void drawGridLine() {
        // Horizontal Line
        double horizontalUnitsPerCell = calculateUnitPerCell(World.DEFAULT_HEIGHT, HEIGHT);
        for (int y = 0; y < HEIGHT; y++) {
            int drawY = (int) Math.round(y * horizontalUnitsPerCell);
            graphicEntityModule.createLine()
                    .setX(0)
                    .setY(drawY)
                    .setX2(World.DEFAULT_WIDTH)
                    .setY2(drawY)
                    .setLineColor(0xffffff)
                    .setLineWidth(5);
        }
        // Vertical Line
        double verticalUnitsPerCell = calculateUnitPerCell(World.DEFAULT_WIDTH, WIDTH);
        for (int x = 0; x < WIDTH; x++) {
            int drawX = (int) Math.round(x * verticalUnitsPerCell);
            graphicEntityModule.createLine()
                    .setX(drawX)
                    .setY(0)
                    .setX2(drawX)
                    .setY2(World.DEFAULT_HEIGHT)
                    .setLineColor(0xffffff)
                    .setLineWidth(5);
        }
    }

    private double calculateUnitPerCell(int worldLength, int length) {
        return (double) worldLength / length;
    }
}
