package com.nstanogias.skistore.web;

import com.nstanogias.skistore.dtos.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/buggy")
public class BuggyController {

    @GetMapping("/notfound")
    public ResponseEntity<Void> getNotFoundRequest() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/servererror")
    public ResponseEntity<ApiResponse> getServerError() {
        return new ResponseEntity(new ApiResponse(false, ""), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/badrequest")
    public ResponseEntity<ApiResponse> getBadRequest() {
        return new ResponseEntity(new ApiResponse(false, ""), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/badrequest/{id}")
    public ResponseEntity<ApiResponse> getNotFoundRequest(@PathVariable int id) {
        return new ResponseEntity(new ApiResponse(false, ""), HttpStatus.NOT_FOUND);
    }
}