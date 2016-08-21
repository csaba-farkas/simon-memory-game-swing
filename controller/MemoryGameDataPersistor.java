package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import interfaces.IDataPersistor;

public class MemoryGameDataPersistor implements IDataPersistor {
	
	/**
	 * <p>Data persistor class which implements {@link IDataPersistor}. 
	 * It handles the reading and writing of the high score from and to 
	 * savedGame.ser file.</p>
	 * <p>Date of last modification: 27-11/2015</p>
	 * 
	 * @author Csaba Farkas csaba.farkas@mycit.ie
	 */
	
	//final static variable stores the file location
	private final static String FILE_LOCATION = "src/data/savedGame.ser";

	/**
	 * <p>Method implements {@link IDataPersistor}.write method. Method creates a {@link FileOutputStream}
	 * and an {@link ObjectOutputStream}. They are used to write data into the file.</p>
	 */
	@Override
	public void write(Integer highScore) {
		try {
			FileOutputStream fos = new FileOutputStream(FILE_LOCATION);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(highScore);
			oos.close();
		} catch(IOException iox) {
			System.out.println(iox.getMessage());
		}
		
	}

	/**
	 * <p>Method implements {@link IDataPersistor}.read method. It creates a 
	 * {@link FileInputStream} and an {@link ObjectInputStream}. Then it reads 
	 * the Integer object from the file and returns it. When the file has no 
	 * content, a {@link ClassNotFoundException} is thrown and caught. In this
	 * case method returns null.</p>
	 */
	@Override
	public Integer read() {
		try {
			FileInputStream fis = new FileInputStream(FILE_LOCATION);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Integer highScore = (Integer) ois.readObject();
			return highScore;
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

}
