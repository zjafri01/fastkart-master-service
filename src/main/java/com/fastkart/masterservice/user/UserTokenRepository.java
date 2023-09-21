package com.fastkart.masterservice.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepository extends JpaRepository<UserToken, Integer> {

    Optional<UserToken> findByEmail(String email);

}