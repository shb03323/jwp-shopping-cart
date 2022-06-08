package woowacourse.shoppingcart.dto.customer;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CustomerUpdateRequest {

    @Size(min = 1, max = 10, message = "닉네임 1자 이상 10자 이하여야합니다.")
    @Pattern(regexp = "\\S*", message = "닉네임에는 공백이 들어가면 안됩니다.")
    private String username;

    private CustomerUpdateRequest() {
    }

    public CustomerUpdateRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}