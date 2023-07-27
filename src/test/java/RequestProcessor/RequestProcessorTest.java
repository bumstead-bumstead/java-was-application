package RequestProcessor;

import application.RequestProcessor.RequestProcessor;
import webserver.http.session.Session;
import webserver.http.session.SessionDatabase;
import application.db.UserDatabase;
import webserver.http.session.Cookie;
import application.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import webserver.exceptions.BadRequestException;
import webserver.http.message.HttpHeaderUtils;
import webserver.http.message.HttpMessageHeader;
import webserver.http.message.HttpResponse;
import webserver.http.message.StatusCode;

import static org.assertj.core.api.Assertions.assertThat;

class RequestProcessorTest {
    @Nested
    @DisplayName("유저 생성 테스트")
    class CreateUser {
        RequestProcessor requestProcessor;
        String userId;
        String password;
        String name;
        String email;

        @BeforeEach
        void setup() {
            UserDatabase.clear();
            requestProcessor = RequestProcessor.getInstance();
            userId = "userId";
            password = "password";
            name = "name";
            email = "asdsda@dasads";
        }

        @Test
        @DisplayName("올바른 입력에 대해서 응답으로 302번 코드를 보낸다.")
        void createUserTest() {
            HttpResponse httpResponse = requestProcessor.createUser(userId, password, name, email);

            assertThat(httpResponse.getStatusCode()).isEqualTo(StatusCode.FOUND);
            assertThat(httpResponse.getHeaders().containsKey("Location")).isTrue();
        }

        @Test
        @DisplayName("데이터베이스에 유저 정보를 저장한다.")
        void uploadToDatabase() {
            requestProcessor.createUser(userId, password, name, email);

            User actualUser = UserDatabase.findUserById("userId");
            User expectedUser = new User(userId, password, name, email);

            assertThat(actualUser).isEqualTo(expectedUser);
        }
    }

    @Nested
    @DisplayName("유저 로그인 테스트")
    class login {
        RequestProcessor requestProcessor;
        String userId;
        String password;
        String name;
        String email;

        @BeforeEach
        void setup() {
            UserDatabase.clear();
            requestProcessor = RequestProcessor.getInstance();
            userId = "userId";
            password = "password";
            name = "name";
            email = "asdsda@dasads";

            requestProcessor.createUser(userId, password, name, email);
        }

        @Test
        @DisplayName("올바른 로그인에 대해서 302를 응답으로 보낸다.")
        void login() throws BadRequestException {
            HttpResponse httpResponse = requestProcessor.login(userId, password);

            assertThat(httpResponse.getStatusCode()).isEqualTo(StatusCode.FOUND);
        }

