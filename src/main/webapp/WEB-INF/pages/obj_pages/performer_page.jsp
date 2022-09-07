<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="../assets/localisation.jsp" %>
    <c:set var="item" value="${sessionScope.get('performer')}"/>
    <title><c:out value="${item.getName()}"/></title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@200&display=swap" rel="stylesheet">
    <style>
        <%@include file="../css/general/body.css"%>
        <%@include file="../css/general/header.css"%>
        <%@include file="../css/general/footer.css"%>
        <%@include file="../css/general/search.css"%>
        <%@include file="../css/general/object.css"%>
    </style>
</head>
<body class="main_container">
<c:set var="user" value="${sessionScope.get('USER_SESSION_INFO')}"/>
<%@include file="../assets/header.jsp" %>
<div class="content_body">
    <div class="inner_content_container object_page">
        <div class="description_block">
            <img class="object_cover" src="${item.getCoverImagePath()}" alt="performer cover">
            <div class="text_about">
                <h1 class="item_big_name">#<c:out value="${item.getId()}"/> - <c:out value="${item.getName()}"/></h1>
                <p class="description_p"><c:out value="${item.getDescription()}"/></p>
                <div class="performer_creator">
                    <span><fmt:message bundle="${lang_bundle}" key="object_pages.creator"/>: <c:out
                            value="${sessionScope.get('performer_creator').getUsername()}"/></span>
                </div>
                <div class="performer_verification">
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
                <c:if test="${user != null && (sessionScope.get('performer_creator').getId() == user.getId()
                || user.getRoleId() != 3)}">
                    <a href="/servlet?cmd=change_performer"><fmt:message bundle="${lang_bundle}"
                                                                         key="object_pages.change"/></a>
                </c:if>
                <c:if test="${user != null && user.getRoleId() != 3}">
                    <div class="object_managing">
                        <form method="post"
                              action="/servlet?cmd=change_performer_status&status_id=1&item_id=${item.getId()}"><input
                                type="submit" value="<fmt:message bundle="${lang_bundle}" key="moderation.verify"/>">
                        </form>
                        <form method="post"
                              action="/servlet?cmd=change_performer_status&status_id=2&item_id=${item.getId()}"><input
                                type="submit" value="<fmt:message bundle="${lang_bundle}" key="moderation.unverify"/>">
                        </form>
                        <form method="post"
                              action="/servlet?cmd=change_performer_status&status_id=3&item_id=${item.getId()}"><input
                                type="submit" value="<fmt:message bundle="${lang_bundle}" key="moderation.limit"/>">
                        </form>
                    </div>
                </c:if>
            </div>
        </div>
        <div class="albums_block">
            <h2 class="block_h2"><fmt:message bundle="${lang_bundle}" key="header.albums"/></h2>
            <c:forEach var="album" items="${item.getPerformerAlbums()}">
                <c:if test="${album.getStatus() != 3 || (album.getStatus() == 3 && user != null && user.getRoleId() != 3)}">
                    <a href="/servlet?cmd=get_album&item_id=${album.getId()}" class="search_item item_big_cover">
                        <div class="item_part">
                            <img src="${album.getCoverImagePath()}" class="big_cover"/>
                            <h3 class="item_big_name"><c:out value="${album.getName()}"/></h3>
                            <span>
                                <c:choose>
                                    <c:when test="${album.getStatus() == 1}"><fmt:message bundle="${lang_bundle}"
                                                                                          key="search.verified"/></c:when>
                                    <c:when test="${album.getStatus() == 2}"><fmt:message bundle="${lang_bundle}"
                                                                                          key="search.not_verified"/></c:when>
                                    <c:otherwise><fmt:message bundle="${lang_bundle}"
                                                              key="search.limited"/></c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <p class="item_part item_id">#<c:out value="${album.getId()}"/></p>
                    </a>
                </c:if>
            </c:forEach>
        </div>
        <div class="tracks_block">
            <h2 class="block_h2"><fmt:message bundle="${lang_bundle}" key="header.tracks"/></h2>
            <c:forEach var="track" items="${item.getPerformerTracks()}">
                <c:if test="${track.getStatus() != 3 || (track.getStatus() == 3 && user != null && user.getRoleId() != 3)}">
                    <a href="/servlet?cmd=get_track&item_id=${track.getId()}" class="search_item item_small_cover">
                        <div class="item_part">
                            <img src="${track.getTrackAlbums().get(0).getCoverImagePath()}" class="small_cover"/>
                            <h3 class="item_small_name"><c:out value="${track.getName()}"/></h3>
                            <span>
                                <c:choose>
                                    <c:when test="${track.getStatus() == 1}"><fmt:message bundle="${lang_bundle}"
                                                                                          key="search.verified"/></c:when>
                                    <c:when test="${track.getStatus() == 2}"><fmt:message bundle="${lang_bundle}"
                                                                                          key="search.not_verified"/></c:when>
                                    <c:otherwise><fmt:message bundle="${lang_bundle}"
                                                              key="search.limited"/></c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <p class="item_part item_id">#<c:out value="${track.getId()}"/></p>
                    </a>
                </c:if>
            </c:forEach>
            <c:if test="${param.get('err_code') != null}">
                <p><fmt:message bundle="${lang_bundle}" key="error.${param.get('err_code')}"/></p>
            </c:if>
        </div>
    </div>
</div>
<%@include file="../assets/footer.jsp" %>
</body>
</html>
