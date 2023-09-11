package fr.simonlou.testrisbot.utils;

public class GameType {

    private GameTypes gameTypes;

    public enum GameTypes {
        IA,
        Player
    };

    public GameType(GameTypes type){
        setGameTypes(type);
    }

    public GameTypes getGameTypes() {
        return gameTypes;
    }

    public void setGameTypes(GameTypes gameTypes) {
        this.gameTypes = gameTypes;
    }

    public boolean isPlayer(){
        return this.gameTypes == GameTypes.Player;
    }

}
