package fr.simonlou.testrisbot.listeners;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import fr.simonlou.testrisbot.Game;
import fr.simonlou.testrisbot.screens.GameScreen;

public class KeyListeners implements InputProcessor {

    private Game game;
    public KeyListeners(Game game){
        this.game = game;
    }


    @Override
    public boolean keyDown(int keycode) {
        if(game.keyPressed.contains(keycode)) return false;
        else if(GameScreen.isGamePlaying()){
            game.keyPressed.add(keycode);
            switch (keycode){
                case Input.Keys.ESCAPE: {
                    GameScreen.pauseGame();
                    break;
                }
                case Input.Keys.DPAD_UP: {
                    if(!GameScreen.getGameType().isPlayer()) break;
                    GameScreen.rotateMoov = true;
                            break;
                }
                case Input.Keys.DPAD_LEFT: {
                    if(!GameScreen.getGameType().isPlayer()) break;
                    GameScreen.leftMoov = true;
                    break;
                }
                case Input.Keys.DPAD_DOWN: {
                    if(!GameScreen.getGameType().isPlayer()) break;
                    GameScreen.downMoov = true;
                    break;
                }
                case Input.Keys.DPAD_RIGHT: {
                    if(!GameScreen.getGameType().isPlayer()) break;
                    GameScreen.rightMoov = true;
                    break;
                }
                case Input.Keys.CONTROL_RIGHT : {
                    if(!GameScreen.getGameType().isPlayer()) break;
                    GameScreen.dropMoov = true;
                    break;
                }
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(!game.keyPressed.contains(keycode)) return false;
        game.keyPressed.remove(game.keyPressed.indexOf(keycode));
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
