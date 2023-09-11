package fr.simonlou.testrisbot;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import fr.simonlou.testrisbot.bot.Player.Moov;
import fr.simonlou.testrisbot.listeners.EventListener;
import fr.simonlou.testrisbot.listeners.KeyListeners;
import fr.simonlou.testrisbot.screens.MainMenuScreen;
import fr.simonlou.testrisbot.tetrisevents.EventCaller;
import fr.simonlou.testrisbot.tetrisevents.EventList;
import fr.simonlou.testrisbot.utils.Assets;

import java.util.ArrayList;
import java.util.List;

public class Game extends com.badlogic.gdx.Game {

	//Event
	public List<EventList.GameStartedEvent> gameStartedEventListeners;

	public List<EventList.PieceSpawnEvent> pieceSpawnEventsListeners;

	public List<EventList.LineClearedEvent> lineClearedEvents;
	public List<EventList.NextTickEvent> nextTickEvents;
	public List<Integer> keyPressed;
	public List<Moov> AIMoovs;

	public SpriteBatch batch;
	public BitmapFont font;
	private KeyListeners keyListeners;

	//Class
	EventCaller eventCaller;
	MainMenuScreen mainMenuScreen;

	private void registerVars(){
		gameStartedEventListeners = new ArrayList<>();
		pieceSpawnEventsListeners = new ArrayList<>();
		lineClearedEvents = new ArrayList<>();
		nextTickEvents = new ArrayList<>();
		keyPressed = new ArrayList<>();
		AIMoovs = new ArrayList<>();

	}
	private void registerClass(){

		eventCaller = new EventCaller();
		keyListeners = new KeyListeners(this);
	}
	private void registerEvent(){
		gameStartedEventListeners.add(new EventListener(this));
		pieceSpawnEventsListeners.add(new EventListener(this));
		lineClearedEvents.add(new EventListener(this));
		nextTickEvents.add(new EventListener(this));
	}

	private void registerObject(){
		batch = new SpriteBatch();
		font = new BitmapFont();
		mainMenuScreen = new MainMenuScreen(this);
	}
	
	@Override
	public void create () {
		Assets.load();

		this.registerVars();
		this.registerClass();
		this.registerEvent();
		this.registerObject();


		Gdx.input.setInputProcessor(keyListeners);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);



		this.setScreen(mainMenuScreen);
	}

	@Override
	public void render () {
		super.render();
			}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}
