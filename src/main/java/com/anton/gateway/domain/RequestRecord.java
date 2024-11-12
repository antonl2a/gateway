package com.anton.gateway.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Columns;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "RequestRecord")
public class RequestRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ServiceType")
    private ServiceType serviceType;

    @Column(name = "RequestID")
    private String requestId;

    @Column(name = "Time")
    private Long receivedAt;

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

    public Long getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Long receivedAt) {
        this.receivedAt = receivedAt;
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
}
