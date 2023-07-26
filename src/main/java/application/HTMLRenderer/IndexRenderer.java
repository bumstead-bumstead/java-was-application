package application.HTMLRenderer;

import application.model.User;

import java.util.Map;

class IndexRenderer implements HTMLRenderer {
    @Override
    public byte[] render(Map<String, Object> parameters) {
        User user = (User) parameters.get("user");
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<!DOCTYPE html>\n");
        htmlBuilder.append("<html lang=\"kr\">\n");
        htmlBuilder.append("<head>\n");
        htmlBuilder.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n");
        htmlBuilder.append("<meta charset=\"utf-8\">\n");
        htmlBuilder.append("<title>SLiPP Java Web Programming</title>\n");
        htmlBuilder.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n");
        htmlBuilder.append("<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">\n");
        htmlBuilder.append("<!--[if lt IE 9]>\n");
        htmlBuilder.append("<script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script>\n");
        htmlBuilder.append("<![endif]-->\n");
        htmlBuilder.append("<link href=\"css/styles.css\" rel=\"stylesheet\">\n");
        htmlBuilder.append("</head>\n");
        htmlBuilder.append("<body>\n");
        htmlBuilder.append("<nav class=\"navbar navbar-fixed-top header\">\n");
        htmlBuilder.append("<div class=\"col-md-12\">\n");
        htmlBuilder.append("<div class=\"navbar-header\">\n");
        htmlBuilder.append("<a href=\"index.html\" class=\"navbar-brand\">SLiPP</a>\n");
        htmlBuilder.append("<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse1\">\n");
        htmlBuilder.append("<i class=\"glyphicon glyphicon-search\"></i>\n");
        htmlBuilder.append("</button>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("<div class=\"collapse navbar-collapse\" id=\"navbar-collapse1\">\n");
        htmlBuilder.append("<form class=\"navbar-form pull-left\">\n");
        htmlBuilder.append("<div class=\"input-group\" style=\"max-width:470px;\">\n");
        htmlBuilder.append("<input type=\"text\" class=\"form-control\" placeholder=\"Search\" name=\"srch-term\" id=\"srch-term\">\n");
        htmlBuilder.append("<div class=\"input-group-btn\">\n");
        htmlBuilder.append("<button class=\"btn btn-default btn-primary\" type=\"submit\"><i class=\"glyphicon glyphicon-search\"></i></button>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</form>\n");
        htmlBuilder.append("<ul class=\"nav navbar-nav navbar-right\">\n");
        htmlBuilder.append("<li>\n");
        htmlBuilder.append("<a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-bell\"></i></a>\n");
        htmlBuilder.append("<ul class=\"dropdown-menu\">\n");
        htmlBuilder.append("<li><a href=\"https://slipp.net\" target=\"_blank\">SLiPP</a></li>\n");
        htmlBuilder.append("<li><a href=\"https://facebook.com\" target=\"_blank\">Facebook</a></li>\n");
        htmlBuilder.append("</ul>\n");
        htmlBuilder.append("</li>\n");
        htmlBuilder.append("<li><a href=\"./user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>\n");
        htmlBuilder.append("</ul>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</nav>\n");
        htmlBuilder.append("<div class=\"navbar navbar-default\" id=\"subnav\">\n");
        htmlBuilder.append("<div class=\"col-md-12\">\n");
        htmlBuilder.append("<div class=\"navbar-header\">\n");
        htmlBuilder.append("<a href=\"#\" style=\"margin-left:15px;\" class=\"navbar-btn btn btn-default btn-plus dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-home\" style=\"color:#dd1111;\"></i> Home <small><i class=\"glyphicon glyphicon-chevron-down\"></i></small></a>\n");
        htmlBuilder.append("<ul class=\"nav dropdown-menu\">\n");
        htmlBuilder.append("<li><a href=\"user/profile.html\"><i class=\"glyphicon glyphicon-user\" style=\"color:#1111dd;\"></i> Profile</a></li>\n");
        htmlBuilder.append("<li class=\"nav-divider\"></li>\n");
        htmlBuilder.append("<li><a href=\"#\"><i class=\"glyphicon glyphicon-cog\" style=\"color:#dd1111;\"></i> Settings</a></li>\n");
        htmlBuilder.append("</ul>\n");
        htmlBuilder.append("<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse2\">\n");
        htmlBuilder.append("<span class=\"sr-only\">Toggle navigation</span>\n");
        htmlBuilder.append("<span class=\"icon-bar\"></span>\n");
        htmlBuilder.append("<span class=\"icon-bar\"></span>\n");
        htmlBuilder.append("<span class=\"icon-bar\"></span>\n");
        htmlBuilder.append("</button>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("<div class=\"collapse navbar-collapse\" id=\"navbar-collapse2\">\n");
        htmlBuilder.append("<ul class=\"nav navbar-nav navbar-right\">\n");

        if(user == null) {
            htmlBuilder.append("<li class=\"active\"><a href=\"index.html\">Posts</a></li>\n");
            htmlBuilder.append("<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>\n");
            htmlBuilder.append("<li><a href=\"user/form.html\" role=\"button\">회원가입</a></li>\n");
        } else {
            htmlBuilder.append("<li><a role=\"button\">")
                    .append(user.getName())
                    .append("</a></li>\n");
            htmlBuilder.append("<li class=\"active\"><a href=\"index.html\">Posts</a></li>\n");
            htmlBuilder.append("<li><a href=\"#\" role=\"button\">로그아웃</a></li>\n");
            htmlBuilder.append("<li><a href=\"#\" role=\"button\">개인정보수정</a></li>\n");
        }
        htmlBuilder.append("</ul>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("<div class=\"container\" id=\"main\">\n");
        htmlBuilder.append("<div class=\"col-md-12 col-sm-12 col-lg-10 col-lg-offset-1\">\n");
        htmlBuilder.append("<div class=\"panel panel-default qna-list\">\n");
        htmlBuilder.append("<ul class=\"list\">\n");
        htmlBuilder.append("<li>\n");
        htmlBuilder.append("<div class=\"wrap\">\n");
        htmlBuilder.append("<div class=\"main\">\n");
        htmlBuilder.append("<strong class=\"subject\">\n");
        htmlBuilder.append("<a href=\"./qna/show.html\">국내에서 Ruby on Rails와 Play가 활성화되기 힘든 이유는 뭘까?</a>\n");
        htmlBuilder.append("</strong>\n");
        htmlBuilder.append("<div class=\"auth-info\">\n");
        htmlBuilder.append("<i class=\"icon-add-comment\"></i>\n");
        htmlBuilder.append("<span class=\"time\">2016-01-15 18:47</span>\n");
        htmlBuilder.append("<a href=\"./user/profile.html\" class=\"author\">자바지기</a>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("<div class=\"reply\" title=\"댓글\">\n");
        htmlBuilder.append("<i class=\"icon-reply\"></i>\n");
        htmlBuilder.append("<span class=\"point\">8</span>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</li>\n");
        htmlBuilder.append("<li>\n");
        htmlBuilder.append("<div class=\"wrap\">\n");
        htmlBuilder.append("<div class=\"main\">\n");
        htmlBuilder.append("<strong class=\"subject\">\n");
        htmlBuilder.append("<a href=\"./qna/show.html\">runtime 에 reflect 발동 주체 객체가 뭔지 알 방법이 있을까요?</a>\n");
        htmlBuilder.append("</strong>\n");
        htmlBuilder.append("<div class=\"auth-info\">\n");
        htmlBuilder.append("<i class=\"icon-add-comment\"></i>\n");
        htmlBuilder.append("<span class=\"time\">2016-01-05 18:47</span>\n");
        htmlBuilder.append("<a href=\"./user/profile.html\" class=\"author\">김문수</a>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("<div class=\"reply\" title=\"댓글\">\n");
        htmlBuilder.append("<i class=\"icon-reply\"></i>\n");
        htmlBuilder.append("<span class=\"point\">12</span>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</li>\n");
        htmlBuilder.append("</ul>\n");
        htmlBuilder.append("<div class=\"row\">\n");
        htmlBuilder.append("<div class=\"col-md-3\"></div>\n");
        htmlBuilder.append("<div class=\"col-md-6 text-center\">\n");
        htmlBuilder.append("<ul class=\"pagination center-block\" style=\"display:inline-block;\">\n");
        htmlBuilder.append("<li><a href=\"#\">«</a></li>\n");
        htmlBuilder.append("<li><a href=\"#\">1</a></li>\n");
        htmlBuilder.append("<li><a href=\"#\">2</a></li>\n");
        htmlBuilder.append("<li><a href=\"#\">3</a></li>\n");
        htmlBuilder.append("<li><a href=\"#\">4</a></li>\n");
        htmlBuilder.append("<li><a href=\"#\">5</a></li>\n");
        htmlBuilder.append("<li><a href=\"#\">»</a></li>\n");
        htmlBuilder.append("</ul>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("<div class=\"col-md-3 qna-write\">\n");
        htmlBuilder.append("<a href=\"./qna/form.html\" class=\"btn btn-primary pull-right\" role=\"button\">질문하기</a>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("<script src=\"js/jquery-2.2.0.min.js\"></script>\n");
        htmlBuilder.append("<script src=\"js/bootstrap.min.js\"></script>\n");
        htmlBuilder.append("<script src=\"js/scripts.js\"></script>\n");
        htmlBuilder.append("</body>\n");
        htmlBuilder.append("</html>\n");

        return htmlBuilder.toString().getBytes();
    }
}
