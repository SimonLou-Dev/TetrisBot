package fr.simonlou.testrisbot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import fr.simonlou.testrisbot.Game;
import fr.simonlou.testrisbot.factory.FactoryRing;
import fr.simonlou.testrisbot.factory.PieceBagFactory;
import fr.simonlou.testrisbot.tetrisevents.EventCaller;
import fr.simonlou.testrisbot.utils.Assets;
import fr.simonlou.testrisbot.utils.GameType;

public class GameScreen implements Screen {

    public static final int BLOCK_SIZE = 18;
    public static final int GRID_HEIGHT = 24;
    public static final int GRID_WIDTH = 11;

    public SpriteBatch batch = new SpriteBatch();
    public Vector3 touchPoint = new Vector3();
    private static GameType gameType;

    public Game game;
    public OrthographicCamera cam;
    public Sprite mm_sprite, block_sprite, over_sprite, pause_sprite, right_sprite, left_sprite, down_sprite, rotate_sprite, space_sprite, grid_sprite;
    public BitmapFont font = new BitmapFont();
    private static boolean gammeRunning = false;
    private static boolean requirePause = false;
    public static boolean downMoov = false;
    public static boolean dropMoov = false;
    public static boolean leftMoov = false;
    public static boolean rightMoov = false;
    public static boolean rotateMoov = false;

    public float drop_timeout = 1;
    public int rows = 0;
    public int score = 0;

    public static GameState state;
    public PieceFactory factory;

    public static boolean isGamePlaying() {
        return gammeRunning;
    }

    public static void pauseGame(){
        requirePause = true;
    }

    public static int[][] getGrid() {
        return grid.clone();
    }

    public static void setGrid(int[][] grid) {
        GameScreen.grid = grid;
    }



    public interface GameState {
        void draw();
        void update(float delta);
    }



    public  interface PieceFactory {
        int[][] nextPiece();
    }

    // row-major grid, 0 at the bottom, 21/20 hidden at the top
    public static int[][] grid = new int[GRID_HEIGHT][GRID_WIDTH];

    public GameScreen(Game game, GameType.GameTypes type){
        this.game = game;
        GameScreen.grid = new int[GRID_HEIGHT][GRID_WIDTH];
        this.gameType = new GameType(type);
        this.factory = new FactoryRing(new PieceFactory[]{
                new PieceBagFactory(Assets.classicPieces),
                new PieceBagFactory(Assets.classicPieces),
        });



        this.nextPiece = nextPiece();
        this.nextNextPiece = nextPiece();


        state = new PieceState();

        cam = new OrthographicCamera();
        cam.setToOrtho(false, 800, 480);

        mm_sprite = new Sprite(Assets.gameScreen);
        mm_sprite.setPosition(0, 0);

        over_sprite = new Sprite(Assets.overScreen);
        over_sprite.setPosition(0, 0);

        pause_sprite = new Sprite(Assets.pauseScreen);
        pause_sprite.setPosition(0, 0);

        block_sprite = new Sprite(Assets.block);
        block_sprite.setPosition(0, 0);

        left_sprite = new Sprite(Assets.leftBtn);
        left_sprite.setPosition(0, 0);

        right_sprite = new Sprite(Assets.rightBtn);
        right_sprite.setRotation(180f);

        down_sprite = new Sprite(Assets.downBtn);
        down_sprite.setRotation(90f);

        rotate_sprite = new Sprite(Assets.rotateBtn);
        rotate_sprite.setPosition(0, 0);

        space_sprite = new Sprite(Assets.spaceBtn);
        space_sprite.setPosition(0, 0);

        grid_sprite = new Sprite(Assets.grid);
        grid_sprite.setPosition(0, 0);
    }

    public static GameType getGameType() {
        return GameScreen.gameType;
    }

    class GameOverState implements GameState {
        GameState losingState;

        public GameOverState(GameState losing){
            losingState = losing;
        }

        public void draw(){
            losingState.draw();
            batch.setColor(Color.WHITE);
            batch.draw(over_sprite, 0, 0);
        }


        public void update(float delta){
            gammeRunning = false;
            if(touched(Assets.overMenu)){
                game.setScreen(new MainMenuScreen(game));
            }

            if(touched(Assets.overRestart)){
                game.setScreen(new GameScreen(game, gameType.getGameTypes()));
            }
        }
    }

    public class PausedState implements GameState {
        GameState returnState;
        public PausedState(GameState returnState){
            this.returnState = returnState;
        }

        @Override
        public void draw(){

            returnState.draw();
            batch.setColor(Color.WHITE);
            batch.draw(pause_sprite, 0, 0);
        }

        @Override
        public void update(float delta){
            gammeRunning = false;
            if(touched(Assets.pauseResume)){
                requirePause = false;
                gammeRunning = true;
                state = returnState;
            }

            if(touched(Assets.pauseMenu)){

                game.setScreen(new MainMenuScreen(game));
            }
        }
    }

    class ClearingState implements GameState {
        float left = 0.4f;
        boolean cleared = false;

        public void draw(){
            drawGame();
        }

