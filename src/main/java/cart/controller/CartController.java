package cart.controller;

import cart.auth.Auth;
import cart.dto.request.CartRequestDto;
import cart.dto.response.CartItemResponseDto;
import cart.exception.CartItemNotFoundException;
import cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    private static final String REDIRECT_URL = "/";

    private final CartService cartService;

    public CartController(final CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@Auth final Integer userId,
                                       @RequestBody @Valid final CartRequestDto cartRequestDto) {
        cartService.create(cartRequestDto, userId);
        return ResponseEntity.created(URI.create(REDIRECT_URL)).build();
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponseDto>> read(@Auth final Integer userId) {
        final List<CartItemResponseDto> response = cartService.getProductsInCart(userId);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Auth final Integer userId, @PathVariable final int id) {
        cartService.delete(id, userId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<String> handle() {
        return ResponseEntity.notFound().build();
    }
}
