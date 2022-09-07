<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="../assets/localisation.jsp" %>
    <title><fmt:message bundle="${lang_bundle}" key="title.user_page"/></title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@200&display=swap" rel="stylesheet">
    <style>
        <%@include file="../css/general/body.css"%>
        <%@include file="../css/general/header.css"%>
        <%@include file="../css/general/footer.css"%>
        <%@include file="../css/general/object.css"%>
        <%@include file="../css/general/user.css"%>
    </style>
</head>
<body class="main_container">
<%@include file="../assets/header.jsp" %>
<div class="content_body">
    <div class="inner_content_container object_page">
        <h1 class="page_h1"><fmt:message bundle="${lang_bundle}" key="title.user_page"/></h1>
        <div class="managing_container">
            <div class="acc_manage_block">
                <form method="post" action="/servlet?cmd=change_username" autocomplete="off">
                    <label for="new_username"><fmt:message bundle="${lang_bundle}"
                                                           key="user_page.new_username"/></label><input type="text"
                                                                                                        required
                                                                                                        minlength="8"
                                                                                                        class="input"
                                                                                                        id="new_username"
                                                                                                        name="new_username"
                                                                                                        placeholder="<fmt:message bundle="${lang_bundle}" key="user_page.new_username"/>">
                    <label for="password"><fmt:message bundle="${lang_bundle}" key="user_page.password"/></label><input
                        type="password"
                        required minlength="8"
                        id="password" name="password"
                        class="input"
                        placeholder="<fmt:message bundle="${lang_bundle}" key="user_page.password"/>">
                    <input type="submit" class="input submit"
                           value="<fmt:message bundle="${lang_bundle}" key="user_page.submit"/>">
                </form>
            </div>
            <div class="acc_manage_block">
                <form method="post" action="/servlet?cmd=change_password">
                    <label for="old_password"><fmt:message bundle="${lang_bundle}"
                                                           key="user_page.old_password"/></label><input type="password"
                                                                                                        required
                                                                                                        minlength="8"
                                                                                                        class="input"
                                                                                                        id="old_password"
                                                                                                        name="old_password"
                                                                                                        placeholder="<fmt:message bundle="${lang_bundle}" key="user_page.old_password"/>">
                    <label for="new_password"><fmt:message bundle="${lang_bundle}"
                                                           key="user_page.new_password"/></label><input type="password"
                                                                                                        required
                                                                                                        minlength="8"
                                                                                                        id="new_password"
                                                                                                        name="new_password"
                                                                                                        class="input"
                                                                                                        placeholder="<fmt:message bundle="${lang_bundle}" key="user_page.new_password"/>">
                    <label for="new_password_rep"><fmt:message bundle="${lang_bundle}"
                                                               key="user_page.new_password_rep"/></label><input
                        required minlength="8"
                        type="password" id="new_password_rep"
                        name="new_password_rep"
                        class="input"
                        placeholder="<fmt:message bundle="${lang_bundle}" key="user_page.new_password_rep"/>">
                    <input type="submit" class="input submit"
                           value="<fmt:message bundle="${lang_bundle}" key="user_page.submit"/>">
                </form>
            </div>
        </div>
        <div class="creation_labels">
            <p><fmt:message bundle="${lang_bundle}" key="header.performers"/></p>
            <p><fmt:message bundle="${lang_bundle}" key="header.albums"/></p>
            <p><fmt:message bundle="${lang_bundle}" key="header.tracks"/></p>
        </div>
        <div class="user_creations">
            <div class="user_creations_block">
                <c:forEach var="performer" items="${sessionScope.get('user_performers')}">
                    <a class="user_creation_link" href="/servlet?cmd=get_performer&item_id=${performer.getId()}"><c:out
                            value="${performer.getName()}"/></a>
                </c:forEach>
            </div>
            <div class="user_creations_block">
                <c:forEach var="album" items="${sessionScope.get('user_albums')}">
                    <a class="user_creation_link" href="/servlet?cmd=get_album&item_id=${album.getId()}"><c:out
                            value="${album.getName()}"/></a>
                </c:forEach>
            </div>
            <div class="user_creations_block">
                <c:forEach var="track" items="${sessionScope.get('user_tracks')}">
                    <a class="user_creation_link" href="/servlet?cmd=get_track&item_id=${track.getId()}"><c:out
                            value="${track.getName()}"/></a>
                </c:forEach>
            </div>
        </div>
        <c:if test="${param.get('err_code') != null}">
            <p><fmt:message bundle="${lang_bundle}" key="error.${param.get('err_code')}"/></p>
        </c:if>
    </div>
</div>
<%@include file="../assets/footer.jsp" %>
</body>
</html>
