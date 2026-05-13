package com.app.quantitymeasurement;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurement.services.QuantityMeasurementServiceImpl;
import com.app.quantitymeasurement.util.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class QuantityMeasurementApp {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementApp.class);

    private final IQuantityMeasurementRepository repository;
    private final QuantityMeasurementController controller;

    public QuantityMeasurementApp() {
        ApplicationConfig config = ApplicationConfig.getInstance();
        String repoType = config.getRepositoryType();

        if ("database".equalsIgnoreCase(repoType)) {
            this.repository = new QuantityMeasurementDatabaseRepository();
            logger.info("Using DatabaseRepository. Stats: {}", repository.getPoolStatistics());
        } else {
            this.repository = new QuantityMeasurementCacheRepository();
            logger.info("Using CacheRepository");
        }

        QuantityMeasurementServiceImpl service = new QuantityMeasurementServiceImpl(repository);
        this.controller = new QuantityMeasurementController(service);
        logger.info("QuantityMeasurementApp initialized");
    }

    public QuantityMeasurementController getController() {
        return controller;
    }

    public void closeResources() {
        repository.releaseResources();
        logger.info("Resources released");
    }

    public void deleteAllMeasurements() {
        repository.deleteAll();
        logger.info("All measurements deleted");
    }

    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return repository.getAllMeasurements();
    }

    public static void main(String[] args) {
        logger.info("Starting Quantity Measurement Application");
        QuantityMeasurementApp app = new QuantityMeasurementApp();

        try {
            // Example operations
            boolean result1 = app.getController().compareQuantities(1.0, "FEET", 12.0, "INCH");
            logger.info("1 FEET == 12 INCH : {}", result1);

            boolean result2 = app.getController().compareQuantities(1.0, "KG", 1000.0, "GRAM");
            logger.info("1 KG == 1000 GRAM : {}", result2);

            double added = app.getController().addQuantities(2.0, "FEET", 2.0, "FEET", "FEET");
            logger.info("2 FEET + 2 FEET = {} FEET", added);

            List<QuantityMeasurementEntity> all = app.getAllMeasurements();
            logger.info("Total measurements stored: {}", all.size());
            all.forEach(e -> logger.info("  -> {}", e));

            logger.info("Pool stats: {}", app.repository.getPoolStatistics());

            app.deleteAllMeasurements();
            logger.info("Measurements after delete: {}", app.repository.getTotalCount());
        } finally {
            app.closeResources();
        }
    }
}
