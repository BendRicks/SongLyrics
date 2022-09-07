<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="assets/localisation.jsp"%>
    <title>SongLyr</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@200&display=swap" rel="stylesheet">
    <style>
        <%@include file="css/general/body.css"%>
        <%@include file="css/general/header.css"%>
        <%@include file="css/general/footer.css"%>
        <%@include file="css/main_page.css"%>
    </style>
</head>
<body class="main_container">
<%@include file="assets/header.jsp"%>
<div class="content_body">
    <div class="inner_content_container main_page">
        <div class="page_content">
            <h1 class="page_h1">SongLyr</h1>
            <img src="https://i.ytimg.com/vi/AxStWpBWeUg/maxresdefault.jpg" class="gigachad">
            <p class="description_text"><fmt:message bundle="${lang_bundle}" key="main_page.description_text"/></p><br/>
            <p class="partnership_text"><fmt:message bundle="${lang_bundle}" key="main_page.partnership_text"/></p>
        </div>
        <c:if test="${param.get('err_code') != null}">
            <p><fmt:message bundle="${lang_bundle}" key="error.${param.get('err_code')}"/></p>
        </c:if>
    </div>
</div>
<%@include file="assets/footer.jsp"%>
</body>
</html>
