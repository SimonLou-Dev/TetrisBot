package fr.simonlou.testrisbot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import fr.simonlou.testrisbot.Game;
import fr.simonlou.testrisbot.utils.Assets;
import fr.simonlou.testrisbot.utils.GameType;

public class MainMenuScreen implements Screen {
    final Game game;
    OrthographicCamera camera;
    private Sprite  menuScreen, humanBtn, robotBtn, gitBtn;
    public Vector3 touchPoint = new Vector3();


    public MainMenuScreen(Game game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800,480);

        menuScreen = new Sprite(Assets.menuScreen);
        menuScreen.setPosition(0, 0);

        humanBtn = new Sprite(Assets.humanBtn);
        humanBtn.setPosition(450, 100);

        robotBtn = new Sprite(Assets.robotBtn);
        robotBtn.setPosition(146, 100);

        gitBtn = new Sprite(Assets.gitBtn);
        gitBtn.setPosition(670, 10);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0.2f,1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(menuScreen, 0,0);
        game.batch.draw(humanBtn, humanBtn.getX(), humanBtn.getY());
        game.batch.draw(robotBtn,  robotBtn.getX(), robotBtn.getY());
        game.batch.draw(gitBtn, gitBtn.getX() ,gitBtn.getY());
        game.batch.end();

        //gitBtn.get

        if(touched(humanBtn)){
            game.setScreen(new GameScreen(game, GameType.GameTypes.Player));
        }

        if(touched(robotBtn)){
            game.setScreen(new GameScreen(game, GameType.GameTypes.IA));
        }

        if (touched(gitBtn)){
            Gdx.net.openURI("https://github.com/SimonLou-Dev");
        }



    }

    private boolean touched(Sprite sprite){
        float spriteY = 420 - (sprite.getY() + sprite.getHeight());
        boolean inWidth = sprite.getX() <= Gdx.input.getX() && Gdx.input.getX() <= (sprite.getX() + sprite.getWidth());
        boolean inHeight = spriteY <= Gdx.input.getY() && Gdx.input.getY() <= (spriteY + sprite.getHeight());

        if(Assets.isKeyDonglePressed(sprite)) return true;
        if(!Gdx.input.justTouched()) return  false;

        return inWidth && inHeight;
     }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
