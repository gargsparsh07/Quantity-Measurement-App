package com.bridgelabz.repository;

import com.bridgelabz.model.QuantityMeasurementEntity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UC15: QuantityMeasurementCacheRepository - Singleton Repository
 * In-memory cache backed by disk serialization for persistence.
 */
public class QuantityMeasurementCacheRepository
        implements IQuantityMeasurementRepository {

    // ======== Singleton ========
    private static QuantityMeasurementCacheRepository instance;

    private QuantityMeasurementCacheRepository() {
        cache = new ArrayList<>();
        loadFromDisk();
    }

    public static synchronized QuantityMeasurementCacheRepository getInstance() {
        if (instance == null)
            instance = new QuantityMeasurementCacheRepository();
        return instance;
    }

    // ======== Fields ========
    private final List<QuantityMeasurementEntity> cache;
    private static final String FILE_PATH = "quantity_measurements.ser";

    // ======== IQuantityMeasurementRepository Implementation ========
    @Override
    public void save(QuantityMeasurementEntity entity) {
        if (entity == null) return;
        cache.add(entity);
        saveToDisk(entity);
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return new ArrayList<>(cache);
    }

    @Override
    public void clearAll() {
        cache.clear();
        File file = new File(FILE_PATH);
        if (file.exists()) file.delete();
    }

    // ======== Disk Persistence ========
    private void saveToDisk(QuantityMeasurementEntity entity) {
        File file = new File(FILE_PATH);
        try {
            ObjectOutputStream oos;
            if (file.exists() && file.length() > 0) {
                oos = new AppendableObjectOutputStream(
                        new FileOutputStream(file, true));
            } else {
                oos = new ObjectOutputStream(
                        new FileOutputStream(file, false));
            }
            oos.writeObject(entity);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            System.err.println("[Repository] Failed to save to disk: "
                    + e.getMessage());
        }
    }

    private void loadFromDisk() {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) return;

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    Object obj = ois.readObject();
                    if (obj instanceof QuantityMeasurementEntity)
                        cache.add((QuantityMeasurementEntity) obj);
                } catch (EOFException eof) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Repository] Failed to load from disk: "
                    + e.getMessage());
        }
    }

    // ======== Inner Class: AppendableObjectOutputStream ========
    private static class AppendableObjectOutputStream
            extends ObjectOutputStream {

        public AppendableObjectOutputStream(OutputStream out)
                throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset(); // suppress header on append
        }
    }
}