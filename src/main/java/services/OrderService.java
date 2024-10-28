/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;


import com.github.eneskocs.JavaChallange.entities.Order;
import com.github.eneskocs.JavaChallange.entities.Cart;
import com.github.eneskocs.JavaChallange.entities.CartProduct;
import com.github.eneskocs.JavaChallange.entities.OrderProduct;
import com.github.eneskocs.JavaChallange.repositories.OrderProductRepository;
import com.github.eneskocs.JavaChallange.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
    }

    @Transactional
    public Order placeOrder(Cart cart) {
        Order order = new Order();
        order.setCustomer(cart.getCustomer());

        List<OrderProduct> orderProducts = new ArrayList<>();
        BigDecimal totalOrderPrice = BigDecimal.ZERO;

        for (CartProduct cartProduct : cart.getCartProducts()) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(cartProduct.getProduct());
            orderProduct.setQuantity(cartProduct.getQuantity());
            orderProduct.setPurchasePrice(cartProduct.getProduct().getPrice());

            BigDecimal productTotalPrice = orderProduct.getPurchasePrice().multiply(BigDecimal.valueOf(orderProduct.getQuantity()));
            totalOrderPrice = totalOrderPrice.add(productTotalPrice);

            orderProducts.add(orderProduct);
        }

        order.setOrderProducts(orderProducts);
        order.setTotalPrice(totalOrderPrice);

        orderRepository.save(order);
        orderProductRepository.saveAll(orderProducts);

        return order;
    }
}
