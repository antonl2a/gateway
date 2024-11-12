package com.anton.gateway.domain;

public enum ServiceType {
    EXT_SERVICE_1("EXT_SERVICE_1"), EXT_SERVICE_2("EXT_SERVICE_2");

    private final String serviceName;

    ServiceType(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}
