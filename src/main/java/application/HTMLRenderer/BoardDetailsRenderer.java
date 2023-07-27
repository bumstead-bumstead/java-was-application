package application.HTMLRenderer;

import application.model.Board;
import application.model.User;

import java.util.Map;

public class BoardDetailsRenderer implements HTMLRenderer {
    @Override
    public byte[] render(Map<String, Object> renderParameter) {
        StringBuilder htmlBuilder = new StringBuilder();
        Board board = (Board) renderParameter.get("board");
        User user = (User) renderParameter.get("user");
        fillNavigationBar(user, htmlBuilder);

        htmlBuilder.append("<div class=\"container\" id=\"main\">");
        htmlBuilder.append("<div class=\"col-md-12 col-sm-12 col-lg-12\">");
        htmlBuilder.append("<div class=\"panel panel-default\">");
        htmlBuilder.append("<header class=\"qna-header\">");
        htmlBuilder.append("<h2 class=\"qna-title\">")
                .append(board.getTitle())
                .append("</h2>");
        htmlBuilder.append("</header>");
        htmlBuilder.append("<div class=\"content-main\">");
        htmlBuilder.append("<article class=\"article\">");
        htmlBuilder.append("<div class=\"article-header\">");
        htmlBuilder.append("<div class=\"article-header-thumb\">");
        htmlBuilder.append("<img src=\"https://graph.facebook.com/v2.3/100000059371774/picture\" class=\"article-author-thumb\" alt=\"\">");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<div class=\"article-header-text\">");
        htmlBuilder.append("<a href=\"\" class=\"article-author-name\">")
                .append(board.getWriter())
                .append("</a>");
        htmlBuilder.append("<a href=\"/questions/413\" class=\"article-header-time\" title=\"퍼머링크\">");
        htmlBuilder.append(board.getCreatedAt());
        htmlBuilder.append("<i class=\"icon-link\"></i>");
        htmlBuilder.append("</a>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<div class=\"article-doc\">");
        htmlBuilder.append(board.getContents());
        htmlBuilder.append("</div>");
        htmlBuilder.append("<div class=\"article-utils\">");
        htmlBuilder.append("<ul class=\"article-utils-list\">");
        htmlBuilder.append("<li>");
        htmlBuilder.append("<a class=\"link-modify-article\" href=\"/questions/423/form\">수정</a>");
        htmlBuilder.append("</li>");
        htmlBuilder.append("<li>");
        htmlBuilder.append("<form class=\"form-delete\" action=\"/questions/423\" method=\"POST\">");
        htmlBuilder.append("<input type=\"hidden\" name=\"_method\" value=\"DELETE\">");
        htmlBuilder.append("<button class=\"link-delete-article\" type=\"submit\">삭제</button>");
        htmlBuilder.append("</form>");
        htmlBuilder.append("</li>");
        htmlBuilder.append("<li>");
        htmlBuilder.append("<a class=\"link-modify-article\" href=\"/index.html\">목록</a>");
        htmlBuilder.append("</li>");
        htmlBuilder.append("</ul>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</article>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</div>");

        htmlBuilder.append("<script type=\"text/template\" id=\"answerTemplate\">");
        htmlBuilder.append("<article class=\"article\">");
        htmlBuilder.append("<div class=\"article-header\">");
        htmlBuilder.append("<div class=\"article-header-thumb\">");
        htmlBuilder.append("<img src=\"https://graph.facebook.com/v2.3/1324855987/picture\" class=\"article-author-thumb\" alt=\"\">");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<div class=\"article-header-text\">");
        htmlBuilder.append("<a href=\"#\" class=\"article-author-name\">{0}</a>");
        htmlBuilder.append("<div class=\"article-header-time\">{1}</div>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<div class=\"article-doc comment-doc\">");
        htmlBuilder.append("{2}");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<div class=\"article-utils\">");
        htmlBuilder.append("<ul class=\"article-utils-list\">");
        htmlBuilder.append("<li>");
        htmlBuilder.append("<a class=\"link-modify-article\" href=\"/api/questions/{3}/answers/{4}/form\">수정</a>");
        htmlBuilder.append("</li>");
        htmlBuilder.append("<li>");
        htmlBuilder.append("<form class=\"delete-answer-form\" action=\"/api/questions/{3}/answers/{4}\" method=\"POST\">");
        htmlBuilder.append("<input type=\"hidden\" name=\"_method\" value=\"DELETE\">");
        htmlBuilder.append("<button type=\"submit\" class=\"delete-answer-button\">삭제</button>");
        htmlBuilder.append("</form>");
        htmlBuilder.append("</li>");
        htmlBuilder.append("</ul>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</article>");
        htmlBuilder.append("</script>");

        htmlBuilder.append("<!-- script references -->");
        htmlBuilder.append("<script src=\"../js/jquery-2.2.0.min.js\"></script>");
        htmlBuilder.append("<script src=\"../js/bootstrap.min.js\"></script>");
        htmlBuilder.append("<script src=\"../js/scripts.js\"></script>");
        htmlBuilder.append("</body>");
        htmlBuilder.append("</html>");

        return htmlBuilder.toString().getBytes();
    }
}
