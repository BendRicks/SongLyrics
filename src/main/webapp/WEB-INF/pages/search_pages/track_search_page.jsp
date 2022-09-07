<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <c:set var="user" value="${sessionScope.get('USER_SESSION_INFO')}"/>
    <%@include file="../assets/localisation.jsp" %>
    <title><fmt:message bundle="${lang_bundle}" key="title.track_search"/></title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@200&display=swap" rel="stylesheet">
    <style>
        <%@include file="../css/general/body.css"%>
        <%@include file="../css/general/header.css"%>
        <%@include file="../css/general/footer.css"%>
        <%@include file="../css/general/search.css"%>
    </style>
</head>
<body class="main_container">
<%@include file="../assets/header.jsp" %>
<div class="content_body">
    <div class="inner_content_container search_page">
        <h1 class="page_h1"><fmt:message bundle="${lang_bundle}" key="title.track_search"/></h1>
        <c:set value="/servlet?cmd=track_search" var="form_action"/>
        <%@include file="../assets/search_menu.jsp" %>
        <a href="/servlet?cmd=create_track"><fmt:message bundle="${lang_bundle}"
                                                         key="search.create_new_track"/></a>
        <div class="search_result_block">
            <c:forEach var="item" items="${sessionScope.get('tracks')}">
                <c:if test="${(item.getStatus() != 3 || (item.getStatus() == 3 && user != null && user.getRoleId() != 3))}">
                    <a href="/servlet?cmd=get_track&item_id=${item.getId()}" class="search_item item_small_cover">
                        <div class="item_part">
                            <img src="${item.getTrackAlbums().get(0).getCoverImagePath()}" class="small_cover"/>
                            <h2 class="item_small_name"><c:out value="${item.getName()}"/></h2>
                            <span>
                                <c:choose>
                                    <c:when test="${item.getStatus() == 1}"><fmt:message bundle="${lang_bundle}"
                                                                                         key="search.verified"/></c:when>
                                    <c:when test="${item.getStatus() == 2}"><fmt:message bundle="${lang_bundle}"
                                                                                         key="search.not_verified"/></c:when>
                                    <c:otherwise><fmt:message bundle="${lang_bundle}"
                                                              key="search.limited"/></c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <p class="item_part item_id">#<c:out value="${item.getId()}"/></p>
                    </a>
                </c:if>
            </c:forEach>
        </div>
        <div class="pages">
            <c:set var="page" value="${sessionScope.get('page')}"/>
            <c:forEach begin="1" end="${sessionScope.get('pages')}" var="i">
                <c:choose>
                    <c:when test="${i != page}">
                        <a href="/servlet?cmd=turn_track_search_page&page=${i}"><c:out value="${i}"/></a>
                    </c:when>
                    <c:otherwise>
                        <span><c:out value="${i}"/></span>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <c:if test="${param.get('err_code') != null}">
            <p><fmt:message bundle="${lang_bundle}" key="error.${param.get('err_code')}"/></p>
        </c:if>
    </div>
</div>
<%@include file="../assets/footer.jsp" %>
</body>
</html>
