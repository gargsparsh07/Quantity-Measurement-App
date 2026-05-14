package com.qm.user.controller;

import com.qm.user.dto.ConversionHistoryRequest;
import com.qm.user.model.ConversionHistory;
import com.qm.user.repository.ConversionHistoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/users") @RequiredArgsConstructor
@Tag(name = "User History", description = "Conversion history per user")
public class HistoryController {
    private final ConversionHistoryRepository historyRepository;

    @PostMapping("/{userId}/history")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save a conversion to user history")
    public ConversionHistory saveHistory(@PathVariable Long userId,
                                         @RequestBody ConversionHistoryRequest request) {
        ConversionHistory history = ConversionHistory.builder()
            .userId(userId).type(request.getType())
            .fromUnit(request.getFromUnit()).toUnit(request.getToUnit())
            .inputValue(request.getInputValue()).outputValue(request.getOutputValue()).build();
        return historyRepository.save(history);
    }

    @GetMapping("/{userId}/history")
    @Operation(summary = "Get all conversion history for a user")
    public ResponseEntity<List<ConversionHistory>> getHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(historyRepository.findByUserId(userId));
    }
}
