package com.nstanogias.skistore.domain.order;

public enum OrderStatus {
    Pending("Pending"), PaymentReceived("Payment Received"), PaymentFailed("Payment Failed");

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }
}
