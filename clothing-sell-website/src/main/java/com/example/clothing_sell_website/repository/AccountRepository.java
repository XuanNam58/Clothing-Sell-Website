package com.example.clothing_sell_website.repository;

import com.example.clothing_sell_website.entity.Account;
import com.example.clothing_sell_website.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByUsername(String s);
    Optional<Account> findByCustomer(Customer customer);
}