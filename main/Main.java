package main;

import java.util.ResourceBundle;

import javax.swing.SwingUtilities;

import controller.MemoryGameController;
import controller.MemoryGameDataPersistor;
import interfaces.IDataPersistor;
import interfaces.IMemoryGameGui;
import view.MemoryGameFrame;

public class Main {

	public static void main(String[] args) {
				
		/**
		 * <p>This class includes the main method of the program. 
		 * <ul>
		 * <li>It creates the data persistor object, and assigns it to the program via the controller.</li>
		 * <li>It also reads in the high score and assigns it to the the current game using the controller.</li>
		 * <li>Then it creates a gui object and assigns it to the program.</p>
		 * </p>
		 * <p>Date of last modification: 27/11/2015</p>
		 * 
		 * @author Csaba Farkas csaba.farkas@mycit.ie
		 */
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				//Create the ResourceBundle
				ResourceBundle bundle = ResourceBundle.getBundle("main.mainProps");
				
				//Create data persistor
				IDataPersistor dataPersistor = new MemoryGameDataPersistor();
				MemoryGameController.getInstance().setDataPersistor(dataPersistor);
				
				//Read saved high score
				Integer highScore = MemoryGameController.getInstance().getDataPersistor().read();
				MemoryGameController.getInstance().setHighScore(highScore);
				
				//Create a GUI object and assign it to the program
				IMemoryGameGui gui = new MemoryGameFrame(bundle.getString("frameTitle"));
				MemoryGameController.getInstance().setGuiReference(gui);
			}
		});

		
		
		
	}
}
