package com.qm.measurement.client;

import com.qm.measurement.model.ConversionHistoryRequest;
import com.qm.measurement.model.ConversionHistoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-service", fallback = UserServiceClientFallback.class)
public interface UserServiceClient {

    @PostMapping("/api/users/{userId}/history")
    ConversionHistoryResponse saveHistory(
        @PathVariable("userId") Long userId,
        @RequestBody ConversionHistoryRequest request
    );

    @GetMapping("/api/users/{userId}/history")
    List<ConversionHistoryResponse> getHistory(
        @PathVariable("userId") Long userId
    );
}
