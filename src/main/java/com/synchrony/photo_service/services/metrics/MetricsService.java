package com.synchrony.photo_service.services.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    public enum MetricType {
        CREATE_USER, UPDATE_USER, DELETE_USER, UPLOAD_IMAGE
    }

    private final Map<MetricType, Counter> counters = new EnumMap<>(MetricType.class);

    public MetricsService(MeterRegistry meterRegistry) {
        counters.put(MetricType.CREATE_USER, meterRegistry.counter("user_creations_total", "operation", "create"));
        counters.put(MetricType.UPDATE_USER, meterRegistry.counter("user_updates_total", "operation", "update"));
        counters.put(MetricType.DELETE_USER, meterRegistry.counter("user_deletions_total", "operation", "delete"));
        counters.put(MetricType.UPLOAD_IMAGE, meterRegistry.counter("image_uploads_total", "operation", "create"));
    }

    public void increment(MetricType metricType) {
        Counter counter = counters.get(metricType);
        if (counter != null) {
            counter.increment();
        }
    }
}