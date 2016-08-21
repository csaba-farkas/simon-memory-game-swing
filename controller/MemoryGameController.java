package controller;

import interfaces.IDataPersistor;
import interfaces.IMemoryGameGui;
import model.Game;
import model.Player;
import model.Stage;

public class MemoryGameController {
	
	/**
	 * <p>Controller class which connects classes of model package with
	 * classes of view package.</p>
	 * <p>Date of last modification: 27-11/2015</p>
	 * 
	 * @author Csaba Farkas csaba.farkas@mycit.ie
	 */
	
	//Singleton class
	private static MemoryGameController instance = null;
	
	//static final variables are used to define the difficulty of the game
	//eg. they define the delay the buttons are flashed
	//The more complicated the game, the faster the buttons flash.
	public static final int EASY_DIFFICULTY = 2000;
	public static final int MEDIUM_DIFFICULTY = 1500;
	public static final int HARD_DIFFICULTY = 1000;
	
	/**
	 * <p>Static function which returns the only one instance of this class.</p>
	 * <p>When the program is started, instance = null, so it creates a new
	 * instance of this class, before returning.</p>
	 * 
	 * @return the instance of {@link MemoryGameController}
	 */
	public static MemoryGameController getInstance() {
		
		if(instance == null) {
			instance = new MemoryGameController();
		}
		
		return instance;
	}
	
	//Field variables
	private Player player;					//Player object of game
	private Game game;						//Game object
	private IMemoryGameGui gui;				//Instance of gui object which implements IMemoryGameGui
	private int difficulty;					//int variable indicating the difficulty of the game
	private Integer highScore;				//Current high score
	private IDataPersistor dataPersistor;	//Instance of data persistor object which implements IDataPersistor
	private boolean isSoundOn;				//Boolan variable indicating if game is muted or not
	
	/**
	 * <p>Accessor method returns the {@link Game} object stored in 
	 * game field variable.</p>
	 * 
	 * @return {@link Game} object.
	 */
	public Game getGame() {
		return this.game;
	}
	
	/**
	 * <p>Mutator method sets difficulty to the value which is passed to the method.</p>
	 * 
	 * @param difficulty indicates the difficulty of the game.
	 */
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	/**
	 * <p>This method creates a new game and sets it to the difficulty level indicated
	 * by the integer parameter. Then it assigns the new {@link Game} object to game field
	 * variable. It also sets the high score of the new game object to highScore field 
	 * variable.</p>
	 * 
	 * @param difficulty indicates the difficulty of the new game.
	 */
	public void createNewGame(int difficulty) {
		this.difficulty = difficulty;
		this.game = new Game(this.player, this.difficulty);
		this.game.setHighScore(this.highScore);
	}
	
	/**
	 * <p>Mutator method which sets the highScore field variable to the Integer
	 * object parameter which is passed to the method.</p>
	 * <p>If user runs the program at the first time, there is no data stored in
	 * the serialized file, so it sets the value of highScore to 0.</p>
	 * 
	 * @param highScore indicates the current high score.
	 */
	public void setHighScore(Integer highScore) {
		if(highScore != null) {
			this.highScore = highScore;
		} else {
			this.highScore = 0;
		}
	}
	
	/**
	 * <p>Accessor method which returns the current high score.</p>
	 * 
	 * @return high score.
	 */
	public Integer getHighScore() {
		return this.highScore;
	}
	
	/**
	 * <p>Method which creates a new {@link Player} object. It sets the name 
	 * of the new player to the value of the String object parameter.</p>
	 * 
	 * @param playerName indicates the name of the new {@link Player}.
	 */
	public void createPlayer(String playerName) {
		this.player = new Player(playerName);
	}
	
	/**
	 * <p>Accessor method which returns the {@link Player} object assigned to 
	 * player field variable.</p>
	 * 
	 * @return current {@link Player} object.
	 */
	public Player getPlayer() {
		return this.player;
	}
	
