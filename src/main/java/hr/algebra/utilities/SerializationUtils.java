package hr.algebra.utilities;

import java.io.*;

public class SerializationUtils {
    private SerializationUtils() {
    }

    public static<T extends Serializable> void write(T t, String filename) throws IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(t);
        }
    }
    public static<T extends Serializable> T read(String filename) throws IOException, ClassNotFoundException {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (T) ois.readObject();
        }
    }
}
