package application.HTMLRenderer;

import application.model.User;

import java.util.Map;

public class BoardFormRenderer implements HTMLRenderer {
    @Override
    public byte[] render(Map<String, Object> renderParameter) {
        User sessionUser = (User) renderParameter.get("user");
        StringBuilder htmlBuilder = new StringBuilder();

        fillNavigationBar(sessionUser, htmlBuilder);
        htmlBuilder.append("<div class=\"container\" id=\"main\">");
        htmlBuilder.append("<div class=\"col-md-12 col-sm-12 col-lg-10 col-lg-offset-1\">");
        htmlBuilder.append("<div class=\"panel panel-default content-main\">");
        htmlBuilder.append("<form name=\"question\" method=\"post\" action=\"/board/create\">");
        htmlBuilder.append("<div class=\"form-group\">");
        htmlBuilder.append("<label for=\"writer\">글쓴이</label>");
        htmlBuilder.append("<input class=\"form-control\" id=\"writer\" name=\"writer\" placeholder=\"글쓴이\"/>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<div class=\"form-group\">");
        htmlBuilder.append("<label for=\"title\">제목</label>");
        htmlBuilder.append("<input type=\"text\" class=\"form-control\" id=\"title\" name=\"title\" placeholder=\"제목\"/>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<div class=\"form-group\">");
        htmlBuilder.append("<label for=\"contents\">내용</label>");
        htmlBuilder.append("<textarea name=\"contents\" id=\"contents\" rows=\"5\" class=\"form-control\"></textarea>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<button type=\"submit\" class=\"btn btn-success clearfix pull-right\">질문하기</button>");
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
