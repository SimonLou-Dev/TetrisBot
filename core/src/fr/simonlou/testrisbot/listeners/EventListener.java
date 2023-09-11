package fr.simonlou.testrisbot.listeners;

import com.badlogic.gdx.Gdx;
import fr.simonlou.testrisbot.Game;
import fr.simonlou.testrisbot.bot.Bot;
import fr.simonlou.testrisbot.bot.Player.Moov;
import fr.simonlou.testrisbot.bot.calculator.MoovsCalculator;
import fr.simonlou.testrisbot.screens.GameScreen;
import fr.simonlou.testrisbot.tetrisevents.EventList;
import fr.simonlou.testrisbot.utils.Debugger;

import java.util.List;

public class EventListener implements EventList.GameStartedEvent, EventList.PieceSpawnEvent, EventList.LineClearedEvent, EventList.NextTickEvent {

    private Game game;

    public EventListener(Game game) {
        this.game = game;
    }

    @Override
    public void onGameStartedEvent() {

    }


    @Override
    public void onLineClearedEvent(int[][] nextPieces, int[][] nextNextPieces, int[][] grid) {

    }

    @Override
    public void onPieceSpawnEvent(int[][] nextPieces, int[][] nextNextPieces, int[][] grid) {
        int[][] nextPiecesA = nextPieces.clone();
        int[][] nextNextPiecesA = nextNextPieces.clone();
        int[][] gridA  = Bot.copyGrid(grid, GameScreen.GRID_HEIGHT, GameScreen.GRID_WIDTH);
        if(!GameScreen.getGameType().isPlayer()){
            int[] bestPos = Bot.findBestPlace(nextPiecesA, nextNextPiecesA, gridA);
            List<Moov> moovs = new MoovsCalculator(gridA, bestPos[1], bestPos[0], bestPos[2], nextPieces).findMoovsToHaveGrid();
            game.AIMoovs = moovs;
        }
    }

    @Override
    public void onNextTick(int[][] nextPieces, int[][] nextNextPieces, int[][] grid) {

        if(!GameScreen.getGameType().isPlayer() && game.AIMoovs.size() != 0){
            Moov nextMoov = game.AIMoovs.get(0);
            game.AIMoovs.remove(nextMoov);
            switch (nextMoov.getCode()){
                case 5: {
                    GameScreen.rotateMoov = true;
                    break;
                }
                case 2: {
                    GameScreen.leftMoov = true;
                    break;
                }
                case 3: {
                    GameScreen.downMoov = true;
                    break;
                }
                case 1: {
                    GameScreen.rightMoov = true;
                    break;
                }
                case 4: {
                    GameScreen.dropMoov = true;
                    break;
                }
                default:break;
            }
        }

    }
}
