<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    if (session.getAttribute("USER_SESSION_INFO") == null) {
        response.sendRedirect("/servlet?cmd=sign_in");
    }
%>
<html>
<head>
    <%@include file="../assets/localisation.jsp"%>
    <title><fmt:message bundle="${lang_bundle}" key="title.create_track"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@200&display=swap" rel="stylesheet">
    <style>
        <%@include file="../css/general/body.css"%>
        <%@include file="../css/general/header.css"%>
        <%@include file="../css/general/footer.css"%>
        <%@include file="../css/general/create.css"%>
    </style>
</head>
<body class="main_container">
<%@include file="../assets/header.jsp"%>
<div class="content_body">
    <div class="inner_content_container">
        <div class="page_content">
            <h1 class="page_h1"><fmt:message bundle="${lang_bundle}" key="title.create_track"/></h1>
            <form method="post" action="/servlet?cmd=create_track">
                <label class="input_label" for="name"><fmt:message bundle="${lang_bundle}" key="create_page.name"/></label><input class="input input_small" type="text" required id="name" name="name"/>
                <label class="input_label" for="performers"><fmt:message bundle="${lang_bundle}" key="create_page.performers"/></label><input class="input input_small" type="text" required pattern="[0-9]+(,[0-9]+)*" id="performers" name="performers"/>
                <label class="input_label" for="albums"><fmt:message bundle="${lang_bundle}" key="create_page.albums"/></label><input class="input input_small" type="text" required pattern="[0-9]+(,[0-9]+)*" id="albums" name="albums"/>
                <label class="input_label" for="lyrics"><fmt:message bundle="${lang_bundle}" key="create_page.lyrics"/></label><textarea class="input input_big" id="lyrics" required name="lyrics" wrap="soft"></textarea>
                <input class="submit" type="submit" value="<fmt:message bundle="${lang_bundle}" key="create_page.create"/>">
            </form>
            <c:if test="${param.get('err_code') != null}">
                <p><fmt:message bundle="${lang_bundle}" key="error.${param.get('err_code')}"/></p>
            </c:if>
        </div>
    </div>
</div>
<%@include file="../assets/footer.jsp"%>
</body>
</html>
