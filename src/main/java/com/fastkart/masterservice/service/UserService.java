package com.fastkart.masterservice.service;

import com.fastkart.masterservice.model.User;
import com.fastkart.masterservice.repository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

//    @Autowired
//    private SellerDetailsRepository sellerDetailsRepository;
//    @Autowired
//    private BuyerDetailsRepository buyerDetailsRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    private final PasswordEncoder passwordEncoder;

    public boolean signup(User user){
        if(userDetailsRepository.existsUserByUsername(user.getUsername())){
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDetailsRepository.signup(user);
    }


    public boolean login(User user){
        User userDetails = null;
        try{
            userDetails = userDetailsRepository.findByUsername(user.getUsername());
            return user.getUsername().equals(userDetails.getUsername()) && user.getPassword().equals(userDetails.getPassword());
        }
        catch (NullPointerException e){
            return false;
        }
    }
}
