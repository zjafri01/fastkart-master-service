package com.fastkart.masterservice.controller;

import com.fastkart.masterservice.model.Product;
import com.fastkart.masterservice.model.User;
import com.fastkart.masterservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

@RestController
@RequestMapping("/fastkart")
@CrossOrigin
@Slf4j
public class Controller {

    private LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    private final LinkedHashMap<String, String> sessionMap = new LinkedHashMap<>();

    private final HashMap<String,String> invalidSessionMessage = new HashMap<>();

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody User user){
        responseMap.clear();
        log.info("Request received to create a new userToken");
        if(userService.signup(user)){
            log.info("Request Completed");
            responseMap.put("successMessage","signup successful new account for "+user.getRole().toLowerCase(Locale.ROOT)+" created");
            responseMap.put("statusCode",HttpStatus.OK.value());
            responseMap.put("user",user);
            return ResponseEntity.status(HttpStatus.OK).body(responseMap);
        }

        log.error("BAD REQUEST: User Already Exists With the Username");
        HttpHeaders headers = new HttpHeaders();
        headers.add("errorMessage", "User Already Exists With the Username, Please try different Username.");
        responseMap.put("errorMessage","User Already Exists With the Username, Please try different Username.");
        responseMap.put("statusCode",HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(responseMap);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user){
        responseMap.clear();
        log.info("Request received to login a userToken");

        if(userService.login(user)){
            sessionMap.put(user.getUsername(), user.getRole());
            responseMap.put("successMessage","Welcome");
            responseMap.put("statusCode",HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(responseMap);
        }
        responseMap.put("errorMessage","Incorrect Credentials");
        responseMap.put("statusCode",HttpStatus.FORBIDDEN.value());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseMap);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestBody User user){
        responseMap.clear();
        log.info("Request received to logout a user");

        if(!checkIsValidSession(user.getUsername())){
            responseMap.put("errorMessage",invalidSessionMessage);
            responseMap.put("statusCode",HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseMap);
        }

        sessionMap.remove(user.getUsername());
        responseMap.put("successMessage","User Logged Out Successfully");
        responseMap.put("statusCode",HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(responseMap);
    }

    @PostMapping("/seller/viewProducts")
    public ResponseEntity<Object> sellerViewProducts(@RequestBody User user){
        if(!checkIsValidSession(user.getUsername())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(invalidSessionMessage);
        }

        log.info("Request received to view seller products");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> entity = new  HttpEntity<>(user,headers);
        ResponseEntity<Object> response = restTemplate.exchange("http://localhost:8081/seller/viewProducts", HttpMethod.POST, entity, Object.class);

        return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
    }

    @PostMapping("/buyer/viewProducts")
    public ResponseEntity<Object> buyerViewProducts(@RequestBody User user){
        if(!checkIsValidSession(user.getUsername())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(invalidSessionMessage);
        }

        log.info("Request received to view buyer products");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> entity = new  HttpEntity<>(user, headers);
        ResponseEntity<Object> response = restTemplate.exchange("http://localhost:8082/buyer/viewAvailableProducts", HttpMethod.POST, entity, Object.class);

        return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
    }

    @PostMapping("/seller/addProducts")
    public ResponseEntity<Object> sellerAddProduct(@RequestBody Product product){
        log.info("Request received to add seller products");

        if(!checkIsValidSession(product.getUsername())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(invalidSessionMessage);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Product> entity = new  HttpEntity<>(product, headers);
        ResponseEntity<Object> response = restTemplate.exchange("http://localhost:8081/seller/addProducts", HttpMethod.POST, entity, Object.class);

        return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
    }

    @PostMapping("/buyer/placeBid")
    public ResponseEntity placeBid(@RequestBody Product product){
        log.info("Request received to place a bid for product");

        if(!checkIsValidSession(product.getUsername())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(invalidSessionMessage);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Product> entity = new  HttpEntity<>(product, headers);
        ResponseEntity response = restTemplate.exchange("http://localhost:8082/buyer/placeBid", HttpMethod.POST, entity, Object.class);

        return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
    }

    @PostMapping("/helloSeller")
    public ResponseEntity<String> helloSeller(){
        log.info("Request received to view products");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new  HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8081/seller/hello", HttpMethod.GET, entity, String.class);
        return new ResponseEntity<String>(response.getBody(), HttpStatus.OK);
    }

    private boolean checkIsValidSession(String username) {
        if(!sessionMap.containsKey(username)){
            invalidSessionMessage.put("errorMessage","User is not available in the current users logged-in session, please login again.");
            return false;
        }
        return true;
    }
}