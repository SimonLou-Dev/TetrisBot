package fr.simonlou.testrisbot.tetrisevents;

public class EventList
{

    public interface PiecePlacedEvent {

        void onPiecePlacedEvent();

    }

    public interface GameStartedEvent {

        void onGameStartedEvent();

    }

    public interface LineClearedEvent{
        void onLineClearedEvent(int[][] nextPieces, int[][] nextNextPieces, int[][] board);
    }

    public interface PieceSpawnEvent{
        void onPieceSpawnEvent(int[][] nextPieces, int[][] nextNextPieces, int [][] board);
    }

    public interface NextTickEvent{
        void onNextTick(int[][] nextPieces, int[][] nextNextPieces, int[][] grid);
    }

}
