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

        htmlBuilder.append("<!DOCTYPE html>\n");
        htmlBuilder.append("<html lang=\"kr\">\n");
        htmlBuilder.append("<head>\n");
        htmlBuilder.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n");
        htmlBuilder.append("<meta charset=\"utf-8\">\n");
        htmlBuilder.append("<title>SLiPP Java Web Programming</title>\n");
        htmlBuilder.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n");
        htmlBuilder.append("<link href=\"../css/bootstrap.min.css\" rel=\"stylesheet\">\n");
        htmlBuilder.append("<!--[if lt IE 9]>\n");
        htmlBuilder.append("<script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script>\n");
        htmlBuilder.append("<![endif]-->\n");
        htmlBuilder.append("<link href=\"../css/styles.css\" rel=\"stylesheet\">\n");

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
