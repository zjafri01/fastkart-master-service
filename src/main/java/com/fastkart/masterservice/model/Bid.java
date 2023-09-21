package com.fastkart.masterservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bid_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bid {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="bid_sequence")
    @SequenceGenerator(name = "bid_sequence", sequenceName = "bid_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    private String username;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product productId;

    @Column
    private Double biddingAmount;

    public Bid(User user,String username, Double biddingAmount) {
        this.user = user;
        this.username = username;
        this.biddingAmount = biddingAmount;
    }
}
