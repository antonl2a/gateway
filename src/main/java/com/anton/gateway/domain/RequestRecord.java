package com.anton.gateway.domain;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "RequestRecord",
        indexes = {
        @Index(name = "IX_Request_Id", columnList = "RequestID")
        })
public class RequestRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ServiceType")
    private ServiceType serviceType;

    @Column(name = "RequestID", nullable = false)
    private String requestId;

    @Column(name = "Timestamp", columnDefinition = "TIMESTAMP")
    private Timestamp receivedAt;

    @Column(name = "ClientID")
    private String clientId;

    @Column(name = "Currency")
    private String currency;

    @Column(name = "hours")
    private Integer hours;

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Instant getTimestamp() {
        return receivedAt != null ? receivedAt.toInstant() : null;
    }
    public void setTimestamp(Instant timestamp) {
        this.receivedAt = timestamp != null ? Timestamp.from(timestamp) : null;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestRecord that = (RequestRecord) o;
        return serviceType == that.serviceType && Objects.equals(requestId, that.requestId) && Objects.equals(receivedAt, that.receivedAt) && Objects.equals(clientId, that.clientId) && Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceType, requestId, receivedAt, clientId, currency);
    }

    @Override
    public String toString() {
        return "RequestRecord{" +
                "hours=" + hours +
                ", currency='" + currency + '\'' +
                ", clientId='" + clientId + '\'' +
                ", receivedAt=" + getTimestamp() +
                ", requestId='" + requestId + '\'' +
                ", serviceType=" + serviceType +
                '}';
    }
}
