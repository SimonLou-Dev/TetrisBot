package fr.simonlou.testrisbot.bot;

import fr.simonlou.testrisbot.bot.calculator.PossibilitiesCalculator;
import fr.simonlou.testrisbot.bot.calculator.ScoreCalculator;
import fr.simonlou.testrisbot.screens.GameScreen;
import fr.simonlou.testrisbot.utils.Debugger;
import jdk.javadoc.internal.doclets.toolkit.util.Comparators;


import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Bot {


    public static int[] findBestPlace(int[][] nextPieces, int[][] nextNextPieces, int[][] grid){
        int[][] Agrid = copyGrid(grid.clone(), GameScreen.GRID_HEIGHT, GameScreen.GRID_WIDTH);
        /*
        Steps :
            - Find all possibilites
            - Score the board and sort
            - while moove are not possibilites
                -> check treemap and try to place the piece
            -
         */
        System.out.println("Scoring of my board : " + ScoreCalculator.score(grid.clone()));

       Map<Integer, int[]> possibilities = PossibilitiesCalculator.findPossibilities(Agrid, nextPieces, nextNextPieces);//N°of board {x,y,pitch} TODO : a revoir
       TreeMap<Integer, int[]> possibilitesOrdoned = new TreeMap<>(); // score of board {x,y,pitch}
        System.out.println("Nombres de grilles " + possibilities.size());
        for (int i = 0; i < possibilities.size(); i++) {
            int[][] scoringGrid = new int[GameScreen.GRID_HEIGHT][GameScreen.GRID_WIDTH];
            scoringGrid = copyGrid(Agrid, GameScreen.GRID_HEIGHT, GameScreen.GRID_WIDTH);
            int[] possibility = possibilities.get(i);
            scoringGrid = getScoringGridWithPieces(scoringGrid, possibility[1],  possibility[0], possibility[2], nextPieces);
            int score = ScoreCalculator.score(scoringGrid);
            possibilitesOrdoned.put(score, possibility);

        }



        System.out.println("Meilleure grille : " +possibilitesOrdoned.lastKey() + " pire grille " + possibilitesOrdoned.firstKey());
        int[] bestPossibilitie = possibilitesOrdoned.get(possibilitesOrdoned.lastKey());
        int[][] bestGrid = getScoringGridWithPieces(grid, bestPossibilitie[1],  bestPossibilitie[0], bestPossibilitie[2], nextPieces);
        Debugger.printGrid(bestGrid);

        return possibilitesOrdoned.get(possibilitesOrdoned.lastKey());
    }

    public static int[][] copyGrid(int[][] startingGrid, int gridHeight, int gridWidth){
        int[][] newGrid = new int[gridHeight][gridWidth];
        for(int i = 0; i < startingGrid.length; i++){
            for(int j = 0; j < startingGrid[i].length; j++){
                newGrid[i][j] = startingGrid[i][j];
            }
        }
        return newGrid;
    }


    //Ici il y a un problème ...
    public static int[][] getScoringGridWithPieces(int[][] grid, int piece_x,int piece_y, int pitch, int[][] piece){
        int[][] scoringGrid = grid.clone();
        int[][] pieceInGoodPitch = piece.clone();
        while (pitch > 0){
            pieceInGoodPitch = rotate(true, pieceInGoodPitch);
            pitch--;
        }


        for(int y = 0; y < pieceInGoodPitch.length; y++){
            for (int x = 0; x < pieceInGoodPitch[y].length; x++){
                int newX = piece_x + x;
                int newY = piece_y - y;
                if(newY < 0 || newX > GameScreen.GRID_WIDTH) continue;

                if(pieceInGoodPitch[y][x] == 0) continue;
                scoringGrid[newY][newX] = pieceInGoodPitch[y][x];
            }
        }


        return clearRows(scoringGrid);
    }

    /*
    TODO : mon problème c'est que dans mon tableau final j'ai des pièces qui flottent, j'aimerai avoir une grid à chaque fois pour savoir à quoi ça ressemble


     */

    public static boolean checkColoisionBottom(int[][] piece, int[][] grid, int piece_x, int piece_y){
        for(int y = 0; y < piece.length; y++){
            for(int x = 0; x < piece.length; x++){
                if(piece[y][x] != 0){
                    int b_x = piece_x + x;
                    int b_y = piece_y - y;
                    if(b_y >= GameScreen.GRID_HEIGHT || b_x < 0 || b_y < 0 || b_x >= GameScreen.GRID_WIDTH) break;

                    int u_y = b_y-1;
                    if(u_y < 0 || grid[u_y][b_x] !=  0 ){
                        return true;
                    }

                    //System.out.println("end with " + u_y);
                }
            }
        }

        return false;
    }

    public static int[][] rotate(boolean right, int[][] piece){
        // Assume that piece is a square (not ragged or rectangular)
        int[][] new_piece = new int[piece.length][piece[0].length];

        for(int i = 0; i < piece.length; i++){
            for(int j = 0; j < piece[i].length; j++){
                new_piece[right ? j : piece.length - 1 - j][right ? piece.length - 1 - i : i] = piece[i][j];
            }
        }

        return new_piece;
    }

    private static int[][] clearRows(int[][] grid) {


        for (int i = 0; i < GameScreen.GRID_HEIGHT; ) {
            boolean full = true;

            for (int j = 0; j < GameScreen.GRID_WIDTH; j++) {
                if (grid[i][j] == 0)
                    full = false;
            }

            if (full) {

                for (int k = i; k < GameScreen.GRID_HEIGHT - 1; k++) {
                    grid[k] = grid[k + 1];
                }

                grid[GameScreen.GRID_HEIGHT - 1] = new int[GameScreen.GRID_WIDTH];
            } else {
                i++;
            }

        }

        return grid;
    }


}
