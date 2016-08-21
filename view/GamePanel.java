package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ResourceBundle;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

import controller.MemoryGameController;

public class GamePanel extends JPanel {
	
	/**
	 * <p>This custom JPanel displays the virtual Simon game with the four buttons.</p>
	 * <p>Date of last modification: 27/11/2015.</p>
	 * 
	 * @author Csaba Farkas csaba.farkas@mycit.ie
	 */
	
	//final static variables to store the different notes played when a button is pressed
	private static final int PIANO = 0;
	private static final int E_NOTE = 64;
	private static final int C_SHARP = 73;
	private static final int A_NOTE = 69;
	private static final int E_NOTE_UP = 76;
	
	//final static variables to store integer related to the buttons.
	private final static int RED_BUTTON = 0;
	private final static int BLUE_BUTTON = 1;
	private final static int YELLOW_BUTTON = 2;
	private final static int GREEN_BUTTON = 3;
	
	//Localization
	private ResourceBundle bundle;
	
	//Field variables
	private Shape outerEllipse;	
	private Area innerEllipse;
	private Area redButton;
	private Area blueButton;
	private Area yellowButton;
	private Area greenButton;
	// private ArrayList<Area> buttons;
	private boolean redButtonIsFlashed;
	private boolean blueButtonIsFlashed;
	private boolean yellowButtonIsFlashed;
	private boolean greenButtonIsFlashed;
	private int colorIndex;
	private ActionListener animate;
	private Timer flashTimer;
	private Graphics2D g2d;
	private Timer flashDelay;
	private boolean isListening;
	private String level;
	private Synthesizer synthesizer;
	private MidiChannel[] channels;
	private final Integer highScoreAtStart;
	private int velocity;
	private boolean isDarkColor;
	