        public void update(float delta){
            if(requirePause){
                gammeRunning = false;
                state = new PausedState(this);
            }else{
                gammeRunning = true;
            }
            /*if(touched(Assets.gameScreenPause1)){

            }*/

            left -= delta;

            if(left <= 0.2f && !cleared){
                clearRows();
                cleared = true;
            }

            if(left <= 0){
                state = new PieceState();
            }
        }
    }

    public class PieceState implements GameState {
        // A square array of length 2/3/4
        int[][] piece = popPiece();

        // These are interpreted as grid position of the top left corner
        // of the piece (which give the odd-looking "- i" bits) -
        // should be changed to bottom left corner at some point

        int piece_x = GRID_WIDTH / 2 - piece[0].length / 2;
        int piece_y = 23;

        float next_drop = drop_timeout;

        boolean checkEnd = true;

        public void draw(){
            if(requirePause){
                gammeRunning = false;
                state = new PausedState(this);
            }else{
                gammeRunning = true;
            }
            drawGame();

            drawBlocks(piece, Assets.gameScreenGrid, piece_x, piece_y, true,  false);
        }

        // copy piece onto grid
        void attachPiece(){
            int size = piece.length;
            for(int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    if(piece[i][j] != 0){
                        grid[piece_y - i][piece_x + j] = piece[i][j];
                    }
                }
            }
        }

        public void update(float delta){
            // If this check is done in the constructor, it's possible for other state to be overwritten
            // (e.g. by my own sloppiness)

            if(checkEnd){
                if(collidesWithGridOrWall(piece, piece_x, piece_y)){
                    state = new GameOverState(this);

                    return;
                }
                checkEnd = false;
            }

            if(touched(Assets.gameScreenPause1)){
                state = new PausedState(PieceState.this);
            }

            next_drop -= delta;

            if (next_drop <= 0) {
                System.out.println(piece_x + " " + piece_y);
                EventCaller.fireNewTickEvent(game, nextPiece, nextNextPiece, grid);
                dropPiece(false);
            }

            checkRequiredMoovs();
            handlePieceKeys();
        }

        void checkRequiredMoovs(){
            if (dropMoov){
                dropDownPiece();
                dropMoov = false;

            }

            if(!collidesWithGridOrWall(piece, piece_x - 1, piece_y) && leftMoov){
                piece_x -= 1;
                leftMoov = false;
            }

            if(!collidesWithGridOrWall(piece, piece_x + 1, piece_y) && rightMoov) {
                piece_x += 1;
                rightMoov = false;
            }

            if(!collidesWithGridOrWall(piece, piece_x, piece_y -1 ) && downMoov){
                dropPiece(true);
                downMoov = false;
            }

            int[][] new_piece = GameScreen.this.rotate(true, piece);
            if(!collidesWithGridOrWall(new_piece, piece_x, piece_y) && rotateMoov){
                piece = new_piece;
                rotateMoov = false;
            }


        }

        void handlePieceKeys() {
            if (!gameType.isPlayer()) return;

            if (touched(Assets.gameScreenDrop)){
               dropDownPiece();

            }

            if (touched(Assets.gameScreenLeft)){
                if(!collidesWithGridOrWall(piece, piece_x - 1, piece_y)){
                    piece_x -= 1;
                }
            }

            if (touched(Assets.gameScreenRight)){
                if(!collidesWithGridOrWall(piece, piece_x + 1, piece_y)){
                    piece_x += 1;
                }
            }

            if (touched(Assets.gameScreenBottom)){
                if(!collidesWithGridOrWall(piece, piece_x, piece_y)){
                    dropPiece(true);
                }
            }

            if (touched(Assets.gameScreenRotRight)){
                int[][] new_piece = GameScreen.this.rotate(true, piece);
                if(!collidesWithGridOrWall(new_piece, piece_x, piece_y)){
                    piece = new_piece;
                }
            }
        }

        void dropPiece(boolean byUser){

            if(!collidesWithGridOrWall(piece, piece_x, piece_y - 1)){
                piece_y = piece_y - 1;

                next_drop = drop_timeout - (byUser ? 0 : next_drop);
            }
            else {
                attachPiece();

                state = new ClearingState();
            }
        }