        @Test
        @DisplayName("올바른 로그인 후에는 세션을 저장한다.")
        void loginSessionTest() throws BadRequestException {
            SessionDatabase.clear();
            HttpResponse httpResponse = requestProcessor.login(userId, password);

            assertThat(SessionDatabase.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("올바른 로그인일 경우 응답 헤더에 cookie를 포함시킨다.")
        void loginCookieTest() throws BadRequestException {
            HttpResponse httpResponse = requestProcessor.login(userId, password);
            HttpMessageHeader headers = httpResponse.getHeaders();

            assertThat(headers.getCookies().size()).isGreaterThan(0);

            Cookie cookie = headers.getCookies().get(0);
            assertThat(cookie.getName()).isEqualTo("sid");
        }

        @Test
        @DisplayName("존재하지 않는 id인 경우, /user/login_failed.html로 redirection")
        void loginWithWrongId() throws BadRequestException {
            userId = "modifiedId";
            HttpResponse httpResponse = requestProcessor.login(userId, password);

            String expectedRedirectionURI = "http://localhost:8080/user/login_failed.html";
            String actualRedirectionURI = httpResponse.getHeaders().getValue(HttpHeaderUtils.LOCATION_HEADER);


            assertThat(actualRedirectionURI).isEqualTo(expectedRedirectionURI);
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않는 경우, /user/login_failed.html로 redirection")
        void loginWithWrongPwd() throws BadRequestException {
            password = "wrongPassword";
            HttpResponse httpResponse = requestProcessor.login(userId, password);

            String expectedRedirectionURI = "http://localhost:8080/user/login_failed.html";
            String actualRedirectionURI = httpResponse.getHeaders().getValue(HttpHeaderUtils.LOCATION_HEADER);

            assertThat(actualRedirectionURI).isEqualTo(expectedRedirectionURI);
        }
    }

    @Nested
    @DisplayName("index.html 테스트")
    class index {
        RequestProcessor requestProcessor;
        String userId;
        String password;
        String name;
        String email;

        @BeforeEach
        void setup() throws BadRequestException {
            UserDatabase.clear();
            requestProcessor = RequestProcessor.getInstance();
            userId = "userId";
            password = "password";
            name = "name";
            email = "asdsda@dasads";

            requestProcessor.createUser(userId, password, name, email);
            requestProcessor.login(userId, password);
        }

        @Test
        @DisplayName("유효한 세션이 있는 경우 로그아웃/개인정보/이름을 보여준다.")
        void indexWithSession() throws Exception {
            Session session = new Session();
            session.addAttribute("userId", "userId");
            SessionDatabase.addSession(session);

            HttpResponse httpResponse = requestProcessor.index(session);
            String body = new String(httpResponse.getBody());

            assertThat(body).contains("name");
            assertThat(body).contains("로그아웃");
            assertThat(body).contains("개인정보수정");
            assertThat(body).doesNotContain("로그인");
            assertThat(body).doesNotContain("회원가입");

        }

        @Test
        @DisplayName("유효한 세션이 없는 경우 로그인/회원가입을 보여준다.")
        void indexWithoutSession() throws Exception {
            HttpResponse httpResponse = requestProcessor.index(null);
            String body = new String(httpResponse.getBody());

            assertThat(body).contains("로그인");
            assertThat(body).contains("회원가입");
            assertThat(body).doesNotContain("로그아웃");
            assertThat(body).doesNotContain("개인정보조회");

        }

        @Test
        @DisplayName("유효하지 않은 세션이 있는 경우 로그인/회원가입을 보여준다.")
        void indexWithInvalidSession() throws Exception {
            Session session = new Session();
            session.addAttribute("userId", "d");
            SessionDatabase.addSession(session);

            HttpResponse httpResponse = requestProcessor.index(session);

            String body = new String(httpResponse.getBody());

            assertThat(body).contains("로그인");
            assertThat(body).contains("회원가입");
            assertThat(body).doesNotContain("로그아웃");
            assertThat(body).doesNotContain("개인정보조회");
        }
    }

    @Nested
    @DisplayName("/user/list 테스트")
    class userList {
        RequestProcessor requestProcessor;
        String userId;
        String password;
        String name;
        String email;

        @BeforeEach
        void setup() throws BadRequestException {
            UserDatabase.clear();
            requestProcessor = RequestProcessor.getInstance();
            userId = "userId";
            password = "password";
            name = "name";
            email = "asdsda@dasads";

            requestProcessor.createUser(userId, password, name, email);
            requestProcessor.login(userId, password);
        }

        @Test
        @DisplayName("유효한 세션이 있는 경우 유저 정보를 모두 보여준다.")
        void getListWithSession() throws Exception {
            Session session = new Session();
            session.addAttribute("userId", "userId");
            SessionDatabase.addSession(session);

            HttpResponse httpResponse = requestProcessor.userList(session);
            String body = new String(httpResponse.getBody());

            assertThat(body).contains(userId);
            assertThat(body).contains(name);
            assertThat(body).contains(email);
        }

        @Test
        @DisplayName("세션이 존재하지 않는 경우 login.html로 redirection한다.")
        void getListWithoutSession() throws BadRequestException {
            HttpResponse httpResponse = requestProcessor.userList(null);

            assertThat(httpResponse.getStatusCode()).isEqualTo(StatusCode.FOUND);
            assertThat(httpResponse.getHeaders().getValue("Location")).contains("login.html");
        }
    }
}