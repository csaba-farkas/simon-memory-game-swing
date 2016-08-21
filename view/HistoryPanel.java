package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class HistoryPanel extends JPanel {

	/**
	 * <p>This is the third JTabbedPane and it contains a few lines about the
	 * history of Simon memory game. I copied the contents of the wiki page of
	 * the game. This class extends to {@link JPanel}</p>
	 * 
	 * <p>Date of last modification: 27/11/2015.</p>
	 * 
	 * @author Csaba Farkas csaba.farkas@mycit.ie
	 */
	
	//final static Font variables. Used in JList, to change the font of the text.
	private final static Font MONOSPACED = new Font("Monospaced", Font.PLAIN, 20);
	private final static Font ARIAL = new Font("Arial", Font.PLAIN, 20);
	private final static Font SANS_SERIF = new Font("SansSerif", Font.PLAIN, 20);
	
	//Field variables
	private ResourceBundle bundle;
	private JTextArea textArea;
	
	/**
	 * <p>Constructor method creates an instance of this class. Object inherits all 
	 * of the functionalities of {@link JPanel}.</p>
	 */
	public HistoryPanel() {
		super();
		
		//Localization
		this.bundle = ResourceBundle.getBundle("view.historyPanelProps");

		//Set layout to BorderLayout
		this.setLayout(new BorderLayout());
		
		//Create JTextArea with text parsed from bundle. Set wrap style to line and word
		//i.e. line breaks are inserted at the end of words.
		//Add some padding, using an empty border and set font type to arial.
		this.textArea = new JTextArea(this.bundle.getString("content"));
		this.textArea.setWrapStyleWord(true);
		this.textArea.setLineWrap(true);
		this.textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.textArea.setFont(ARIAL);
		
		//Create JScrollPane object and add text area to it. Set vertical scrollbar to be
		//displayed only when text is too large to display on the screen. Never displays
		//horizontal scrollbar. Add scrollpane to this panel.
		JScrollPane scrollPane = new JScrollPane(this.textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(scrollPane, BorderLayout.CENTER);
		
		//Create a new JPanel, which holds radiobuttons and a jlist to allow user to
		//do some styling.
		JPanel northPanel = new JPanel();
		
		//I used GridBagLayout with the new JPanel
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 1;
		c.insets = new Insets(15, 15, 15, 15);
		northPanel.setLayout(gridBagLayout);
		
		//Add elements to the new JPanel
		JLabel selectColorLabel = new JLabel(this.bundle.getString("selectColor"));
	
		ButtonGroup buttonGroup = new ButtonGroup();
		
		JRadioButton dayModeButton = new JRadioButton();
		dayModeButton.setText(this.bundle.getString("dayMode"));
		dayModeButton.setSelected(true);
		buttonGroup.add(dayModeButton);
		//Add actionlistener to radio button "Day Mode". When it is selected,
		//it fires an eventh which changes the background color to white, and
		//the text color to black.
		dayModeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.setForeground(Color.BLACK);
				textArea.setBackground(Color.WHITE);
			}
		});
		JRadioButton nightModeButton = new JRadioButton();
		nightModeButton.setText(this.bundle.getString("nightMode"));
		buttonGroup.add(nightModeButton);
		//This actionlistener is attached to "N ight Mode" radio button.
		//It triggers an event with the opposite effect. I.e. background is
		//set to black and text color is set to white.
		nightModeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.setForeground(Color.WHITE);
				textArea.setBackground(Color.BLACK);
			}
		});
		
		//Create JList with 3 elements: Monospaced, Arial and SansSerif.
		//These are different font types, which user can choose from.
		String[] fonts = new String[] {
				this.bundle.getString("mono"), 
				this.bundle.getString("arial"), 
				this.bundle.getString("sansSerif"
		)};
		final JList<String> list = new JList<String>(fonts);
		list.setSelectedIndex(1);
		
		//I attached an actionlistener to the list, so when selection is
		//changed, the event fires a change in the font type of text area
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if(list.getSelectedIndex() == 0) {
					textArea.setFont(MONOSPACED);
				} else if(list.getSelectedIndex() == 1) {
					textArea.setFont(ARIAL);
				} else {
					textArea.setFont(SANS_SERIF);
				}
			}
		});
		
		//Create a border around JList
		list.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		//Add elements to northpanel and add northpanel to this panel.
		northPanel.add(selectColorLabel, c); 
		c.gridx++;
		northPanel.add(dayModeButton, c);
		c.gridx++;
		northPanel.add(nightModeButton, c);
		c.gridx++;
		northPanel.add(list, c);
		this.add(northPanel, BorderLayout.NORTH);
	}
}
