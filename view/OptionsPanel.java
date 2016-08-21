package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import controller.MemoryGameController;

public class OptionsPanel extends JPanel {
	
	/**
	 * <p>A custom JPanel which is displayed when the game is started. It contains to 
	 * custom buttons (New Game, and Exit) and a title.</p>
	 * 
	 * <p>Date of last modification: 27/11/2015.</p>
	 * 
	 * @author Csaba Farkas csaba.farkas@mycit.ie
	 */
	
	private static final long serialVersionUID = -4098926890860876544L;
	
	//final static Color variables used when different color themes are selected.
	private final static Color DARK_BUTTON_BGCOLOR = new Color(39, 36, 49);
	private final static Color DARK_BUTTON_BGCOLOR_ONHOVER = new Color(88, 87, 96);
	private final static Color DARK_LABEL_COLOR_ONHOVER = new Color(28, 25, 40);
	
	private final static Color LIGHT_BUTTON_BGCOLOR = new Color(100,181,246);
	private final static Color LIGHT_BUTTON_BGCOLOR_ONHOVER = new Color(187,222,251);
	private final static Color LIGHT_LABEL_COLOR_ONHOVER = new Color(62,39,35);
	
	//Field variables
	private boolean isDarkColor;
	private boolean newGameIsOnHover;
	private boolean exitIsOnHover;
	private boolean newGameButtonPressed;
	private boolean exitButtonPressed;
	private final JFrame parent;
	private Shape newGameButton;
	private Shape exitButton;
	private ArrayList<String> buttonLabels;
	private ResourceBundle bundle;
	private Color newGameBgColor;
	private Color exitBgColor;
	private Color newGameLabelColor;
	private Color exitLabelColor;
	
