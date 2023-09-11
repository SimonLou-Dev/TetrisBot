package fr.simonlou.testrisbot.bot.calculator;

import fr.simonlou.testrisbot.screens.GameScreen;

public class ScoreCalculator {

    public static int findFirstEmptyLine(int[][] grid){
        for (int row = 0; row < GameScreen.GRID_HEIGHT  ;  row++){
            for(int col = 0; col < GameScreen.GRID_WIDTH - 1; col++){
                if(grid[row][col] == 0) return row;
            }
        }
        return GameScreen.GRID_HEIGHT;
    }
    public static int score(int[][] grid){
        int sc = 0;
        sc += 2001* nLines(grid);
        for (int row = 0; row <  GameScreen.GRID_HEIGHT; row++) {
            int ct = 0;
            for (int col = 0; col < GameScreen.GRID_WIDTH ; col++)
                if (grid[row][col] != 0)
                    ct++;
            sc += 7*row*row*ct;
        }

        return sc - 1300*countHoles(grid);
    }

    public static int countHoles(int[][] grid) {
        int ct = 0;
        for (int y=1; y < GameScreen.GRID_HEIGHT; y++)
            for (int x=0; x < GameScreen.GRID_WIDTH; x++)
                if (grid[y][x] == 0 && grid[y-1][x] != 0)
                    ct++;
        return ct;
    }

    public static int nLines(int[][] grid) {

        int ct = 0;

        for (int row = 0; row < GameScreen.GRID_HEIGHT; row++) {
            if (hasLine(row, grid))
                ct++;
        }
        return ct;
    }

    private static boolean hasLine(int row, int[][] grid) {
        for (int col=0; col<GameScreen.GRID_WIDTH; col++){
            if (grid[row][col] != 0)
                return false;
        }

        return true;
    }

}
