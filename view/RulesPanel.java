package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class RulesPanel  extends JPanel {

	/**
	 * <p>A custom JPanel which contains the rules of the game and a 
	 * JComboBox which allows user to change between night mode and 
	 * day mode..</p>
	 * 
	 * <p>Date of last modification: 27/11/2015.</p>
	 * 
	 * @author Csaba Farkas csaba.farkas@mycit.ie
	 */
	
	//Field variables
	private boolean isDarkColor;
	private JComboBox<String> colorSelectBox;
	private ResourceBundle bundle;
	private String[] lines;
	
	public RulesPanel() {
		super();
		this.bundle = ResourceBundle.getBundle("view.rulesPanelProps");
		
		//I removed the layout manager so I could use absolute positions to move
		//JComboBox to the top left corner.
		this.setLayout(null);
		Insets insets = this.getInsets();
		this.isDarkColor = false;
		
		this.colorSelectBox = new JComboBox<>(new String[] {
				bundle.getString("dayMode"), 
				bundle.getString("nightMode")
		});
		Dimension size = this.colorSelectBox.getPreferredSize();
		this.colorSelectBox.setBounds(insets.left, insets.top, (int)size.getWidth(), (int)size.getHeight());
		
		//An actionListener is attached to the JComboBox
		this.colorSelectBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//If user clicks on Day Mode, color is changed to day colors
				if(colorSelectBox.getSelectedIndex() == 0) {
					isDarkColor = false;
					repaint();
				} else {
					//User clicks on Night Mode, color is changed to night colors
					isDarkColor = true;
					repaint();
				}
			}
		});
		this.add(this.colorSelectBox);
		
		//String array includes lines which are drawn
		this.lines = new String[] {
				this.bundle.getString("line1"),	
				this.bundle.getString("line2"),	
				this.bundle.getString("line3"),	
				this.bundle.getString("line4"),	
				this.bundle.getString("line5"),	
				this.bundle.getString("line6")	
			};
	}
	
	/**
	 * <p>Overriden paintComponent method paints the text on the panel and sets
	 * background color.</p>
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g2d);
		
		//Set background color to black and text color to white if "Night Mode" is selected.
		if(this.isDarkColor) {
			this.setBackground(Color.BLACK);
			g2d.setColor(Color.WHITE);
		} else {
			//Set background to white and text color to black if "Day mode" is selected
			this.setBackground(Color.WHITE);
			g2d.setColor(Color.BLACK);
		}
		
		//Draw "RULES" undelined and center aligned.
		String rules = bundle.getString("rules");
		
		g2d.setFont(new Font("Monospaced", Font.BOLD, (int) (this.getHeight()*0.07)));
		int stringWidth = getStringWidth(rules, g2d);
		g2d.drawString(rules, this.getWidth()/2-stringWidth/2, (int) (this.getHeight()*0.25));
		
		g2d.drawLine(this.getWidth()/2-stringWidth/2, (int) (this.getHeight()*0.26), this.getWidth()/2+stringWidth/2, (int) (this.getHeight()*0.26));
		
		//Draw six lines of rules with some line spacing
		g2d.setFont(new Font("Monospaced", Font.PLAIN, (int) (this.getHeight()*0.03)));
		double textOffsetRatio = 0.31;
		
		for(String s : this.lines) {
			stringWidth = getStringWidth(s, g2d);
			g2d.drawString(s, this.getWidth()/2-stringWidth/2, (int) (this.getHeight()*textOffsetRatio));
			textOffsetRatio += 0.06;
		}
	}
	
	/**
	 * <p>Private method which returns an integer value indicating the width of the string in the 
	 * given graphics context.</p>
	 * 
	 * @param string of which width is to be returned.
	 * @param g2d given Graphics2D context
	 * @return the width of the string
	 */
	private int getStringWidth(String string, Graphics2D g2d) {
		return (int) g2d.getFontMetrics().getStringBounds(string, g2d).getWidth();
	}
}
