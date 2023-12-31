package com.fastkart.masterservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fastkart.masterservice.user.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence")
    private Long id;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column
    private String role;

    @OneToMany
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @JsonIgnore
    private List<Product> product;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}