	/**
	 * <p>Accessor method which returns an integer value indicating the difficulty 
	 * of the current game.</p>
	 * 
	 * @return an integer value indicating difficulty.
	 */
	public int getDifficulty() {
		return this.difficulty;
	}
	
	/**
	 * <p>One of the most important methods, which connects the GUI to the program. It 
	 * sets the {@link IMemoryGameGui} field variable to the parameter.</p>
	 * 
	 * @param gui defines a class which implements {@link IMemoryGameGui} interface.
	 */
	public void setGuiReference(IMemoryGameGui gui) {
		this.gui = gui;
	}

	/**
	 * <p>Accessor method which returns the current gui reference object.</p>
	 * 
	 * @return the current gui object.
	 */
	public IMemoryGameGui getGuiReference() {
		return this.gui;
	}
	
	/**
	 * <p>Mutator method which sets the data persistor of the program to the {@link IDataPersistor} 
	 * parameter.</p>
	 * 
	 * @param dataPersistor defines a class which implements {@link IDataPersistor} interface.
	 */
	public void setDataPersistor(IDataPersistor dataPersistor) {
		this.dataPersistor = dataPersistor;
	}
	
	/**
	 * <p>Accessor method which returns the current data persistor object.</p>
	 * 
	 * @return current data persistor.
	 */
	public IDataPersistor getDataPersistor() {
		return this.dataPersistor;
	}
	
	/**
	 * <p>Accessor method which returns level number of the current game.</p>
	 * 
	 * @return level number. Eg. 1, 2, 3...
	 */
	public int getGameStage() {
		return this.game.getStage().getLevelNumber();
	}
	
	/**
	 * <p>Accessor method which returns the current color index of the current
	 * stage of the game. In the color sequence of the the {@link Stage} class
	 * colors are represented as numbers. That's why this method is returning an
	 * integer value.</p>
	 * 
	 * @param index defines the index number of the element in the color sequence.
	 * @return an integer indicating a color.
	 */
	public int getColorIndex(int index) {
		return this.game.getStage().getColorSequence().get(index);
	}
	
	/**
	 * <p>This method is called when player is clicking on the right button during the 
	 * game. Player is awarded with a point. It also checks whether current score is 
	 * higher than high score, and changes the value of high score to current score if 
	 * it is. It also writes high score to the serialized data file so no extra saving 
	 * is required from the user.</p>
	 */
	public void increasePlayerPoint() {
		this.game.getPlayer().addPointForButton();
		if(this.highScore < this.player.getCurrentScore()) {
			this.highScore = this.player.getCurrentScore();
			this.dataPersistor.write(highScore);
			System.out.println("High score: " + this.highScore);
		}
	}
	
	/**
	 * <p>Accessor method which returns the size of the color sequence of current 
	 * stage of the game.</p>
	 * 
	 * @return an integer value indicating the size of color sequence list.
	 */
	public int getColorSequenceSize() {
		return this.game.getStage().getColorSequence().size();
	}
	
	/**
	 * <p>This function is called when player repeated the color sequence in the right
	 * order. It calls the levelUp() function of {@link Stage}.</p>
	 */
	public void levelUp() {
		this.game.getStage().levelUp();
	}

	/**
	 * <p>Accessor method which returns the current score of the player.</p>
	 * 
	 * @return an integer indicating the current score.
	 */
	public int getPlayerScore() {
		return this.player.getCurrentScore();
	}
	
	/**
	 * <p>Mutator method which sets the isSoundOn field variable to the boolean parameter. 
	 * This method triggers turning off or on the sound in the game.</p>
	 * 
	 * @param isSoundOn a boolean value.
	 */
	public void setIsSoundOn(boolean isSoundOn) {
		this.isSoundOn = isSoundOn;
	}
	
	/**
	 * <p>Accessor method which returns the current value of isSoundOn field variable.</p>
	 * 
	 * @return
	 */
	public boolean getIsSoundOn() {
		return this.isSoundOn;
	}

}
