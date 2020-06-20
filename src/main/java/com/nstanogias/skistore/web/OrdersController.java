package com.nstanogias.skistore.web;

import com.nstanogias.skistore.domain.order.DeliveryMethod;
import com.nstanogias.skistore.domain.order.Order;
import com.nstanogias.skistore.dtos.OrderDto;
import com.nstanogias.skistore.dtos.OrderToReturnDto;
import com.nstanogias.skistore.security.UserPrincipal;
import com.nstanogias.skistore.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrdersController {
    @Autowired
    private ModelMapper modelMapper;

    @NotNull
    private final OrdersService ordersService;

    @PostMapping()
    public ResponseEntity<Order> createOrder(@RequestBody OrderDto orderDto) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = ordersService.createOrder(userPrincipal.getUsername(), orderDto.getDeliveryMethodId(), orderDto.getBasketId(), orderDto.getShipToAddress());
        return order == null ? ResponseEntity.badRequest().body(null) : ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrdersForUser() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Order> ordersForUser = ordersService.getOrdersForUser(userPrincipal.getUsername());
        List<OrderDto> orderDtos = ordersForUser.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return ResponseEntity.ok(orderDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderToReturnDto> getOrderByIdForUser(@PathVariable long id) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = ordersService.getOrderById(id, userPrincipal.getUsername());
        if (order == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            OrderToReturnDto orderToReturnDto = modelMapper.map(order, OrderToReturnDto.class);
//            orderToReturnDto.getOrderItems().stream().forEach(orderItemDto -> orderItemDto.setPictureUrl(or));
            return ResponseEntity.ok(orderToReturnDto);
        }
    }

    @GetMapping("/deliveryMethods")
    public ResponseEntity<List<DeliveryMethod>> getDeliveryMethods() {
        return ResponseEntity.ok(ordersService.getDeliveryMethods());
    }
}
