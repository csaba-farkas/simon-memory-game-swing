package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;

import controller.MemoryGameController;
import interfaces.IMemoryGameGui;
import model.Game;

public class MemoryGameFrame extends JFrame implements IMemoryGameGui{
	
	/**
	 * <p>This class is defines the frame of the GUI. It extends {@link JFrame} and inherits all of
	 * JFrames functionalities. It also implements {@link IMemoryGameGui} with its method.</p>
	 * 
	 * <p>Date of last modification: 27/11/2015.</p>
	 * 
	 * @author Csaba Farkas csaba.farkas@mycit.ie
	 */
	
	//Field variables
	private GamePanel gamePanel;
	private RulesPanel rulesPanel;
	private HistoryPanel historyPanel;
	private JMenu difficultyMenu;
	private JRadioButtonMenuItem easySelect;
	private JRadioButtonMenuItem mediumSelect;
	private JRadioButtonMenuItem hardSelect;
	private JMenu soundMenu;
	private ResourceBundle bundle;
	private OptionsPanel optionsPanel;

	/**
	 * <p>Constructor method which creates an instance of this class. Constructor initializes most of the 
	 * field variables and calls a function which creates the menu bar.</p>
	 * 
	 * @param title defines the title of the frame.
	 */
	public MemoryGameFrame(String title) {
		super(title);
		this.bundle = ResourceBundle.getBundle("view.gameFrameProps");
		
		//Create JTabbedPanel and 3 JPanels
		JTabbedPane jTabbedPane = new JTabbedPane();
		this.rulesPanel = new RulesPanel();
		this.historyPanel = new HistoryPanel();
		
		//Add custom panels to JTabbedPane
		this.optionsPanel = new OptionsPanel(this, true);
		jTabbedPane.addTab(this.bundle.getString("game"), this.optionsPanel);
		jTabbedPane.addTab(this.bundle.getString("rules"), this.rulesPanel);
		jTabbedPane.addTab(this.bundle.getString("history"), this.historyPanel);
		
		//Add JTabbedPane to ContentPane
		this.getContentPane().add(jTabbedPane);
		
		//Add menu bar to frame
		this.setJMenuBar(createMenuBar());
		
		//Set size of the frame
		this.setMinimumSize(new Dimension(800, 600));
		
		//Get the size of the screen and position frame in the middle of the screen
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();      
        int frameWidth = this.getSize().width;
        int frameHeigth = this.getSize().height;
        
        //X coordinate = (width of screen - width of frame) / 2
        int locationX = (dimension.width - frameWidth)/2; 
        //Y coordinate = (height of screen - height of frame) / 2
        int locationY = (dimension.height - frameHeigth)/2;              
        this.setLocation(locationX, locationY);
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	/**
	 * <p>This method creates and returns a {@link JMenuBar} object, which is
	 * added to the frame.</p>
	 * 
	 * @return a JMenuBar object
	 */
	private JMenuBar createMenuBar() {
		
		//Create menubar
		JMenuBar jMenuBar = new JMenuBar();
		
		//Create file menu
		JMenu jMenuFile = new JMenu(this.bundle.getString("file"));
		jMenuFile.setMnemonic(KeyEvent.VK_F);
		
		//Create menu items in file menu
		
		//Create "New Game" menu item, attach key event, attach action listener
		JMenuItem fileNewGame = new JMenuItem(this.bundle.getString("newGame"));
		fileNewGame.setMnemonic(KeyEvent.VK_N);
		fileNewGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new NewPlayerDialog(MemoryGameFrame.this, "");
			}
			
		});
		
		//Create "Exit" menu item, attach key event, and attach action listener.
		JMenuItem fileExit = new JMenuItem(this.bundle.getString("exit"));
		fileExit.setMnemonic(KeyEvent.VK_E);
		fileExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(2);
				
			}
		});
		
		//Add menu items to file menu
		jMenuFile.add(fileNewGame);
		jMenuFile.addSeparator();
		jMenuFile.add(fileExit);
		
		//Add file menu to menu bar
		jMenuBar.add(jMenuFile);
		
		//Create options menu
		JMenu jMenuOptions = new JMenu(this.bundle.getString("options"));
		jMenuOptions.setMnemonic(KeyEvent.VK_O);
		
		//Create menu items
		this.difficultyMenu = new JMenu(this.bundle.getString("difficulty"));
		this.difficultyMenu.setMnemonic(KeyEvent.VK_D);
		this.difficultyMenu.setEnabled(false);
		JMenu uiMenu = new JMenu(this.bundle.getString("ui_options"));
		uiMenu.setMnemonic(KeyEvent.VK_U);
		
		//Add them to jMenuOptions
		jMenuOptions.add(this.difficultyMenu);
		jMenuOptions.add(uiMenu);
		
		//Create submenus
		//Submenu which allows player to set the difficulty of the game during the game.
		//I created a buttongroup, to which JRadioButtonMenuItems are added, so only one
		//can be selected at a time from the same group.
		ButtonGroup difficultyButtonGroup = new ButtonGroup();
		
		//Option "Easy"
		this.easySelect = new JRadioButtonMenuItem(this.bundle.getString("easy"));
		this.easySelect.setMnemonic(KeyEvent.VK_E);
		//An action listener calls the controller, when this is selected, to change the difficulty of the game.
		this.easySelect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MemoryGameController.getInstance().setDifficulty(MemoryGameController.EASY_DIFFICULTY);
			}
		});
		
		difficultyButtonGroup.add(this.easySelect);
		
		//Option "Medium"
		this.mediumSelect = new JRadioButtonMenuItem(this.bundle.getString("medium"));
		this.mediumSelect.setMnemonic(KeyEvent.VK_M);
		this.mediumSelect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MemoryGameController.getInstance().setDifficulty(MemoryGameController.MEDIUM_DIFFICULTY);
			}
		});
		
		difficultyButtonGroup.add(this.mediumSelect);
		
		//Option "Hard"
		this.hardSelect = new JRadioButtonMenuItem(this.bundle.getString("hard"));
		this.hardSelect.setMnemonic(KeyEvent.VK_H);
		this.hardSelect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MemoryGameController.getInstance().setDifficulty(MemoryGameController.HARD_DIFFICULTY);
			}
		});
		
		difficultyButtonGroup.add(this.hardSelect);
		
		//Create color menu (user can select a light or dark theme) and sound menu (sound on or off)
		JMenu colorMenu = new JMenu(this.bundle.getString("theme"));
		colorMenu.setMnemonic(KeyEvent.VK_T);
		this.soundMenu = new JMenu(this.bundle.getString("sound"));
		this.soundMenu.setEnabled(false);
		this.soundMenu.setMnemonic(KeyEvent.VK_S);
		
		//Create a button group for the two color JRadioButtonMenuItems
		ButtonGroup colorButtonGroup = new ButtonGroup();
		
		//Create JRadioButtonMenuItems and add them to the button group.
		JRadioButtonMenuItem lightColorSelect = new JRadioButtonMenuItem(this.bundle.getString("light"));
		lightColorSelect.setMnemonic(KeyEvent.VK_L);
		lightColorSelect.addActionListener(new ActionListener() {
			
			//Action listener calls the functions of OptionsPanel and GamePanel to change the colors.
			@Override
			public void actionPerformed(ActionEvent e) {
				optionsPanel.setIsDarkColor(false);
				if(gamePanel != null) {
					gamePanel.setIsDarkColor(false);
					gamePanel.repaint();
				}
				optionsPanel.repaint();
			}
		});
		colorButtonGroup.add(lightColorSelect);
		
		//Same procedure for the other button which lets user to change the colors to a dark theme
		JRadioButtonMenuItem darkColorSelect = new JRadioButtonMenuItem(this.bundle.getString("dark"));
		darkColorSelect.setSelected(true);
		darkColorSelect.setMnemonic(KeyEvent.VK_D);
		darkColorSelect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				optionsPanel.setIsDarkColor(true);
				if(gamePanel != null) {
					gamePanel.setIsDarkColor(true);
					gamePanel.repaint();
				}
				optionsPanel.repaint();
				
			}
		});
		colorButtonGroup.add(darkColorSelect);
		
		//A new button group which holds the JRadioButtonMenuItems for sound options
		ButtonGroup soundButtonGroup = new ButtonGroup();
		JRadioButtonMenuItem soundOnSelect = new JRadioButtonMenuItem(this.bundle.getString("on"));
		soundOnSelect.setMnemonic(KeyEvent.VK_O);
		soundOnSelect.setSelected(true);
		soundOnSelect.addActionListener(new ActionListener() {
			
			//When selected --> sound is on and velocity is set to 80 in gamePanel
			@Override
			public void actionPerformed(ActionEvent e) {
				gamePanel.setVelocity(80);
				
			}
		});
		soundButtonGroup.add(soundOnSelect);
		
		JRadioButtonMenuItem soundOffSelect = new JRadioButtonMenuItem(this.bundle.getString("off"));
		soundOffSelect.setMnemonic(KeyEvent.VK_F);
		soundOffSelect.addActionListener(new ActionListener() {
			
			//When selected --> sound is off and velocity is set to 0 in gamePanel
			@Override
			public void actionPerformed(ActionEvent e) {
				gamePanel.setVelocity(0);
				
			}
		});
		soundButtonGroup.add(soundOffSelect);
		
		//Add everything to the right place
		this.difficultyMenu.add(this.easySelect);
		this.difficultyMenu.add(this.mediumSelect);
		this.difficultyMenu.add(this.hardSelect);
		
		colorMenu.add(lightColorSelect);
		colorMenu.add(darkColorSelect);
		
		this.soundMenu.add(soundOnSelect);
		this.soundMenu.add(soundOffSelect);
		
		uiMenu.add(colorMenu);
		uiMenu.add(this.soundMenu);
		
		jMenuOptions.add(this.difficultyMenu);
		jMenuOptions.add(uiMenu);
		
		jMenuBar.add(jMenuOptions);
		
		//Return menu bar
		return jMenuBar;

	}

	/**
	 * <p>Implemented {@link IMemoryGameGui} method. It does the following:
	 * <ul>
	 * <li>Enables difficulty menu so player can change difficulty during the game</li>
	 * <li>Creates a new game based using the value of difficulty returned by controller</li>
	 * <li>Sets the color theme of the gamePanel based on the color theme of optionsPanel</li>
	 * <li>Removes everything from the tab , adds gamePanel, revalidates and repaints. In other
	 * words gamePanel replaces optionsPanel.</li>
	 * <li>It calls the flashButttons() method of {@link Game} so the first sequence of buttons 
	 * are flashed.</li>
	 * </ul>
	 * </p>
	 */
	@Override
	public void runGame() {
		//Set difficulty in menu
		this.difficultyMenu.setEnabled(true);
		this.soundMenu.setEnabled(true);
		int difficulty = MemoryGameController.getInstance().getDifficulty();
		if(difficulty == MemoryGameController.EASY_DIFFICULTY) {
			this.easySelect.setSelected(true);
		} else if(difficulty == MemoryGameController.MEDIUM_DIFFICULTY) {
			this.mediumSelect.setSelected(true);
		} else {
			this.hardSelect.setSelected(true);
		}
		this.gamePanel = new GamePanel(optionsPanel.getIsDarkColor());
		this.getContentPane().removeAll();
		this.getContentPane().add(this.gamePanel);
		this.getContentPane().revalidate();
		this.getContentPane().repaint();
		this.gamePanel.flashButtons();
		
	}

}
