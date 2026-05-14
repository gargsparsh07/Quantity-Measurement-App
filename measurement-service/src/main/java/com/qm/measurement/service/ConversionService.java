package com.qm.measurement.service;

import com.qm.measurement.client.UserServiceClient;
import com.qm.measurement.model.ConversionHistoryRequest;
import com.qm.measurement.model.ConversionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversionService {

    private static final Logger log = LoggerFactory.getLogger(ConversionService.class);

    @Autowired
    private UserServiceClient userServiceClient;

    public ConversionResult convertLength(String from, String to, double value, Long userId) {
        double result = performLengthConversion(from, to, value);
        saveHistory(userId, "LENGTH", from, to, value, result);
        return new ConversionResult(from, to, value, result, "LENGTH");
    }

    public ConversionResult convertWeight(String from, String to, double value, Long userId) {
        double result = performWeightConversion(from, to, value);
        saveHistory(userId, "WEIGHT", from, to, value, result);
        return new ConversionResult(from, to, value, result, "WEIGHT");
    }

    public ConversionResult convertTemperature(String from, String to, double value, Long userId) {
        double result = performTemperatureConversion(from, to, value);
        saveHistory(userId, "TEMPERATURE", from, to, value, result);
        return new ConversionResult(from, to, value, result, "TEMPERATURE");
    }

    public ConversionResult convertVolume(String from, String to, double value, Long userId) {
        double result = performVolumeConversion(from, to, value);
        saveHistory(userId, "VOLUME", from, to, value, result);
        return new ConversionResult(from, to, value, result, "VOLUME");
    }

    private void saveHistory(Long userId, String type, String from, String to,
                              double input, double output) {
        if (userId != null) {
            try {
                userServiceClient.saveHistory(userId,
                    new ConversionHistoryRequest(type, from, to, input, output));
            } catch (Exception e) {
                log.warn("Could not save history: {}", e.getMessage());
            }
        }
    }

    private double performLengthConversion(String from, String to, double value) {
        if (from.equalsIgnoreCase("km") && to.equalsIgnoreCase("miles"))   return value * 0.621371;
        if (from.equalsIgnoreCase("miles") && to.equalsIgnoreCase("km"))   return value / 0.621371;
        if (from.equalsIgnoreCase("feet") && to.equalsIgnoreCase("inch"))  return value * 12;
        if (from.equalsIgnoreCase("inch") && to.equalsIgnoreCase("feet"))  return value / 12;
        if (from.equalsIgnoreCase("meter") && to.equalsIgnoreCase("feet")) return value * 3.28084;
        if (from.equalsIgnoreCase("feet") && to.equalsIgnoreCase("meter")) return value / 3.28084;
        throw new IllegalArgumentException("Unsupported length conversion: " + from + " -> " + to);
    }

    private double performWeightConversion(String from, String to, double value) {
        if (from.equalsIgnoreCase("kg") && to.equalsIgnoreCase("lbs"))   return value * 2.20462;
        if (from.equalsIgnoreCase("lbs") && to.equalsIgnoreCase("kg"))   return value / 2.20462;
        if (from.equalsIgnoreCase("kg") && to.equalsIgnoreCase("gram"))  return value * 1000;
        if (from.equalsIgnoreCase("gram") && to.equalsIgnoreCase("kg"))  return value / 1000;
        throw new IllegalArgumentException("Unsupported weight conversion: " + from + " -> " + to);
    }

    private double performTemperatureConversion(String from, String to, double value) {
        if (from.equalsIgnoreCase("C") && to.equalsIgnoreCase("F")) return (value * 9.0 / 5.0) + 32;
        if (from.equalsIgnoreCase("F") && to.equalsIgnoreCase("C")) return (value - 32) * 5.0 / 9.0;
        if (from.equalsIgnoreCase("C") && to.equalsIgnoreCase("K")) return value + 273.15;
        if (from.equalsIgnoreCase("K") && to.equalsIgnoreCase("C")) return value - 273.15;
        throw new IllegalArgumentException("Unsupported temperature conversion: " + from + " -> " + to);
    }

    private double performVolumeConversion(String from, String to, double value) {
        if (from.equalsIgnoreCase("liter") && to.equalsIgnoreCase("gallon"))   return value * 0.264172;
        if (from.equalsIgnoreCase("gallon") && to.equalsIgnoreCase("liter"))   return value / 0.264172;
        if (from.equalsIgnoreCase("liter") && to.equalsIgnoreCase("ml"))       return value * 1000;
        if (from.equalsIgnoreCase("ml") && to.equalsIgnoreCase("liter"))       return value / 1000;
        throw new IllegalArgumentException("Unsupported volume conversion: " + from + " -> " + to);
    }
}
