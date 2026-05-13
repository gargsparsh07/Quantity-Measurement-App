package com.bridgelabz;

import com.bridgelabz.controller.QuantityMeasurementController;
import com.bridgelabz.model.QuantityDTO;
import com.bridgelabz.repository.IQuantityMeasurementRepository;
import com.bridgelabz.repository.QuantityMeasurementCacheRepository;
import com.bridgelabz.service.IQuantityMeasurementService;
import com.bridgelabz.service.QuantityMeasurementServiceImpl;

/**
 * UC15: QuantityMeasurementApp - Refactored Entry Point
 * Design Patterns: Factory, Facade, Singleton, Dependency Injection
 */
public class QuantityMeasurementApp {

    // ======== Factory Methods ========
    private static IQuantityMeasurementRepository createRepository() {
        return QuantityMeasurementCacheRepository.getInstance();
    }

    private static IQuantityMeasurementService createService(
            IQuantityMeasurementRepository repo) {
        return new QuantityMeasurementServiceImpl(repo);
    }

    private static QuantityMeasurementController createController(
            IQuantityMeasurementService service) {
        return new QuantityMeasurementController(service);
    }

    // ======== Main ========
    public static void main(String[] args) {

        // Initialize layers
        IQuantityMeasurementRepository repository = createRepository();
        IQuantityMeasurementService    service    = createService(repository);
        QuantityMeasurementController  controller = createController(service);

        System.out.println("=== UC15: Quantity Measurement App ===\n");

        // Example 1: Length Equality
        System.out.println(">>> Example 1: Length Equality");
        controller.performComparison(
                new QuantityDTO(1.0,  QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));

        // Example 2: Weight Equality
        System.out.println(">>> Example 2: Weight Equality");
        controller.performComparison(
                new QuantityDTO(1.0,    QuantityDTO.WeightUnit.KILOGRAM),
                new QuantityDTO(1000.0, QuantityDTO.WeightUnit.GRAM));

        // Example 3: Temperature Equality
        System.out.println(">>> Example 3: Temperature Equality");
        controller.performComparison(
                new QuantityDTO(0.0,  QuantityDTO.TemperatureUnit.CELSIUS),
                new QuantityDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT));

        // Example 4: Length Conversion
        System.out.println(">>> Example 4: Length Conversion (1 FEET -> INCHES)");
        controller.performConversion(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(0.0, QuantityDTO.LengthUnit.INCHES));

        // Example 5: Length Addition
        System.out.println(">>> Example 5: Length Addition (1 FEET + 12 INCHES)");
        controller.performAddition(
                new QuantityDTO(1.0,  QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));

        // Example 6: Temperature Addition (should fail)
        System.out.println(">>> Example 6: Temperature Addition (should fail)");
        controller.performAddition(
                new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                new QuantityDTO(50.0,  QuantityDTO.TemperatureUnit.CELSIUS));

        // Example 7: Cross-Category (should fail)
        System.out.println(">>> Example 7: Cross-Category Compare (should fail)");
        controller.performComparison(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM));

        // Example 8: Division
        System.out.println(">>> Example 8: Division (2 FEET / 1 FOOT)");
        controller.performDivision(
                new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET));

        System.out.println("\n=== Done ===");
    }
}