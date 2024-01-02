package mancala;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;

public class Saver {
    public static void saveObject(final Serializable toSave, final String filename) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(filename);
             ObjectOutputStream outputDestination = new ObjectOutputStream(outputStream)) {
            outputDestination.writeObject(toSave);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static Serializable loadObject(final String filename) throws IOException {
        Serializable loadedObject = null;
        try (FileInputStream inputStream = new FileInputStream(filename);
             ObjectInputStream input = new ObjectInputStream(inputStream)) {
            loadedObject = (Serializable) input.readObject();
        } catch (IOException e) {
            System.out.println("IOException is caught " + e);
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException is caught " + e);
        }
        return loadedObject;
    }
}