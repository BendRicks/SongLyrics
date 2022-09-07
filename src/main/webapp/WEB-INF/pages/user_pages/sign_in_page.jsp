<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    if (session.getAttribute("USER_SESSION_INFO") != null) {
        response.sendRedirect("/servlet?cmd=main_page");
    }
%>
<html>
<head>
    <%@include file="../assets/localisation.jsp" %>
    <title><fmt:message bundle="${lang_bundle}" key="header.sign_in"/></title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@200&display=swap" rel="stylesheet">
    <style>
        <%@include file="../css/general/body.css"%>
        <%@include file="../css/general/header.css"%>
        <%@include file="../css/general/footer.css"%>
        <%@include file="../css/general/user.css"%>
    </style>
</head>
<body class="main_container">
<%@include file="../assets/header.jsp" %>
<div class="content_body">
    <div class="inner_content_container signing_page">
        <h1 class="h1_signing"><fmt:message bundle="${lang_bundle}" key="header.sign_in"/></h1>
        <form action="/servlet?cmd=sign_in" method="post" class="signing_form">
            <input required minlength="8" class="input mt-1" type="text"
                   placeholder="<fmt:message bundle="${lang_bundle}" key="user_page.username"/>" name="username">
            <input required minlength="8" class="input mt-1" type="password"
                   placeholder="<fmt:message bundle="${lang_bundle}" key="user_page.password"/>" name="password">
            <input class="input submit mt-1" type="submit"
                   value="<fmt:message bundle="${lang_bundle}" key="user_page.send_sign_in"/>">
        </form>
        <c:if test="${param.get('err_code') != null}">
            <p><fmt:message bundle="${lang_bundle}" key="error.${param.get('err_code')}"/></p>
        </c:if>
    </div>
</div>
<%@include file="../assets/footer.jsp" %>
</body>
</html>


