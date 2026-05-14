package com.qm.measurement.controller;

import com.qm.measurement.model.ConversionResult;
import com.qm.measurement.service.ConversionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/convert")
@Tag(name = "Conversions", description = "Unit conversion endpoints")
public class ConversionController {

    @Autowired
    private ConversionService conversionService;

    @GetMapping("/length")
    @Operation(summary = "Convert length units (km, miles, feet, inch, meter)")
    public ResponseEntity<ConversionResult> convertLength(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double value,
            @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(conversionService.convertLength(from, to, value, userId));
    }

    @GetMapping("/weight")
    @Operation(summary = "Convert weight units (kg, lbs, gram)")
    public ResponseEntity<ConversionResult> convertWeight(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double value,
            @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(conversionService.convertWeight(from, to, value, userId));
    }

    @GetMapping("/temperature")
    @Operation(summary = "Convert temperature units (C, F, K)")
    public ResponseEntity<ConversionResult> convertTemperature(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double value,
            @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(conversionService.convertTemperature(from, to, value, userId));
    }

    @GetMapping("/volume")
    @Operation(summary = "Convert volume units (liter, gallon, ml)")
    public ResponseEntity<ConversionResult> convertVolume(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double value,
            @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(conversionService.convertVolume(from, to, value, userId));
    }
}
