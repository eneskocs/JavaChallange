/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.eneskocs.JavaChallange.entities;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author enesh
 */
@Entity
@Table(name = "carts")
public class Cart extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartProduct> cartProducts = new ArrayList<>();

    private BigDecimal totalPrice = BigDecimal.ZERO;

    public void calculateTotalPrice() {
        totalPrice = cartProducts.stream()
            .map(cartProduct -> cartProduct.getProduct().getPrice().multiply(BigDecimal.valueOf(cartProduct.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Getters and Setters
    public List<CartProduct> getCartProducts() {
        return cartProducts;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
