package com.fastkart.masterservice.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product_table")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="product_sequence")
    @SequenceGenerator(name = "product_sequence", sequenceName = "product_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    private String username;

    @Column
    private String name;

    @Column
    private String description;

    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    @Column
    private String listed_datetime;

    @Column
    private Double min_bid_amt_seller;

    @Column
    private Double current_bid_amt;

    @OneToMany
    @JoinColumn(name = "bid_id", referencedColumnName = "id")
    private List<Bid> bid;

}