        void dropDownPiece(){
            boolean colide = false;
            while (piece_y > 1 && !colide ){
                if (colide) break;
                if(collidesWithGridOrWall(piece, piece_x, piece_y - 1)) colide = true;
                for (int x = 0; x < piece.length; x++){
                    for (int y = 0; y < piece.length; y++){
                        if(grid[y][x] != 0){
                            if(piece_y - y < 0 || piece_y - y > GRID_HEIGHT){
                                colide = true;
                            }
                            else if(piece_x + x < 0 || piece_x + x  > GRID_WIDTH ){
                                colide = true;
                            }
                            else if(grid[piece_y - y][piece_x + x] != 0 && piece[y][x]  != 0){
                                colide = true;
                            }
                        }

                    }
                }
                if(!colide) piece_y--;
            }

            attachPiece();
            state = new ClearingState();
        }
    }


    @Override
    public void render(float delta) {



        //Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);

        batch.begin();



        state.draw();
        state.update(delta);

        batch.end();
    }

    int[][] nextPiece;
    int[][] nextNextPiece;

    int[][] nextPiece(){
        return factory.nextPiece();
    }

    int[][] popPiece(){
        int[][] currentPiece = nextPiece;
        nextPiece = nextNextPiece;
        nextNextPiece = nextPiece();

        EventCaller.firePieceSpawnEvent(game, currentPiece, nextPiece, getGrid());

        return currentPiece;
    }

    boolean touched(com.badlogic.gdx.math.Rectangle r){
        // Early exit: Keyboard commands for "testing"
        if (Assets.isKeyDonglePressed(r))
            return true;

        if (!Gdx.input.justTouched())
            return false;

        // If this could possibly be slow, I could move it t...
        // It won't be slow
        cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

        return r.contains(touchPoint.x, touchPoint.y);
    }

    void clearRows(){


        int rowsCleared = 0;

        for(int i = 0; i < GRID_HEIGHT;){
            boolean full = true;

            for(int j = 0; j < GRID_WIDTH; j++) {
                if(grid[i][j] == 0)
                    full = false;
            }

            if(full){
                // Move rows down...
                rows++;
                rowsCleared++;



                for(int k = i; k < GRID_HEIGHT - 1; k++){
                    grid[k] = grid[k + 1];
                }

                grid[GRID_HEIGHT - 1] = new int[GRID_WIDTH];
            }
            else {
                i++;
            }

        }
        EventCaller.fireLineClearedEvent(game, nextPiece, nextNextPiece, grid);
        score += rowsCleared * rowsCleared * 10;
    }

    boolean collidesWithGridOrWall(int[][] p, int p_x, int p_y){
        // i is p row, j is p column
        for(int i = 0; i < p.length; i++){
            for(int j = 0; j < p.length; j++){
                if(p[i][j] != 0){
                    int b_x = p_x + j;
                    int b_y = p_y - i;

                    if(b_y < 0 || b_y >= GRID_HEIGHT ||
                            b_x < 0 || b_x >= GRID_WIDTH ||
                            grid[b_y][b_x] !=0)
                        return true;
                }
            }
        }

        return false;
    }

    void drawGame(){
        batch.setColor(Color.WHITE);
        batch.draw(mm_sprite,  0,  0);

        batch.draw(left_sprite, 332,35);
        batch.draw(right_sprite, 512,35);
        batch.draw(down_sprite, 422,35);
        batch.draw(space_sprite, 332, 125);
        batch.draw(rotate_sprite, 512,125);


        // calls setColor. Need to disentangle this eventually.
        drawBlocks(grid, Assets.gameScreenGrid, 0, 0, false, true);

        drawBlocks(nextPiece, Assets.gameScreenPreview, 0, 4, true, false);

        drawBlocks(nextNextPiece, Assets.gameScreenPreview2, 0, 4, true, false);
        //batch.setColor(Color.CLEAR);
        // GWT doesn't support normal string formatting. Rather than get into a mess, don't use string formatting...
        int s = score;
        for(int i = 0; i < 6; i++){
            int d = s % 10;
            Assets.font.draw(batch, Integer.toString(d), 750f, (260.0f + i * 40.0f));
            s = s / 10;
        }
    }

    void drawBlocks(int[][] blocks, Rectangle grid, int x, int y, boolean flip_y, boolean drawLines){
        // flip_y because the grid is stored upside down from the pieces.
        // This is not really a good thing.

        // Note: The Rectangles we're passed in as grid will follow the convention
        // that their width and height will be of a standard block for that display area

        Matrix4 grid_scale = new Matrix4();
        grid_scale.setToTranslationAndScaling(grid.x, grid.y, 0.0f, grid.width / BLOCK_SIZE, grid.height / BLOCK_SIZE, 1.0f);

        batch.setTransformMatrix(grid_scale);


        for(int i = 0; i < blocks.length; i++){
            for(int j = 0; j < blocks[i].length; j++){
                int actual_i = flip_y ? -i : i;
                if (y + actual_i < GRID_HEIGHT - 2){
                    if(blocks[i][j] != 0){
                        batch.setColor(Assets.getBloksColor(blocks[i][j]));
                        batch.draw(
                                block_sprite,
                                BLOCK_SIZE * (x + j),
                                BLOCK_SIZE * (y + actual_i));
                    }
                    batch.setColor(Color.WHITE);
                    if (drawLines)batch.draw(grid_sprite, BLOCK_SIZE * (x + j), BLOCK_SIZE * (y + actual_i));
                }

            }
        }

        batch.setTransformMatrix(new Matrix4().idt());
    }

    public static int[][] rotate(boolean right, int[][] piece){
        // Assume that piece is a square (not ragged or rectangular)
        int[][] new_piece = new int[piece.length][piece[0].length];

        for(int i = 0; i < piece.length; i++){
            for(int j = 0; j < piece.length; j++){
                new_piece[right ? j : piece.length - 1 - j][right ? piece.length - 1 - i : i] = piece[i][j];
            }
        }

        return new_piece;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }







}
