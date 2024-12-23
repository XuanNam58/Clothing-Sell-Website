package com.example.clothing_sell_website.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "[Order]")
public class Order {
    @Id
    @Column(name = "OrderId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer orderId;

    @Column(name = "Date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime date;

    @Column(name = "PaymentMethod")
    String paymentMethod;

    @Column(name = "Status")
    Boolean status;

    @ManyToOne
    @JoinColumn(name = "StaffId")
    Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CustomerId")
    Customer customer;
}