	/**
	 * <p>Constructor method which creates an instance of this class.<p>
	 * 
	 * @param parent is the {@link JFrame} in which this custom panel is displayed.
	 * @param isDarkColor is a boolean value indicating the color theme of the panel.
	 */
	public OptionsPanel(final JFrame parent, boolean isDarkColor) {
		super();
		
		//Initialize field variables.
		this.isDarkColor = isDarkColor;
		
		//These boolean values helps to track if use hovers over the buttons with the mouse
		//Or press any button. Buttons are changing their colors when hovering over them, or 
		//when they are pressed.
		this.newGameIsOnHover = false;
		this.exitIsOnHover = false;
		this.newGameButtonPressed = false;
		this.exitButtonPressed = false;
		this.parent = parent;
		
		//Create a border and create the ResourceBundle
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.GRAY));
		this.bundle = ResourceBundle.getBundle("view.optionsPanelProps");
		
		//Create an ArrayList to store the text which is displayed in the two buttons
		this.buttonLabels = new ArrayList<String>();
		this.buttonLabels.add(this.bundle.getString("newGame"));
		this.buttonLabels.add(this.bundle.getString("exit"));
		
		//Add a MouseMotionListener to the panel so it can be detected when user hovers the mouse over a 
		//button.
		this.addMouseMotionListener(new MouseMotionListener() {
			
			//When mouse is moved and it is moved over a button, change the appropriate isHover boolean to true
			//and repaint the screen. When mouse is moved off a button, it is detected by checking if any of the
			//buttons has an "on_hover" color. Then the color is reset by assigning "false" to the onHover booleans
			//and a repaint is called again.
			@Override
			public void mouseMoved(MouseEvent e) {
				if(newGameButton.contains(e.getPoint())) {
					newGameIsOnHover = true;
					repaint();
				} else if(exitButton.contains(e.getPoint())) {
					exitIsOnHover = true;
					repaint();
				} else if(newGameBgColor.equals(DARK_BUTTON_BGCOLOR_ONHOVER) || exitBgColor.equals(DARK_BUTTON_BGCOLOR_ONHOVER)
					   || newGameBgColor.equals(LIGHT_BUTTON_BGCOLOR_ONHOVER) || exitBgColor.equals(LIGHT_BUTTON_BGCOLOR_ONHOVER)) {
					newGameIsOnHover = false;
					exitIsOnHover = false;
			
					repaint();
				}
				
			}

			@Override
			public void mouseDragged(MouseEvent arg0) {
				
			}
		});
		
		//To detect clicks and presses, I also added a mouseListener object to this panel.
		this.addMouseListener(new MouseListener() {

			//When user clicks on a button different things happen:
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//If "New Game" button was clicked, a NewPlayerDialog" called
				if(newGameButton.contains(arg0.getPoint())) {
					new NewPlayerDialog(parent, "");
				} else if(exitButton.contains(arg0.getPoint())) {
					//If player clicks on "Exit", total score is saved (maybe it's not necessary, I just wanted to make sure it's saved)
					//And program exits.
					MemoryGameController.getInstance().getDataPersistor().write(MemoryGameController.getInstance().getHighScore());
					System.exit(0);
				}
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}

			//When mouse is pressed, the color of the buttons are changed to red, so user gets a 
			//feedback he/she really pressed on a button.
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(newGameButton.contains(arg0.getPoint())) {
					newGameButtonPressed = true;
					repaint();
				} else if(exitButton.contains(arg0.getPoint())) {
					exitButtonPressed = true;
					repaint();
				}
				
			}

			//When mouse is released, the previous colors are restored.
			@Override
			public void mouseReleased(MouseEvent arg0) {
				newGameButtonPressed = false;
				exitButtonPressed = false;
				repaint();
			}
			
		});
	}
	
	/**
	 * <p>Overriden paintComponent method. It draws the custome elements on the 
	 * screen. I used inline comments to explain what's happening.</p>
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g);
		
		//Check if color theme is set to dark or light. Use appropriate colors to paint
		//background, buttons and labels in buttons 
		if(this.isDarkColor) {
			this.setBackground(Color.GRAY);
			this.newGameBgColor = DARK_BUTTON_BGCOLOR; 
			this.exitBgColor = DARK_BUTTON_BGCOLOR;
			this.newGameLabelColor = Color.WHITE;
			this.exitLabelColor = Color.WHITE;
		} else {
			this.setBackground(Color.WHITE);
			this.newGameBgColor = LIGHT_BUTTON_BGCOLOR;
			this.exitBgColor = LIGHT_BUTTON_BGCOLOR;
			this.newGameLabelColor = Color.BLACK;
			this.exitLabelColor = Color.BLACK;
		}
		
		//Check if mouse is hovered over "New Game" button, "Exit Button" or 
		//no button, and use different colors
		//if light or dark theme is selected to indicate mouse hover.
		if(this.newGameIsOnHover) {
			if(this.isDarkColor) {
				newGameBgColor = DARK_BUTTON_BGCOLOR_ONHOVER;
				newGameLabelColor = DARK_LABEL_COLOR_ONHOVER;
			} else {
				newGameBgColor = LIGHT_BUTTON_BGCOLOR_ONHOVER;
				newGameLabelColor = LIGHT_LABEL_COLOR_ONHOVER;
			}
		} else if(this.exitIsOnHover) {
			if(this.isDarkColor) {
				exitBgColor = DARK_BUTTON_BGCOLOR_ONHOVER;
				exitLabelColor = DARK_LABEL_COLOR_ONHOVER;
			} else {
				exitBgColor = LIGHT_BUTTON_BGCOLOR_ONHOVER;
				exitLabelColor = LIGHT_LABEL_COLOR_ONHOVER;
			}
		}
		
		//If any of the two buttons is pressed paint them red
		if(this.newGameButtonPressed) {
			newGameBgColor = Color.RED;
		} else if(this.exitButtonPressed) {
			exitBgColor = Color.RED;
		}
		
		//Local variables used to store offset, panel width, text width and height
		int verticalOffset, width, textWidth, textHeight;
		
		verticalOffset = (int) (this.getHeight()*0.2);
		width = this.getWidth();
		
		//Local variables stores the width and the height of each button
		double buttonsWidth = 300;
		double buttonsHeight = 70;
		
		g2d.setFont(new Font("Arial", Font.BOLD, 35));
		textWidth = (int) g2d.getFontMetrics().getStringBounds(this.bundle.getString("gameName"), g2d).getWidth();
		
		//Draw the title of the panel: "SIMON MEMORY GAME". Use white color if dark theme is selected, and
		//use black color if dark theme is selected.
		if(getIsDarkColor()) {
			g2d.setColor(Color.WHITE);
		} else {
			g2d.setColor(Color.BLACK);
		}
		g2d.drawString(this.bundle.getString("gameName"), width/2 - textWidth/2, verticalOffset);

		//variable which calculates the offset between two buttons
		double offsetMultiplier = 0.25;
		verticalOffset = (int)(this.getHeight()*offsetMultiplier);
		
		//The next part is drawing the custom buttons. I used RoundRectangle2Ds for the shape of the buttons and draw strings for their texts.
		//Buttons are positioned in the middle of the screen
		//String are positioned in the center of each button.
		
		//Create and align buttons to the center
		this.newGameButton = new RoundRectangle2D.Double(width/2 - buttonsWidth/2, verticalOffset, buttonsWidth, buttonsHeight, 10.0, 10.0);
		Rectangle2D r2d = this.newGameButton.getBounds2D();
		
		//Get the width and height of the string which is to be drawn into the button
		textWidth = (int) g2d.getFontMetrics().getStringBounds(this.buttonLabels.get(0), g2d).getWidth();
		textHeight = (int) g2d.getFontMetrics().getStringBounds(this.buttonLabels.get(0), g2d).getHeight();
		
		//Set color and draw it with "fill" command
		g2d.setColor(this.newGameBgColor);
		g2d.fill(this.newGameButton);
		
		//Increase the vertical offset by the height of the button + 15 pixels --> there is a 15 px gap between two buttons
		verticalOffset += r2d.getHeight() + 15;
		
		//Set color of the text
		g2d.setColor(this.newGameLabelColor);
		
		//Draw text in the middle of the button. To get the x coordinate, I subtracted half the width of the string from half the width of 
		//the screen. To get the Y coordinate, I subtracted the (third of text height subtracted from height of button) from the coordinate of
		//the bottom of the button.
		g2d.drawString(this.buttonLabels.get(0), width/2 - textWidth/2, (int) (r2d.getY() + r2d.getHeight() - r2d.getHeight()/2 + textHeight/3));
		
		//Exit button is painted in the same way as New Game button was.
		this.exitButton = new RoundRectangle2D.Double(width/2 - buttonsWidth/2, verticalOffset, buttonsWidth, buttonsHeight, 10.0, 10.0);
		r2d = this.exitButton.getBounds2D();
		textWidth = (int) g2d.getFontMetrics().getStringBounds(this.buttonLabels.get(1), g2d).getWidth();
		textHeight = (int) g2d.getFontMetrics().getStringBounds(this.buttonLabels.get(1), g2d).getHeight();
		g2d.setColor(this.exitBgColor);
		g2d.fill(this.exitButton);
		verticalOffset += r2d.getHeight() + 15;
		g2d.setColor(this.exitLabelColor);
		g2d.drawString(this.buttonLabels.get(1), width/2 - textWidth/2, (int) (r2d.getY() + r2d.getHeight() - r2d.getHeight()/2 + textHeight/3));
	}
	
	public void setIsDarkColor(boolean isDarkColor) {
		this.isDarkColor = isDarkColor;
	}
	
	public boolean getIsDarkColor() {
		return this.isDarkColor;
	}
}
