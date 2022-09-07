<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <c:set var="item" value="${sessionScope.get('track')}"/>
    <title><c:out value="${item.getName()}"/></title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@200&display=swap" rel="stylesheet">
    <%@include file="../assets/localisation.jsp" %>
    <style>
        <%@include file="../css/general/body.css"%>
        <%@include file="../css/general/header.css"%>
        <%@include file="../css/general/footer.css"%>
        <%@include file="../css/general/object.css"%>
    </style>
</head>
<body class="main_container">
<c:set var="user" value="${sessionScope.get('USER_SESSION_INFO')}"/>
<%@include file="../assets/header.jsp" %>
<div class="content_body">
    <div class="inner_content_container object_page">
        <div class="description_block">
            <img class="object_cover" src="${item.getTrackAlbums().get(0).getCoverImagePath()}" alt="performer cover">
            <div class="text_about">
                <h1 class="item_big_name">#<c:out value="${item.getId()}"/> - <c:out value="${item.getName()}"/></h1>
                <div class="track_albums">
                    <span><fmt:message bundle="${lang_bundle}" key="header.albums"/>: </span>
                    <c:forEach var="album" items="${item.getTrackAlbums()}">
                        <a href="servlet?cmd=get_album&item_id=${album.getId()}"><c:out value="${album.getName()}"/></a><span>, </span>
                    </c:forEach>
                </div>
                <div class="track_performers">
                    <span><fmt:message bundle="${lang_bundle}" key="header.performers"/>: </span>
                    <c:forEach var="performer" items="${item.getTrackPerformers()}">
                        <a href="servlet?cmd=get_performer&item_id=${performer.getId()}"><c:out
                                value="${performer.getName()}"/></a><span>, </span>
                    </c:forEach>
                </div>
                <div class="track_creator">
                    <span><fmt:message bundle="${lang_bundle}" key="object_pages.creator"/>: <c:out
                            value="${sessionScope.get('track_creator').getUsername()}"/></span>
                </div>
                <div class="track_verification">
                    <span>
                        <c:choose>
                            <c:when test="${item.getStatus() == 1}"><fmt:message bundle="${lang_bundle}"
                                                                                 key="search.verified"/></c:when>
                            <c:when test="${item.getStatus() == 2}"><fmt:message bundle="${lang_bundle}"
                                                                                 key="search.not_verified"/></c:when>
                            <c:otherwise><fmt:message bundle="${lang_bundle}" key="search.limited"/></c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <c:if test="${user != null && (sessionScope.get('track_creator').getId() == user.getId()
                || user.getRoleId() != 3)}">
                    <a href="/servlet?cmd=change_track"><fmt:message bundle="${lang_bundle}"
                                                                     key="object_pages.change"/></a>
                </c:if>
                <c:if test="${user != null && user.getRoleId() != 3}">
                    <div class="object_managing">
                        <form method="post"
                              action="/servlet?cmd=change_track_status&status_id=1&item_id=${item.getId()}"><input
                                type="submit" value="<fmt:message bundle="${lang_bundle}" key="moderation.verify"/>">
                        </form>
                        <form method="post"
                              action="/servlet?cmd=change_track_status&status_id=2&item_id=${item.getId()}"><input
                                type="submit" value="<fmt:message bundle="${lang_bundle}" key="moderation.unverify"/>">
                        </form>
                        <form method="post"
                              action="/servlet?cmd=change_track_status&status_id=3&item_id=${item.getId()}"><input
                                type="submit" value="<fmt:message bundle="${lang_bundle}" key="moderation.limit"/>">
                        </form>
                    </div>
                </c:if>
            </div>
        </div>
        <p class="description_p"><c:out value="${item.getLyrics()}"/></p>
        <c:if test="${param.get('err_code') != null}">
            <p><fmt:message bundle="${lang_bundle}" key="error.${param.get('err_code')}"/></p>
        </c:if>
    </div>
</div>
<%@include file="../assets/footer.jsp" %>
</body>
</html>
