/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;
import com.github.eneskocs.JavaChallange.entities.Cart;
import com.github.eneskocs.JavaChallange.entities.CartProduct;
import com.github.eneskocs.JavaChallange.entities.Product;
import com.github.eneskocs.JavaChallange.repositories.CartRepository;
import com.github.eneskocs.JavaChallange.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Cart addProductToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }

        Optional<CartProduct> existingCartProduct = cart.getCartProducts().stream()
            .filter(cp -> cp.getProduct().getId().equals(productId))
            .findFirst();

        if (existingCartProduct.isPresent()) {
            CartProduct cartProduct = existingCartProduct.get();
            cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
        } else {
            CartProduct cartProduct = new CartProduct();
            cartProduct.setCart(cart);
            cartProduct.setProduct(product);
            cartProduct.setQuantity(quantity);
            cart.getCartProducts().add(cartProduct);
        }

        cart.calculateTotalPrice();
        return cartRepository.save(cart);
    }
}