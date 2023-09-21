package com.fastkart.masterservice.service;

import com.fastkart.masterservice.model.User;
import com.fastkart.masterservice.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

//    @Autowired
//    private SellerDetailsRepository sellerDetailsRepository;
//    @Autowired
//    private BuyerDetailsRepository buyerDetailsRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public boolean signup(User user){
        if(userDetailsRepository.existsUserByUsername(user.getUsername())){
            return false;
        }
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
