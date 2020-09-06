package com.nstanogias.skistore.web;

import com.nstanogias.skistore.domain.CustomerBasket;
import com.nstanogias.skistore.domain.order.Order;
import com.nstanogias.skistore.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@RestController
@Slf4j
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    @NotNull
    private final PaymentService paymentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{basketId}")
    public ResponseEntity<CustomerBasket> CreateOrUpdatePaymentIntent(@PathVariable String basketId) throws StripeException {
        CustomerBasket basket = paymentService.createOrUpdatePaymentIntent(basketId);

        if (basket == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok().body(basket);
        }
    }

    @PostMapping("webhook")
    public ResponseEntity<?> stripeWebhook(@RequestBody String json, HttpServletRequest request) {
        String header = request.getHeader("Stripe-Signature");
        String WhSecret = "whsec_MCROCcge5j5N1EKqRsccaaSWQZRjg3tI";
        try {
            Event event = Webhook.constructEvent(json, header, WhSecret);
            PaymentIntent intent;
            Order order;
            switch (event.getType()) {
                case "payment_intent.succeeded":
                intent = (PaymentIntent)event.getData().getObject();
                log.info("Payment Succeeded: ", intent.getId());
                order  = paymentService.updateOrderPaymentSucceeded(intent.getId());
                log.info("Order updated to payment received: ", order.getId());
                break;
            case "payment_intent.payment_failed":
                intent = (PaymentIntent)event.getData().getObject();
                log.info("Payment Failed: ", intent.getId());
                order = paymentService.updateOrderPaymentFailed(intent.getId());
                log.info("Payment Failed: ", order.getId());
                break;
            }
            System.err.println(event);
        } catch ( SignatureVerificationException e) {
            log.error(e.toString());
        }
        return ResponseEntity.noContent().build();
    }
}
