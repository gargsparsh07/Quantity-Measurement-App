package com.qm.measurement.client;

import com.qm.measurement.model.ConversionHistoryRequest;
import com.qm.measurement.model.ConversionHistoryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserServiceClientFallback implements UserServiceClient {

    private static final Logger log = LoggerFactory.getLogger(UserServiceClientFallback.class);

    @Override
    public ConversionHistoryResponse saveHistory(Long userId, ConversionHistoryRequest request) {
        log.warn("user-service unavailable — history not saved for user {}", userId);
        return new ConversionHistoryResponse();
    }

    @Override
    public List<ConversionHistoryResponse> getHistory(Long userId) {
        log.warn("user-service unavailable — cannot fetch history for user {}", userId);
        return List.of();
    }
}
