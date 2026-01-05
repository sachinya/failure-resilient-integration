package com.example.failure_resilient_integration.repository;

import com.example.failure_resilient_integration.model.IdempotencyRecord;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class IdempotencyRepository {

    private final Map<String, IdempotencyRecord> store =
            new ConcurrentHashMap<>();

    public Optional<IdempotencyRecord> find(String key) {
        return Optional.ofNullable(store.get(key));
    }

    public void save(IdempotencyRecord record) {
        store.put(record.getKey(), record);
    }
}
