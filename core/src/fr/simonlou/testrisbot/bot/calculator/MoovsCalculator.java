package fr.simonlou.testrisbot.bot.calculator;

import fr.simonlou.testrisbot.bot.Bot;
import fr.simonlou.testrisbot.bot.Player.Moov;
import fr.simonlou.testrisbot.screens.GameScreen;
import fr.simonlou.testrisbot.tetrisevents.EventCaller;
import fr.simonlou.testrisbot.utils.Debugger;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MoovsCalculator {

    private List<Moov> moovs;

    private int spawn_x;
    private int spawn_y;

    private int next_drop;
    private int drop_timeout = 1;

    private int[][] futureGride;
    private int final_x;
    private int final_y;
    private int final_pitch;
    private int [][] piece;

    private int actual_x;
    private int actual_y;
    private int actual_pitch;
    private int finalYonDrop;
    private final int maxLoopRound = 30;
    private int loopround = 0;

    public MoovsCalculator(int[][] startingGrid, int final_x, int final_y, int final_pitch, int[][] piece) {
        moovs = new ArrayList<>();

        this.futureGride = startingGrid;
        this.final_x = final_x;
        this.final_y = final_y;
        this.final_pitch = final_pitch;
        this.piece = piece;
        spawn_x = GameScreen.GRID_WIDTH / 2 - piece[0].length / 2;
        spawn_y = 23;
        actual_y = spawn_y;
        actual_pitch = 0;
        actual_x = spawn_x;

        //Rajoute le poser de pièce ...
    }

    public List<Moov> findMoovsToHaveGrid(){
        while ((actual_y != final_y || actual_x != final_x || actual_pitch !=  final_pitch) && loopround <= maxLoopRound){
            if(final_pitch != actual_pitch && canTurn()){
                turn();
            }
           else if(final_x == actual_x && final_pitch == actual_pitch){
                drop();
                break;
            }

            //else  // Qd il est enable ça fait souvent tout plater
            else if(actual_x > final_x && canMoovLeft()){
                moovLeft();
            }

            else if(actual_x < final_x && canMoovRight()){
                moovRight();
            }
            else if(actual_y > final_y && canDown()){
                down(false);
            }
            else waitNextMoov();

            loopround++;
        };

        Debugger.moovList(moovs);
        return moovs;
    }


    private boolean canTurn(){
        int[][] rotatedPieces = Bot.rotate(true, piece);
        if(!Bot.checkColoisionBottom(rotatedPieces, futureGride,  actual_x, actual_y)) return true;
        return false;
    } //OK

    private boolean canDown(){
        if(!Bot.checkColoisionBottom(piece, futureGride,  actual_x, actual_y - 1)) return true;
        return false;
    } // oK

    private boolean canMoovLeft(){
        if(!Bot.checkColoisionBottom(piece, futureGride,  actual_x-1, actual_y)) return true;
        return false;
    } // OK

    private  boolean canMoovRight(){
        if(!Bot.checkColoisionBottom(piece, futureGride,  actual_x+1, actual_y)) return true;
        return false;
    } //Ok


    private void turn(){
        actual_pitch++;
        piece = Bot.rotate(true, piece);
        moovs.add(Moov.tu);
        System.out.println("turned");
    }

    private void down(boolean byuser){
        actual_y--;
        //next_drop = drop_timeout - (byuser ? 0 : next_drop);
        moovs.add(Moov.dw);
    }

    private void moovLeft(){
        actual_x--;
        moovs.add(Moov.ml);
    }

    private  void moovRight(){
        actual_x++;
        moovs.add(Moov.mr);
    }

    private void waitNextMoov(){
        moovs.add(Moov.wt);
    }

    private void drop(){

        boolean colide = false;
        while (actual_y > 1 && !colide ){
            if (colide) break;
            if(Bot.checkColoisionBottom(piece, futureGride,  actual_x, actual_y - 1)) colide = true;
            for (int x = 0; x < piece.length; x++){
                for (int y = 0; y < piece.length; y++){
                    if(futureGride[y][x] != 0){
                        if(actual_y - y < 0 || actual_y - y > GameScreen.GRID_HEIGHT){
                            colide = true;
                        }
                        else if(actual_x + x < 0 || actual_x + x  > GameScreen.GRID_WIDTH ){
                            colide = true;
                        }
                        else if(futureGride[actual_y - y][actual_x + x] != 0 && piece[y][x]  != 0){
                            colide = true;
                        }
                    }

                }
            }
            if(!colide) actual_y--;
        }
        moovs.add(Moov.dr);
    }




}