	/**
	 * <p>Constructor method creates an instance of this class and it inherits all the 
	 * functionalities of {@link JPanel} class. Because of the size of the method, I 
	 * used inline commenting to explain what is happening.</p>
	 * 
	 * @param isDarkColor defines the color theme of the panel.
	 */
	public GamePanel(boolean isDarkColor) {
		super();
		
		//I created a variable to store the high score at the start of the game.
		//If high score at the end of the game is higher, than it was at the beginning,
		//Player is presented with a "New Highscore" message at the end of the game.
		this.highScoreAtStart = MemoryGameController.getInstance().getHighScore();
		
		//This boolean value is defining the color theme of the panel.
		//If it is set to true, a dark color theme is used, otherwise light color theme is used.
		this.isDarkColor = isDarkColor;
		
		//Color index is used when flashing the buttons. It starts from 0th button and goes
		//up to the last button.
		this.colorIndex = 0;
		
		//Velocity is the volume of sound. It's set to 80 initially, and when player mutes
		//the game, it is set to 0. It has the same effect as noteOff
		this.velocity = 80;
		
		//I created a Synthesizer object (Thank you Karl) which creates the sound effects
		try {
			this.synthesizer = MidiSystem.getSynthesizer();
			this.synthesizer.open();
		} catch (MidiUnavailableException e1) {
			e1.printStackTrace();
		}
		this.channels = synthesizer.getChannels();
		
		//This is the action listener, which makes the buttons flash. It sets the
		//boolean values paired with each button to false (when they are true) with
		//a half a second delay. I.e. buttons are flashed for half a second each.
		//I used Swing Timer to achieve this effect, because Threads are not safe to
		//use in Swing elements. Swing is running on the so called Event Dispatch 
		//Thread and making this thread "sleep" can freeze the GUI
		this.animate = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(redButtonIsFlashed) {
					redButtonIsFlashed = false;
				} else if (blueButtonIsFlashed) {
					blueButtonIsFlashed = false;
				} else if (yellowButtonIsFlashed) {
					yellowButtonIsFlashed = false;
				} else if (greenButtonIsFlashed) {
					greenButtonIsFlashed = false;
				}
				
				repaint();
			}
			
		};
		
		this.flashTimer = new Timer(500, this.animate);
		
		//Localization
		this.bundle = ResourceBundle.getBundle("view.gamePanelProps");
		
		//Set layout and border
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.GRAY));
		
		//Add a mouse listener anonymous class to this custom panel.
		//It listens for mouse events and triggers different effects.
		this.addMouseListener(new MouseAdapter() {

			//When mouse is pressed, call the doMousePressedCheck method, and pass the 
			//index of the appropriate button to it.
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if(redButton.contains(e.getPoint())) {
					doMousePressedCheck(RED_BUTTON);
					
				} else if (blueButton.contains(e.getPoint())) {
					doMousePressedCheck(BLUE_BUTTON);
					
				} else if (yellowButton.contains(e.getPoint())) {
					doMousePressedCheck(YELLOW_BUTTON);
					
				} else if (greenButton.contains(e.getPoint())) {
					doMousePressedCheck(GREEN_BUTTON);
				} 
			}

			//When isListening is true, the GUI flash a button when it is clicked.
			//It only listens after the color sequence is displayed, and it only 
			//listens as long as the player clicks on the right color. 
			private void doMousePressedCheck(int buttonColor) {
				if(isListening) {
					
					//If player clicks on the right button, the color index is incremented. If it's 
					//equal to the size of the arraylist which stores the color sequence, that it is
					//set to 0, player levels up and the new color sequence is displayed.
					if(MemoryGameController.getInstance().getColorIndex(colorIndex) == buttonColor) {
						MemoryGameController.getInstance().increasePlayerPoint();
						colorIndex++;
						repaint();
						if(colorIndex == MemoryGameController.getInstance().getColorSequenceSize()) {
							colorIndex = 0;
							MemoryGameController.getInstance().levelUp();
							isListening = false;
							flashButtons();
							repaint();
						}
						
					//If player clicks on the wrong button, high score is saved, isListening is set to
					//false, and a JOptionPanel is displayed.
					} else {
						isFlashed(buttonColor);
						MemoryGameController.getInstance().getDataPersistor().write(MemoryGameController.getInstance().getHighScore());
						isListening = false;
						Timer gameOverTimer = new Timer(MemoryGameController.getInstance().getDifficulty(), new ActionListener() {


							@Override
							public void actionPerformed(ActionEvent arg0) {

								String message = bundle.getString("gameOver") + "\n" + bundle.getString("totalPoints") + MemoryGameController.getInstance().getPlayerScore();
								
								//I created a string which holds the message that is displayed in the JOptionPane.
								//If player achieved a new high score, then the message is extended with some extra text.
								if(MemoryGameController.getInstance().getHighScore() > highScoreAtStart) {
									message +=  "\n";
									message += bundle.getString("newHighScore");
								} 
								
								//I used an OK_CANCEL JOptionPane to ask the user if he/she wants to exit, or play again.
								//The integer value which is returned by the JOptionPane is used in a condition statement
								//to decide what's the next step. I.e. exit the game, or call the NewPlayerDialog, and offer
								//a new game to the user.
								int answer = JOptionPane.showOptionDialog((Component) MemoryGameController.getInstance().getGuiReference(), message, null, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[] {bundle.getString("exit"), bundle.getString("newGame")}, "default");
								System.out.println("answer: " + JOptionPane.CANCEL_OPTION);
								if(answer == 0) {
									System.exit(1);
								} else {
									new NewPlayerDialog((JFrame) MemoryGameController.getInstance().getGuiReference(), MemoryGameController.getInstance().getPlayer().toString());
								}
							}
							
						});
						
						//The above JOptionPane is only displayed half a second after the game is over.
						//Here I set the timer to no-repeat, and started timer.
						gameOverTimer.setRepeats(false);
						gameOverTimer.start();
						return;
					}
					
					//No matter if player continues the game or this was the last button he/she
					//clicked on during this game, the button is still flashed.
					isFlashed(buttonColor);
				}
			}
		});
	}
	
	/**
	 * <p>This is the method where I the custom painting is done. Shapes are created and converted to
	 * areas, strings are drawn etc. It is also a large method so I commented the most important 
	 * steps inside the method.</p>
	 * 
	 * @param Graphics object g.
	 */
	@Override
	public void paintComponent(Graphics g) {
		this.g2d = (Graphics2D) g;
		super.paintComponent(g);
		
		//Set background depending on what the color theme is set to
		if(this.isDarkColor) {
			this.setBackground(Color.GRAY);
		} else {
			this.setBackground(Color.WHITE);
		}
		
		//Create outer and inner ellipse shapes and transform them into Area objects
		Shape outerEllipseShape = new Ellipse2D.Double(this.getWidth()/2-this.getHeight()/2 + 50, 50, this.getHeight()-100, this.getHeight()-100);
		this.outerEllipse = new Area(outerEllipseShape);
		Shape innerEllipseShape = new Ellipse2D.Double(this.getWidth()/2-this.getHeight()/2 + 200, 200, this.getHeight()-400, this.getHeight()-400);
		this.innerEllipse = new Area(innerEllipseShape);
		
		//Create a Shape forming the red button and convert it to an area
		//Then subtract the area of the inner ellipse from the area of the red button
		Shape redButtonShape = new Arc2D.Double(this.getWidth()/2-this.getHeight()/2 + 80, 80, this.getHeight()-200, this.getHeight()-200, 90, 90, Arc2D.PIE);
		this.redButton = new Area(redButtonShape);
		this.redButton.subtract(this.innerEllipse);
		
		//Same procedure for all the other buttons.
		//I tried to use a for loop here, but for some reason
		//it kept giving me a compiler error. So the fours steps of the button
		//creation seems to be code repeating, but I couldn't find any other way
		//to do it.
		Shape blueButtonShape = new Arc2D.Double(this.getWidth()/2-this.getHeight()/2 + 120, 80, this.getHeight()-200, this.getHeight()-200, 0, 90, Arc2D.PIE);
		this.blueButton = new Area(blueButtonShape);
		this.blueButton.subtract(this.innerEllipse);
		
		Shape yellowButtonShape = new Arc2D.Double(this.getWidth()/2-this.getHeight()/2 + 120, 120, this.getHeight() - 200, this.getHeight() - 200, 270, 90, Arc2D.PIE);
		this.yellowButton = new Area(yellowButtonShape);
		this.yellowButton.subtract(this.innerEllipse);
		
		Shape greenButtonShape = new Arc2D.Double(this.getWidth()/2-this.getHeight()/2 + 80, 120, this.getHeight()-200, this.getHeight()-200, 180, 90, Arc2D.PIE);
		this.greenButton = new Area(greenButtonShape);
		this.greenButton.subtract(this.innerEllipse);
		
		//Draw outer black circle
		g2d.setColor(Color.BLACK);
		g2d.fill(outerEllipse);
		
		//Draw red button
		if(this.redButtonIsFlashed) {
			//If this button is flashed, playe the note associated with
			//this button, and change it's color to white. Also, start
			//flashTimer, so the screen is repeated in half a second and
			//button becomes red again.
			this.channels[PIANO].noteOn(A_NOTE, velocity);
			g2d.setColor(Color.WHITE);
			g2d.fill(this.redButton);
			this.flashTimer.setRepeats(false);
			this.flashTimer.start();
		} else {
			g2d.setColor(Color.RED);
			g2d.fill(this.redButton);
		}
		
		//Draw blue button with the same procedure as the red button
		if(this.blueButtonIsFlashed) {
			this.channels[PIANO].noteOn(E_NOTE, velocity);
			g2d.setColor(Color.WHITE);
			g2d.fill(this.blueButton);
			this.flashTimer.setRepeats(false);
			this.flashTimer.start();
		} else {
			g2d.setColor(Color.BLUE);
			g2d.fill(this.blueButton);
		}
		
		//Draw yellow button with same logic
		if(this.yellowButtonIsFlashed) {
			this.channels[PIANO].noteOn(C_SHARP, velocity);
			g2d.setColor(Color.WHITE);
			g2d.fill(this.yellowButton);
			this.flashTimer.setRepeats(false);
			this.flashTimer.start();
		} else {
			g2d.setColor(Color.YELLOW);
			g2d.fill(this.yellowButton);
		}
		
		//Draw green button with same logic
		if(this.greenButtonIsFlashed) {
			this.channels[PIANO].noteOn(E_NOTE_UP, velocity);
			g2d.setColor(Color.WHITE);
			g2d.fill(this.greenButton);
			this.flashTimer.setRepeats(false);
			this.flashTimer.start();
		} else {
			g2d.setColor(Color.GREEN);
			g2d.fill(this.greenButton);
		}
		
		//Draw inner circle
		g2d.setColor(Color.BLACK);
		g2d.fill(this.innerEllipse);
		
		//Draw Level 'n' text to the middle of the screen.
		//I used the width of the string to position it in the middle.
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Monospaced", Font.PLAIN, (int) (this.getHeight()*0.04)));
		this.level = this.bundle.getString("levelText") + MemoryGameController.getInstance().getGameStage();
		int levelWidth = (int) g2d.getFontMetrics().getStringBounds(level, g2d).getWidth();
		int levelHeight = (int) g2d.getFontMetrics().getStringBounds(level, g2d).getHeight();
		g2d.drawString(level, this.getWidth()/2 - levelWidth/2, this.getHeight()/2 + levelHeight/3);
		
		//Draw Player's name and total score and high score to the top right corner.
		//High score is updated every time a new high score is achieved.
		g2d.setColor(Color.BLACK);
		String player = MemoryGameController.getInstance().getPlayer().toString();
		g2d.drawString(player.toUpperCase(), (int)(this.getWidth() - this.getHeight() * 0.35), 50);
		
		String total = this.bundle.getString("totalPoints") + MemoryGameController.getInstance().getGame().getPlayer().getCurrentScore();
		g2d.drawString(total, (int)(this.getWidth() - this.getHeight() * 0.35), 80);
		
		String highScore = this.bundle.getString("highScore") + MemoryGameController.getInstance().getHighScore();
		g2d.drawString(highScore, (int)(this.getWidth() - this.getHeight() * 0.35), 110);
	}
	
	/**
	 * <p>At the start of each level, this method is called to display the color sequence.</p>
	 * <p>I used swing timer here as well. Delay is set based on the difficulty of the game.</p>
	 * <p>The more difficult the game is, the faster the color sequence is shown. To flash buttons,
	 * it also calls the isFlashed method, which calls a repaint, painting the flashed button to 
	 * white.</p>
	 */
	protected void flashButtons() {
		
		this.flashDelay = new Timer(MemoryGameController.getInstance().getDifficulty(), new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(MemoryGameController.getInstance().getColorIndex(colorIndex)) {
				case RED_BUTTON:
					isFlashed(RED_BUTTON);
					break;
				case BLUE_BUTTON:
					isFlashed(BLUE_BUTTON);
					break;
				case YELLOW_BUTTON:
					isFlashed(YELLOW_BUTTON);
					break;
				case GREEN_BUTTON:
					isFlashed(GREEN_BUTTON);
					break;
				}
				colorIndex++;
				
				//To let the program know when the color sequence has finished, I created this
				//if statement. It compares the colorIndex with the size of the color sequence.
				//If they are equal, colorIndex is reinitialized, timer is stopped and isListening
				//is set true i.e. program is waiting for user to click on the buttons.
				if(colorIndex == MemoryGameController.getInstance().getColorSequenceSize()) {
					colorIndex = 0;
					flashDelay.stop();
					isListening = true;
				}
			}
		});
		
		//Start the timer
		this.flashDelay.start();
	}
	
	/**
	 * <p>Mutator method which sets isDarkColor to the value of the parameter.</p>
	 * <p>Used user is changing the color of the UI.</p>
	 * @param isDarkColor
	 */
	public void setIsDarkColor(boolean isDarkColor) {
		this.isDarkColor = isDarkColor;
	}

	/**
	 * <p>Mutator method which sets the velocity to the value of the parameter. This can
	 * be either 0 or 80 --> mute or with sound.</p>
	 * 
	 * @param velocity is an integer value.
	 */
	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}
	
	/**
	 * <p>This private method triggers each button flash. It selects the boolean
	 * parameter associated with the button indicated by the parameter to true, and
	 * calls a repaint. The rest of it is handled by the methods above.</p>
	 * 
	 * @param buttonIndex indicates the button to be flashed.
	 */
	private void isFlashed(int buttonIndex) {
		switch(buttonIndex) {
		case RED_BUTTON:
			this.redButtonIsFlashed = true;
			break;
		case BLUE_BUTTON:
			this.blueButtonIsFlashed = true;
			break;
		case YELLOW_BUTTON:
			this.yellowButtonIsFlashed = true;
			break;
		case GREEN_BUTTON:
			this.greenButtonIsFlashed = true;
			break;
		}
		repaint();
	}
}
