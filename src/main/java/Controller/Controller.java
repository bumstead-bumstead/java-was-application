package Controller;

import db.Database;
import model.User;
import webserver.annotations.HandleRequest;
import webserver.httpMessage.*;

public class Controller {

    @HandleRequest(path = "/user/create", httpMethod = HttpMethod.GET)
    public static HttpResponse createUser(HttpRequest httpRequest) {
        URI uri = httpRequest.getURI();
        User user = User.of(uri.getParameters());

        Database.addUser(user);

        return HttpResponse.generateHttpResponse(StatusCode.OK, new byte[]{});
    }
}
