package woowacourse.shoppingcart.domain.User;

import woowacourse.shoppingcart.domain.customer.UserName;

public class Member implements User {

    private final UserName userName;

    public Member(UserName userName) {
        this.userName = userName;
    }

    @Override
    public boolean isLogin() {
        return true;
    }

    @Override
    public UserName getUserName() {
        return userName;
    }
}