package com.example.skipro.util;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles persistence of generic data types to and from files.
 * Supports saving and loading both List and Set collections.
 *
 * @param <T> the type of objects to persist
 */
public class PersistenceManager<T> {
    private final String filePath;


    /**
     * Constructs a PersistenceManager for the specified file path.
     * @param filePath the path to the file for persistence
     */
    public PersistenceManager(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves a List of data to the file.
     *
     * @param data the List of data to save
     */
    public void save(List<T> data) {
        File file = new File(filePath);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) dir.mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a List of objects from the file.
     * Returns the deserialized List if successful, or null if the file does not exist or an error occurs.
     *
     * @return the loaded List of type T, or null if loading failed or file does not exist
     */
    @SuppressWarnings("unchecked")
    public List<T> load() {
        File file = new File(filePath);
        if (!file.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Saves a Set of objects to the file.
     * Serializes the provided Set and writes it to the specified file path.
     * If the parent directory does not exist, it will be created.
     * Any IOException encountered during saving will be printed to the error stream.
     *
     * @param data the Set of objects to save
     */
    @SuppressWarnings("unchecked")
    public void saveSet(Set<T> data) {
        File file = new File(filePath);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) dir.mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a Set of objects from the file.
     * Returns the deserialized Set if successful, or an empty Set if the file does not exist,
     * the data is not a Set, or an error occurs during loading.
     *
     * @return the loaded Set of type T, or an empty Set if loading failed or file does not exist
     */
    @SuppressWarnings("unchecked")
    public Set<T> loadSet() {
        File file = new File(filePath);
        if (!file.exists()) return new HashSet<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Set<?>) {
                return (Set<T>) obj;
            } else {
                return new HashSet<>();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }
}