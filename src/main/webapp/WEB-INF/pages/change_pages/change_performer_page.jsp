<%@ page import="entity.UserSession" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    if (session.getAttribute("USER_SESSION_INFO") == null
            || (((UserSession)session.getAttribute("performer_creator")).getId() != ((UserSession)session.getAttribute("USER_SESSION_INFO")).getId()
            && ((UserSession)session.getAttribute("USER_SESSION_INFO")).getRoleId() == 3)) {
        response.sendRedirect("/servlet?cmd=sign_in");
    }
%>
<html>
<head>
    <%@include file="../assets/localisation.jsp"%>
    <title><fmt:message bundle="${lang_bundle}" key="title.change_performer"/></title>
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
<c:set value="${sessionScope.get('performer')}" var="item"/>
<%@include file="../assets/header.jsp"%>
<div class="content_body">
    <div class="inner_content_container">
        <div class="page_content">
            <h1 class="page_h1"><fmt:message bundle="${lang_bundle}" key="title.change_performer"/></h1>
            <form method="post" action="/servlet?cmd=change_performer&item_id=${item.getId()}">
                <label class="input_label" for="name"><fmt:message bundle="${lang_bundle}" key="create_page.name"/></label><input class="input input_small" type="text" id="name" name="name" value="${item.getName()}"/>
                <label class="input_label" for="cover_path"><fmt:message bundle="${lang_bundle}" key="create_page.image"/></label><input class="input input_small" type="url" id="cover_path" name="cover_path" value="${item.getCoverImagePath()}"/>
                <label class="input_label" for="description"><fmt:message bundle="${lang_bundle}" key="create_page.description"/></label><textarea class="input input_big" id="description" name="description" wrap="soft"><c:out value="${item.getDescription()}"/></textarea>
                <input class="submit" type="submit" value="<fmt:message bundle="${lang_bundle}" key="object_pages.change"/>">
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
