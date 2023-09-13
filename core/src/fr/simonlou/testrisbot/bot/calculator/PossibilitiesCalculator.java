package fr.simonlou.testrisbot.bot.calculator;

import fr.simonlou.testrisbot.bot.Bot;
import fr.simonlou.testrisbot.screens.GameScreen;
import fr.simonlou.testrisbot.utils.Debugger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PossibilitiesCalculator {

    private static final int[][] square = {
            {2,2},
            {2,2}
    };


    public static Map<Integer, int[]> findPossibilities(int[][] grid, int[][] piece, int[][] nextPiece){

        Map<Integer, int[]> possibilites = new HashMap<>();
        int firstEmpty = ScoreCalculator.findFirstEmptyLine(grid);


        for (int row = firstEmpty; row < GameScreen.GRID_HEIGHT; row++){
            for (int col = 0; col < GameScreen.GRID_WIDTH; col++){
                int[][] rotatedpieces = piece;

                for (int pitch = 0; pitch < 4; pitch++){
                    int[] coords = {col, row, pitch};
                    if(canPlace(grid, row,col,rotatedpieces)){
                        for(int y = 0; y < piece.length; y++){
                            for(int x = 0; x < piece.length; x++){
                                if(piece[y][x] != 0){
                                    int b_x = row + x;
                                    int b_y = col - y;
                                    if(b_y >= GameScreen.GRID_HEIGHT || b_x < 0 || b_y < 0 || b_x >= GameScreen.GRID_WIDTH) break;
                                    int u_y = b_y-1;

                                    if(u_y == 0 || grid[u_y][b_x] !=  0){
                                        int[][] Mygrid = Bot.copyGrid(grid.clone(), GameScreen.GRID_HEIGHT, GameScreen.GRID_WIDTH);
                                        int[][] spetialgrid = Bot.getScoringGridWithPieces(Mygrid.clone(), row, col, pitch, piece);
                                        Debugger.printGrid(spetialgrid);
                                        if(possibilites.containsValue(coords)) break;

                                        possibilites.put(possibilites.size(),  coords);
                                    }

                                }
                            }
                        }

                    }
                    int[][] testrotated = Bot.rotate(true, rotatedpieces.clone());

                    if(PossibilitiesCalculator.piecesEquals(testrotated, rotatedpieces)) break;
                    else rotatedpieces =  testrotated;
                }

            }
        }

        return possibilites;
    }

    public static boolean piecesEquals(int[][] piece, int[][] otherPiece) {
        if (piece.length != otherPiece.length) {
            return false;
        }
        for (int i = 0; i < piece.length; i++) {
            if (!Arrays.equals(piece[i], otherPiece[i])) {
                return false;
            }
        }
        return true;
    }





    public static boolean canPlace(int[][] grid, int piece_x, int piece_y, int[][] piece){
        //if (Bot.checkColoisionBottom(piece, grid, piece_x, piece_y - 1)) return false;
        for (int y = 0; y < piece.length; y++){
            for (int x = 0; x < piece.length; x++){
                int b_x = piece_x + x;
                int b_y = piece_y - y - 1;
                if(b_y  < 0 || b_y >= GameScreen.GRID_HEIGHT )return  false;
                if(b_x < 0 || b_x >= GameScreen.GRID_WIDTH) return false;
                if(grid[b_y][b_x] != 0 && piece[y][x] != 0) return false;

            }
        }
        return true;
    }




}
