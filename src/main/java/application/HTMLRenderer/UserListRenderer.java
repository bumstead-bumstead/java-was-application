package application.HTMLRenderer;

import application.db.UserDatabase;
import application.model.User;

import java.util.Collection;
import java.util.Map;

public class UserListRenderer implements HTMLRenderer {
    @Override
    public byte[] render(Map<String, Object> parameters) {
        User sessionUser = (User) parameters.get("user");
        StringBuilder htmlBuilder = new StringBuilder();
        Collection<User> users = UserDatabase.findAll();
        fillNavigationBar(sessionUser, htmlBuilder);

        htmlBuilder.append("<div class=\"container\" id=\"main\">\n");
        htmlBuilder.append("<div class=\"col-md-10 col-md-offset-1\">\n");
        htmlBuilder.append("<div class=\"panel panel-default\">\n");
        htmlBuilder.append("<table class=\"table table-hover\">\n");
        htmlBuilder.append("<thead>\n");
        htmlBuilder.append("<tr>\n");
        htmlBuilder.append("<th>#</th><th>사용자 아이디</th><th>이름</th><th>이메일</th><th></th>\n");
        htmlBuilder.append("</tr>\n");
        htmlBuilder.append("</thead>\n");
        htmlBuilder.append("<tbody>\n");

        for (User user : users) {
            htmlBuilder.append("<tr>\n");
            htmlBuilder.append("<th scope=\"row\">1</th><td>")
                    .append(user.getUserId())
                    .append("</td><td>")
                    .append(user.getName())
                    .append("</td><td>")
                    .append(user.getEmail())
                    .append("</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n");
            htmlBuilder.append("</tr>\n");
        }
        htmlBuilder.append("</tbody>\n");
        htmlBuilder.append("</table>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("<!-- script references -->\n");
        htmlBuilder.append("<script src=\"../js/jquery-2.2.0.min.js\"></script>\n");
        htmlBuilder.append("<script src=\"../js/bootstrap.min.js\"></script>\n");
        htmlBuilder.append("<script src=\"../js/scripts.js\"></script>\n");
        htmlBuilder.append("</body>\n");
        htmlBuilder.append("</html>\n");

        return htmlBuilder.toString().getBytes();
    }
}
