package fr.simonlou.testrisbot.factory;

import fr.simonlou.testrisbot.screens.GameScreen;

public class FactoryRing implements GameScreen.PieceFactory {
    GameScreen.PieceFactory[] factories;
    int nextFactory = -1;

    public FactoryRing(GameScreen.PieceFactory[] fs){
        factories = fs;
    }

    @Override
    public int[][] nextPiece() {
        nextFactory = (nextFactory + 1) % factories.length;
        return factories[nextFactory].nextPiece();
    }
}
