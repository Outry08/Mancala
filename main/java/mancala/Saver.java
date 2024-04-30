package mancala;

import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Saver {
    
    public static void saveObject(final Serializable toSave, final String filename) throws IOException{
        final ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(filename));
        objectOut.writeObject(toSave);
        objectOut.close();
    }

    public static Serializable loadObject(final String filename) throws IOException{
        final ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(filename));
        Serializable obj;
        try {
            obj = (Serializable) objectIn.readObject();
            objectIn.close();
        } catch(ClassNotFoundException e) {
            objectIn.close();
            throw new IOException();
        }
        return obj;
    }

}
