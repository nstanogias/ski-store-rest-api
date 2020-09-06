package com.nstanogias.skistore.service;

import com.nstanogias.skistore.domain.CustomerBasket;
import com.nstanogias.skistore.domain.Product;
import com.nstanogias.skistore.domain.order.DeliveryMethod;
import com.nstanogias.skistore.domain.order.Order;
import com.nstanogias.skistore.domain.order.OrderStatus;
import com.nstanogias.skistore.repository.BasketRepository;
import com.nstanogias.skistore.repository.DeliveryMethodRepository;
import com.nstanogias.skistore.repository.OrderRepository;
import com.nstanogias.skistore.repository.ProductRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentService {

    @NotNull
    private final BasketRepository basketRepository;

    @NotNull
    private final DeliveryMethodRepository deliveryMethodRepository;

    @NotNull
    private final ProductRepository productRepository;

    @NotNull
    private final OrderRepository orderRepository;

    public CustomerBasket createOrUpdatePaymentIntent(String basketId) throws StripeException {
        CustomerBasket basket = basketRepository.findByCid(basketId).get();

        if (basket == null) return null;

        double shippingPrice = 0;

        if (basket.getDeliveryMethodId() != null) {
            DeliveryMethod deliveryMethod = deliveryMethodRepository.getOne(basket.getDeliveryMethodId());
            shippingPrice = deliveryMethod.getPrice();
        }

        basket.getItems().stream().forEach(item -> {
            Product product = productRepository.findByName(item.getProductName()).get();
            if (item.getPrice() != product.getPrice()) {
                item.setPrice(product.getPrice());
            }
        });

        long amount = (long) (basket.getItems().stream().mapToDouble(item -> item.getPrice() * item.getQuantity() * 100).reduce(0.0, Double::sum) + shippingPrice);
        Stripe.apiKey = "***********************";
        PaymentIntent paymentIntent;
        if (basket.getPaymentIntentId() == null) {

            List<Object> paymentMethodTypes = new ArrayList<>();
            paymentMethodTypes.add("card");
            Map<String, Object> params = new HashMap<>();
            params.put("amount", amount);
            params.put("currency", "dkk");
            params.put("payment_method_types", paymentMethodTypes);

            paymentIntent = PaymentIntent.create(params);
        } else {
            paymentIntent = PaymentIntent.retrieve(basket.getPaymentIntentId());
            Map<String, Object> params = new HashMap<>();
            params.put("amount", amount);

            paymentIntent = paymentIntent.update(params);
        }

        basket.setPaymentIntentId(paymentIntent.getId());
        basket.setClientSecret(paymentIntent.getClientSecret());

        return basketRepository.save(basket);
    }

    public Order updateOrderPaymentFailed(String paymentIntentId) {
        Optional<Order> order = orderRepository.findByPaymentIntentId(paymentIntentId);
        if (!order.isPresent()) return null;

        Order orderToUpdate = order.get();
        orderToUpdate.setStatus(OrderStatus.PaymentFailed);
        return orderRepository.save(orderToUpdate);
    }

    public Order updateOrderPaymentSucceeded(String paymentIntentId) {
        Optional<Order> order = orderRepository.findByPaymentIntentId(paymentIntentId);
        if (!order.isPresent()) return null;

        Order orderToUpdate = order.get();
        orderToUpdate.setStatus(OrderStatus.PaymentReceived);
        return orderRepository.save(orderToUpdate);
    }
}
