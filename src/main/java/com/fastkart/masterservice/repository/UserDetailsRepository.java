package com.fastkart.masterservice.repository;

import com.fastkart.masterservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<User, Long> {

    default boolean signup(User user){
        User userCreated = save(user);
        return true;
    }

    boolean existsUserByUsername(String username);

    public User findByUsername(String username);
}