package com.nstanogias.skistore.web;

import com.nstanogias.skistore.dtos.CustomerBasketDto;
import com.nstanogias.skistore.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/basket")
@RequiredArgsConstructor
public class BasketController {

    @NotNull
    private final BasketService basketService;

    @GetMapping()
    public ResponseEntity<CustomerBasketDto> getBasketById(@RequestParam String cid) {
        return new ResponseEntity<>(basketService.findByCid(cid), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerBasketDto> updateBasket(@RequestBody @Valid CustomerBasketDto basket) {
        return new ResponseEntity<>(basketService.insert(basket), HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteBasketById(@RequestParam String cid) {
        basketService.deleteByCid(cid);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}