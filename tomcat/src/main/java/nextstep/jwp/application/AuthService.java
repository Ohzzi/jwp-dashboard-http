package nextstep.jwp.application;

import http.request.QueryParams;
import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.exception.DuplicateAccountException;
import nextstep.jwp.exception.InvalidLoginFormatException;
import nextstep.jwp.exception.InvalidPasswordException;
import nextstep.jwp.exception.InvalidSignUpFormatException;
import nextstep.jwp.exception.MemberNotFoundException;
import nextstep.jwp.model.User;

public class AuthService {

    private AuthService() {
    }

    private static class AuthServiceHolder {
        private static final AuthService instance = new AuthService();
    }

    public static AuthService instance() {
        return AuthServiceHolder.instance;
    }

    public void signUp(final String requestBody) {
        QueryParams queryParams = QueryParams.parse(requestBody);
        String account = queryParams.get("account");
        String password = queryParams.get("password");
        String email = queryParams.get("email");
        validateSignUpFormat(account, password, email);
        validateAccountUnique(account);
        User user = new User(account, password, email);
        InMemoryUserRepository.save(user);
    }

    private void validateSignUpFormat(final String account, final String password, final String email) {
        if (account == null || password == null || email == null) {
            throw new InvalidSignUpFormatException();
        }
    }

    private void validateAccountUnique(final String account) {
        if (InMemoryUserRepository.findByAccount(account).isPresent()) {
            throw new DuplicateAccountException();
        }
    }

    public User login(final String requestBody) {
        QueryParams queryParams = QueryParams.parse(requestBody);
        String account = queryParams.get("account");
        String password = queryParams.get("password");
        validateLoginFormat(account, password);
        User user = findUser(account);
        checkPassword(password, user);
        return user;
    }

    private void validateLoginFormat(final String account, final String password) {
        if (account == null || password == null) {
            throw new InvalidLoginFormatException();
        }
    }

    private User findUser(final String account) {
        return InMemoryUserRepository.findByAccount(account)
                .orElseThrow(MemberNotFoundException::new);
    }

    private void checkPassword(final String password, final User user) {
        if (!user.checkPassword(password)) {
            throw new InvalidPasswordException();
        }
    }
}
