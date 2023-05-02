package cart.infra;

import javax.servlet.http.HttpServletRequest;

public interface AuthorizationExtractor<T> {

    String AUTHORIZATION = "Authorization";

    T extract(HttpServletRequest request);
}
