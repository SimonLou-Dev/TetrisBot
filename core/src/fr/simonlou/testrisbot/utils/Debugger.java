package fr.simonlou.testrisbot.utils;

import fr.simonlou.testrisbot.bot.Player.Moov;

import java.util.List;

public class Debugger {

    public static void printGrid(int[][] grid){
        String message = "";
        for(int y = grid.length - 1 ; y > 0; y--){
            message += "\n";
            for (int x =  0; x < grid[y].length; x++){
                message += " " + grid[y][x];
            }
        }

        System.out.println(message);

    }

    public static void moovList(List<Moov> moovs){
        String message  = "";
        for (int i = 0; i < moovs.size(); i++){
            Moov actual = moovs.get(i);
            message += actual.toString() + " ";
        }
        System.out.println(message);
    }

}
