package com.test.dailyreport.model;

import java.util.Map;
import java.util.Objects;

public class Client {

    private String clientId;

    private Map<String, Integer> productMap;

    public Client(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Map<String, Integer> getProductMap() {
        return productMap;
    }

    public void setProductMap(Map<String, Integer> productMap) {
        this.productMap = productMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(clientId, client.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId);
    }
}
