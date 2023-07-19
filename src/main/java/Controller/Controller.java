package Controller;

import db.Database;
import model.User;
import webserver.annotations.HandleRequest;
import webserver.httpMessage.*;

public class Controller {
    private static class SingletonHelper {
        private static final Controller CONTROLLER = new Controller();
    }

    public static Controller getInstance() {
        return SingletonHelper.CONTROLLER;
    }
    @HandleRequest(path = "/user/create", httpMethod = HttpMethod.GET)
    public HttpResponse createUser(HttpRequest httpRequest) {
        URI uri = httpRequest.getURI();
        User user = User.of(uri.getParameters());

        Database.addUser(user);

        return HttpResponse.generateHttpResponse(StatusCode.OK, new byte[]{});
    }
}
