package cart.controller;

import cart.BasicAuthorizationEncoder;
import cart.auth.dto.AuthenticationDto;
import cart.auth.repository.AuthDao;
import cart.dto.request.CartRequestDto;
import cart.dto.response.CartItemResponseDto;
import cart.exception.AuthorizationException;
import cart.exception.CartItemNotFoundException;
import cart.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    @MockBean
    private AuthDao authDao;

    private String email;
    private String password;
    private String authenticationHeader;
    private int userId;

    @BeforeEach
    void setUp() {
        email = "ditoo@wooteco.com";
        password = "ditoo1234";
        final AuthenticationDto authenticationDto = new AuthenticationDto(email, password);
        authenticationHeader = BasicAuthorizationEncoder.encode(
                authenticationDto.getEmail(), authenticationDto.getPassword());
        userId = 1;
    }

    @Test
    @DisplayName("장바구니에 추가 성공")
    void create_success() throws Exception {
        //given
        final CartRequestDto requestDto = new CartRequestDto(2);
        final String requestBody = objectMapper.writeValueAsString(requestDto);
        given(cartService.create(requestDto, userId))
                .willReturn(1);

        //expect
        mockMvc.perform(post("/cart")
                        .header("Authorization", authenticationHeader)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string("Location", "/"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("장바구니에 추가 실패 - 모든 값 입력 안함")
    void create_fail_not_all_argument_input() throws Exception {
        //given
        final CartRequestDto requestDto = new CartRequestDto(null);
        final String requestBody = objectMapper.writeValueAsString(requestDto);

        //expect
        mockMvc.perform(post("/cart")
                        .header("Authorization", authenticationHeader)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("장바구니에 추가 실패 - 잘못된 유저")
    void create_fail_unauthorized() throws Exception {
        // given
        final CartRequestDto requestDto = new CartRequestDto(1);
        final String requestBody = objectMapper.writeValueAsString(requestDto);
        given(authDao.findIdByEmailAndPassword(email, password))
                .willThrow(AuthorizationException.class);


        // expect
        mockMvc.perform(post("/cart")
                        .header("Authorization", authenticationHeader)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("장바구니 조회 성공")
    void read_success() throws Exception {
        // given
        final List<CartItemResponseDto> response = List.of(
                new CartItemResponseDto(1, "삼겹살", "3-hierarchy-fat.jpg", 16000),
                new CartItemResponseDto(1, "목살", "neck-fat.jpg", 15000)
        );
        given(authDao.findIdByEmailAndPassword(email, password))
                .willReturn(1);
        given(cartService.getProductsInCart(userId))
                .willReturn(response);

        // expect
        final String responseBody =
                "[" +
                        "{\"id\":1,\"name\":\"삼겹살\",\"image\":\"3-hierarchy-fat.jpg\",\"price\":16000}," +
                        "{\"id\":1,\"name\":\"목살\",\"image\":\"neck-fat.jpg\",\"price\":15000}" +
                "]";
        mockMvc.perform(get("/cart")
                        .header("Authorization", authenticationHeader))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(responseBody))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("장바구니 조회 실패 - 잘못된 유저")
    void read_fail_unauthorized() throws Exception {
        // given
        given(authDao.findIdByEmailAndPassword(email, password))
                .willThrow(AuthorizationException.class);

        // expect
        mockMvc.perform(get("/cart")
                        .header("Authorization", authenticationHeader))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("장바구니 상품 삭제 성공")
    void delete_success() throws Exception {
        //expect
        mockMvc.perform(delete("/cart/1")
                        .header("Authorization", authenticationHeader))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품 삭제 실패 - 없는 상품 id")
    void delete_fail_product_not_found() throws Exception {
        //given
        doThrow(CartItemNotFoundException.class)
                .when(cartService)
                .delete(0, userId);

        //expect
        mockMvc.perform(delete("/products/0")
                        .header("Authorization", authenticationHeader))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("상품 삭제 실패 - 잘못된 유저")
    void delete_fail_unauthorized() throws Exception {
        // given
        given(authDao.findIdByEmailAndPassword(email, password))
                .willThrow(AuthorizationException.class);

        // expect
        mockMvc.perform(delete("/cart/1")
                        .header("Authorization", authenticationHeader))
                .andExpect(status().isUnauthorized());
    }
}