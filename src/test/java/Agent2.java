import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Agent2 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int WIDTH = in.nextInt();
        int HEIGHT = in.nextInt();

        // game loop
        while (true) {
            int X = in.nextInt();
            int Y = in.nextInt();
            int opponentX = in.nextInt();
            int opponentY = in.nextInt();
            int nbWalls = in.nextInt();
            for (int i = 0; i < nbWalls; i++) {
                int ax = in.nextInt();
                int ay = in.nextInt();
                int bx = in.nextInt();
                int by = in.nextInt();
            }

            System.out.println("0 0 R");
        }
    }
}
