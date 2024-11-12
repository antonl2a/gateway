package com.anton.gateway.repository;

import com.anton.gateway.domain.CurrencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CurrencyRecordRepository extends JpaRepository<CurrencyRecord, Long> {

    @Query(value = "SELECT * FROM currency_record ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<CurrencyRecord> findLastRecord();
}
