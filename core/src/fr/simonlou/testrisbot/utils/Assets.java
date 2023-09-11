package fr.simonlou.testrisbot.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Assets {

    public static TextureRegion gameScreen, block, overScreen, pauseScreen, menuScreen, robotBtn, humanBtn, gitBtn, spaceBtn, rotateBtn,grid, leftBtn, rightBtn, downBtn;

    public static Rectangle gameScreenPreview, gameScreenPreview2, gameScreenGrid;
    public static Rectangle gameScreenBottom, gameScreenRotRight, gameScreenLeft, gameScreenRight,
            gameScreenDrop, gameScreenPause1, overMenu, overRestart, pauseMenu, pauseResume;

    public static BitmapFont font;

    public static Color color1, color2, color1a, color1b;

    private static Map<Rectangle, int[]> keyDongles = new HashMap<Rectangle, int[]>();


    public static void load(){
        color1 = rgb(47,170,170);
        color2 = rgb(198,120,55);
        color1a = rgb(13,38,95);
        color1b = rgb(4,113,4);

        Texture texture;
        texture = new Texture(Gdx.files.internal("game-screen.png"));
        gameScreen = new TextureRegion(texture, 0,0,800,480);
        // Note: The width and height store a block size rather than... uh, something reasonable
        gameScreenGrid = new Rectangle(35, 46, 20f * (10f/11f), 20f * (10f/11f));
        gameScreenPause1 = new Rectangle(102, 380, 138, 78);


        texture = new Texture(Gdx.files.internal("arrow-down.png"));
        downBtn = new TextureRegion(texture, 0,0,70,70);
        gameScreenBottom = new Rectangle(419, 39, 70, 70);

        texture = new Texture(Gdx.files.internal("arrow-left.png"));
        leftBtn = new TextureRegion(texture, 0,0,70,70);
        gameScreenLeft = new Rectangle(329, 39, 70, 70); //Good

        texture = new Texture(Gdx.files.internal("arrow-right.png"));
        rightBtn = new TextureRegion(texture, 0,0,70,70);
        gameScreenRight = new Rectangle(509, 39, 70, 70);

        texture = new Texture(Gdx.files.internal("rotate.png"));
        rotateBtn = new TextureRegion(texture, 0,0,70,70);
        gameScreenRotRight = new Rectangle(509, 129, 70, 70);

        texture = new Texture(Gdx.files.internal("space.png"));
        spaceBtn = new TextureRegion(texture, 0,0,160,70);
        gameScreenDrop = new Rectangle(329, 129, 160, 70);



        gameScreenPreview = grid_r(549, 379, 100, 100, 5, 5);
        gameScreenPreview2 = grid_r(549, 270, 100, 100, 5, 5);

        texture = new Texture(Gdx.files.internal("game-over.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        overScreen = new TextureRegion(texture, 0, 0, 800, 480);
        overMenu = new Rectangle(251, 198, 138, 78);
        overRestart = new Rectangle(413, 198, 138, 78);

        texture = new Texture(Gdx.files.internal("game-pause.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        pauseScreen = new TextureRegion(texture, 0, 0, 800, 480);
        pauseMenu = new Rectangle(251, 198, 138, 78);
        pauseResume = new Rectangle(413, 198, 138, 78);

        texture = new Texture(Gdx.files.internal("block.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        block = new TextureRegion(texture, 1, 1, 18, 18);

        texture = new Texture(Gdx.files.internal("grid.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        grid = new TextureRegion(texture, 1, 1, 18, 18);

        texture = new Texture(Gdx.files.internal("menu-background.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        menuScreen = new TextureRegion(texture, 0, 0, 800, 480);

        texture = new Texture(Gdx.files.internal("simon-lou.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        gitBtn = new TextureRegion(texture, 0, 0, 100, 35);

        texture = new Texture(Gdx.files.internal("robot.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        robotBtn = new TextureRegion(texture, 0, 0, 200, 70);

        texture = new Texture(Gdx.files.internal("human.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        humanBtn = new TextureRegion(texture, 0, 0, 200, 70);





        font = new BitmapFont();

    }

    static Color rgb(int r, int g, int b){
        return new Color(r / 255f, g / 255f, b / 255f, 1.0f);
    }

    static Color bright(Color c){
        return c.lerp(1f, 1f, 1f, 1f, 0.5f);
    }

    // So I don't have to think when copying these from Inkscape
    static Rectangle grid_r(float x, float y, float w, float h, float blocks_wide, float blocks_high){
        return new Rectangle(x + 3, y - 32 + 3, (w - 6) / blocks_wide, (h - 6) / blocks_high);
    }

    static HashSet<Integer> currentlyPressed = new HashSet<Integer>();
    // hack hack hack hack hack..
    public static void clearKeyDongles(){
        currentlyPressed = new HashSet<Integer>();
    }

    // Doesn't belong in assets - more like a "platform" file or something
    public static boolean isKeyDonglePressed(Rectangle r){
        if(!keyDongles.containsKey(r))
            return false;

        for(int k : keyDongles.get(r)){
            if(Gdx.input.isKeyPressed(k)) {
                if(!currentlyPressed.contains(k)){
                    currentlyPressed.add(k);
                    return true;
                }
            }
            else {
                // Note: Can only be removed when it's checked! Good thing that happens a lot...
                currentlyPressed.remove(k);
            }
        }

        return false;
    }

    public static boolean isKeyDonglePressed(Sprite t){

        if(!keyDongles.containsKey(t))
            return false;

        for(int k : keyDongles.get(t)){
            if(Gdx.input.isKeyPressed(k)) {
                if(!currentlyPressed.contains(k)){
                    currentlyPressed.add(k);
                    return true;
                }
            }
            else {
                // Note: Can only be removed when it's checked! Good thing that happens a lot...
                currentlyPressed.remove(k);
            }
        }

        return false;
    }

    static Rectangle button_r(float x, float y, float w, float h){
        return new Rectangle(x, y  - 32f, w, h);
    }

    public static Color getBloksColor(int bloksValue){
        switch (bloksValue){
            case 1 : return new Color(rgb(0,255,255));
            case 2 :return new Color(rgb(255,255,0));
            case 3 :return new Color(rgb(128,0,128));
            case 4 :return new Color(rgb(247,160,0));
            case 5 :return new Color(rgb(0,0,255));
            case 6 :return new Color(rgb(255,0,0));
            case 7 :return new Color(rgb(0,255,0));
            default: return null;
        }
    }



    public static int[][][] classicPieces = new int[][][]{
            {
                {0,1,0,0},
                {0,1,0,0},
                {0,1,0,0},
                {0,1,0,0}
            },
            {
                {2,2},
                {2,2}
            },
            {
                {0,3,0},
                {3,3,3},
                {0,0,0}
            },
            {
                {0,4,0},
                {0,4,0},
                {0,4,4}
            },
            {
                {0,5,0},
                {0,5,0},
                {5,5,0}
            },
            {
                {0,6,0},
                {6,6,0},
                {6,0,0}
            },
            {
                {0,7,0},
                {0,7,7},
                {0,0,7}
            }
    };



}
