package nextstep.jwp.presentation;

import http.header.HttpHeaders;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.HttpStatus;
import http.session.Session;
import http.session.SessionManager;
import java.util.UUID;
import nextstep.jwp.application.AuthService;
import nextstep.jwp.model.User;

public class LoginController extends AbstractController {

    private static final String REDIRECT_PAGE_PATH = "/index.html";

    private final AuthService authService;

    private LoginController(final AuthService authService) {
        this.authService = authService;
    }

    public static LoginController instance() {
        return LoginControllerHolder.instance;
    }

    @Override
    protected HttpResponse doGet(final HttpRequest httpRequest) throws Exception {
        if (isLoggedIn(httpRequest)) {
            return redirect(REDIRECT_PAGE_PATH);
        }

        String responseBody = readResource(httpRequest);
        HttpHeaders responseHeaders = setResponseHeaders(httpRequest, responseBody);

        return new HttpResponse(HttpStatus.OK, responseHeaders, responseBody);
    }

    @Override
    protected HttpResponse doPost(final HttpRequest httpRequest) {
        HttpHeaders responseHeaders = HttpHeaders.createEmpty();
        responseHeaders.add("Location", REDIRECT_PAGE_PATH);

        User loginUser = authService.login(httpRequest.getBody());
        Session session = httpRequest.getSession();
        if (session == null) {
            session = new Session(UUID.randomUUID().toString());
            SessionManager.instance().add(session);
            responseHeaders.add("Set-Cookie", "JSESSIONID=" + session.getId());
        }
        session.setAttribute("user", loginUser);
        return new HttpResponse(HttpStatus.FOUND, responseHeaders, "");
    }

    private boolean isLoggedIn(final HttpRequest httpRequest) {
        Session session = httpRequest.getSession();
        if (session == null) {
            return false;
        }
        User loginUser = (User) session.getAttribute("user");
        return loginUser != null;
    }

    public static class LoginControllerHolder {

        private static final LoginController instance = new LoginController(AuthService.instance());
    }
}
