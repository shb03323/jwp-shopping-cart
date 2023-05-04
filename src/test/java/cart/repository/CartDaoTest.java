package cart.repository;

import cart.dto.response.CartItemResponseDto;
import cart.entity.CartEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartDaoTest {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;
    private CartDao cartDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        cartDao = new CartDao(dataSource);
    }

    @Test
    @DisplayName("장바구니에 추가 성공")
    void create_success() {
        // given
        final int userId = 1;
        final int productId = 2;
        final CartEntity cartEntity = new CartEntity(userId, productId);

        // when
        final int id = cartDao.create(cartEntity);

        // then
        final CartEntity result = findByUserIdAndProductId(userId, productId);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(id),
                () -> assertThat(result.getUserId()).isEqualTo(userId),
                () -> assertThat(result.getProductId()).isEqualTo(productId)
        );
    }

    private CartEntity findByUserIdAndProductId(final int userId, final int productId) {
        final String sql = "select * from cart where user_id = ? and product_id = ?";
        return jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new CartEntity(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id")
                ), userId, productId);
    }

    @Test
    @DisplayName("장바구니 조회 성공")
    @Sql({"/product_dummy_data.sql", "/cart_dummy_data.sql"})
    void findProductByUserId_success() {
        // given
        final int userId = 1;

        // when
        final List<CartItemResponseDto> productsInCart = cartDao.findByUserId(userId);

        // then
        assertAll(
                () -> assertThat(productsInCart).hasSize(2),
                () -> assertThat(productsInCart.get(0).getName()).isEqualTo("삼겹살"),
                () -> assertThat(productsInCart.get(0).getImage()).isEqualTo("3-hierarchy-meat.jpg"),
                () -> assertThat(productsInCart.get(0).getPrice()).isEqualTo(16000)
        );
    }

    @Test
    @DisplayName("장바구니 상품 삭제 성공")
    @Sql({"/product_dummy_data.sql", "/cart_dummy_data.sql"})
    void delete_success() {
        // given
        final int cartId = 1;
        final int userId = 1;

        // when
        cartDao.deleteByIdAndUserId(cartId, userId);

        // then
        String sql = "select * from cart where id = ? and user_id = ?";
        assertThatThrownBy(() -> jdbcTemplate.queryForObject(sql, Integer.class, cartId, userId))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    @DisplayName("장바구니 상품 삭제 실패 - 존재하지 않는 장바구니 상품 id")
    @Sql({"/product_dummy_data.sql", "/cart_dummy_data.sql"})
    void delete_fail() {
        // given
        final int invalidId = 0;
        final int userId = 1;

        // expect
        assertThatThrownBy(() -> cartDao.deleteByIdAndUserId(invalidId, userId))
                .isInstanceOf(DataAccessException.class);
    }
}