package fr.simonlou.testrisbot.factory;

import fr.simonlou.testrisbot.screens.GameScreen;

public class PieceBagFactory implements GameScreen.PieceFactory {

    int[][][] pieces;

    public PieceBagFactory(int[][][] pieces){
        this.pieces = pieces;
    }

    @Override
    public int[][] nextPiece() {
        return pieces[(int) Math.floor(Math.random() * pieces.length)];
    }
}
