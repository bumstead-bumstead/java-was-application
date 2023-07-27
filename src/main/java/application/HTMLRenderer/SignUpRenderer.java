package application.HTMLRenderer;

import application.model.User;

import java.util.Map;

public class SignUpRenderer implements HTMLRenderer {

    @Override
    public byte[] render(Map<String, Object> renderParameter) {
        User sessionUser = (User) renderParameter.get("user");
        StringBuilder htmlBuilder = new StringBuilder();

        fillNavigationBar(sessionUser, htmlBuilder);

        htmlBuilder.append("<div class=\"container\" id=\"main\">");
        htmlBuilder.append("<div class=\"col-md-6 col-md-offset-3\">");
        htmlBuilder.append("<div class=\"panel panel-default content-main\">");
        htmlBuilder.append("<form name=\"question\" method=\"post\" action=\"/user/create\">");
        htmlBuilder.append("<div class=\"form-group\">");
        htmlBuilder.append("<label for=\"userId\">사용자 아이디</label>");
        htmlBuilder.append("<input class=\"form-control\" id=\"userId\" name=\"userId\" placeholder=\"User ID\">");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<div class=\"form-group\">");
        htmlBuilder.append("<label for=\"password\">비밀번호</label>");
        htmlBuilder.append("<input type=\"password\" class=\"form-control\" id=\"password\" name=\"password\" placeholder=\"Password\">");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<div class=\"form-group\">");
        htmlBuilder.append("<label for=\"name\">이름</label>");
        htmlBuilder.append("<input class=\"form-control\" id=\"name\" name=\"name\" placeholder=\"Name\">");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<div class=\"form-group\">");
        htmlBuilder.append("<label for=\"email\">이메일</label>");
        htmlBuilder.append("<input type=\"email\" class=\"form-control\" id=\"email\" name=\"email\" placeholder=\"Email\">");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<button type=\"submit\" class=\"btn btn-success clearfix pull-right\">회원가입</button>");
        htmlBuilder.append("<div class=\"clearfix\" />");
        htmlBuilder.append("</form>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</div>");

        htmlBuilder.append("<!-- script references -->");
        htmlBuilder.append("<script src=\"../js/jquery-2.2.0.min.js\"></script>");
        htmlBuilder.append("<script src=\"../js/bootstrap.min.js\"></script>");
        htmlBuilder.append("<script src=\"../js/scripts.js\"></script>");
        htmlBuilder.append("</body>");
        htmlBuilder.append("</html>");

        return htmlBuilder.toString().getBytes();
    }
}
