package com.anton.gateway.repository;

import com.anton.gateway.domain.RequestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestRecordRepository extends JpaRepository<RequestRecord, Long> {

    Optional<RequestRecord> findByRequestId(String requestId);

    @Query("SELECT r FROM RequestRecord r ORDER BY r.id DESC LIMIT 1")
    Optional<RequestRecord> findLatestRecord();

}
