package fr.simonlou.testrisbot.tetrisevents;

import fr.simonlou.testrisbot.Game;

import java.util.List;

public class EventCaller {

        public static void fireStartedGameEvent(Game game){
            List<EventList.GameStartedEvent> gameStartedEventListeners = game.gameStartedEventListeners;
            for (EventList.GameStartedEvent listener: gameStartedEventListeners) listener.onGameStartedEvent();
        }

        public static void firePieceSpawnEvent(Game game, int[][] nextPieces, int[][] nextNextPieces, int [][] board){
            List<EventList.PieceSpawnEvent> spawnEventList = game.pieceSpawnEventsListeners;
            for (EventList.PieceSpawnEvent listener: spawnEventList) listener.onPieceSpawnEvent(nextPieces, nextNextPieces, board);
        }

        public static void fireLineClearedEvent(Game game, int[][] nextPieces, int[][] nextNextPieces, int [][] grid){
            List<EventList.LineClearedEvent> lineClearedEvents = game.lineClearedEvents;
            for (EventList.LineClearedEvent listener: lineClearedEvents) listener.onLineClearedEvent(nextPieces, nextNextPieces, grid);
        }

        public static void fireNewTickEvent(Game game, int[][] nextPieces, int[][] nextNextPieces, int [][] grid){
            List<EventList.NextTickEvent> nextTickEvents = game.nextTickEvents;
            for (EventList.NextTickEvent listener: nextTickEvents) listener.onNextTick(nextPieces, nextNextPieces, grid);
        }


}
