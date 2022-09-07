<%@ page import="entity.UserSession" %>
<%@ page import="dao.DAOConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    if (session.getAttribute("USER_SESSION_INFO") == null
            || ((UserSession) session.getAttribute("USER_SESSION_INFO")).getRoleId() != DAOConstants.ADMIN_ID) {
        response.sendRedirect("/servlet?cmd=main_page");
    }
%>
<html>
<head>
    <%@include file="../../assets/localisation.jsp" %>
    <title><fmt:message bundle="${lang_bundle}" key="title.track_search"/></title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@200&display=swap" rel="stylesheet">
    <style>
        <%@include file="../../css/general/body.css"%>
        <%@include file="../../css/general/header.css"%>
        <%@include file="../../css/general/footer.css"%>
        <%@include file="../../css/general/search.css"%>
    </style>
</head>
<body class="main_container">
<%@include file="../../assets/header.jsp" %>
<div class="content_body">
    <div class="inner_content_container search_page">
        <h1 class="page_h1"><fmt:message bundle="${lang_bundle}" key="title.track_search"/></h1>
        <div class="search_bar_container">
            <form action="/servlet?cmd=user_search" method="post" class="search_form">
                <input class="search_input" type="text" required id="search_input" name="query"
                       placeholder="<fmt:message bundle="${lang_bundle}" key="search.input_placeholder"/>">
                <div>
                    <input type="radio" name="acc_status" id="all_statuses" value="0" checked>
                    <label for="all_statuses"><fmt:message bundle="${lang_bundle}" key="search.all"/></label>
                    <input type="radio" name="acc_status" id="innocent" value="1">
                    <label for="innocent"><fmt:message bundle="${lang_bundle}" key="search.innocent"/></label>
                    <input type="radio" name="acc_status" id="warned" value="2">
                    <label for="warned"><fmt:message bundle="${lang_bundle}" key="search.warned"/></label>
                    <input type="radio" name="acc_status" id="banned" value="3">
                    <label for="banned"><fmt:message bundle="${lang_bundle}" key="search.banned"/></label>
                </div>
                <div>
                    <input type="radio" name="role" id="all_roles" value="0" checked>
                    <label for="all_roles"><fmt:message bundle="${lang_bundle}" key="search.all"/></label>
                    <input type="radio" name="role" id="admin" value="1">
                    <label for="admin"><fmt:message bundle="${lang_bundle}" key="search.admin"/></label>
                    <input type="radio" name="role" id="moderator" value="2">
                    <label for="moderator"><fmt:message bundle="${lang_bundle}" key="search.moderator"/></label>
                    <input type="radio" name="role" id="user" value="3">
                    <label for="user"><fmt:message bundle="${lang_bundle}" key="search.user"/></label>
                </div>
                <input type="submit" value="<fmt:message bundle="${lang_bundle}" key="search.search"/>">
            </form>
        </div>
        <div class="search_result_block">
            <c:forEach var="item" items="${sessionScope.get('users')}">
                <div class="search_item item_small_cover">
                    <div class="item_part">
                        <span class="item_part item_id">#<c:out value="${item.getId()}"/> </span>
                        <h2 class="item_small_name"><c:out value="${item.getUsername()}"/></h2>
                        <span>
                            <c:choose>
                                <c:when test="${item.getRoleId() == 1}"> <fmt:message bundle="${lang_bundle}"
                                                                                      key="search.admin"/>:</c:when>
                                <c:when test="${item.getRoleId() == 2}"> <fmt:message bundle="${lang_bundle}"
                                                                                      key="search.moderator"/>:</c:when>
                                <c:otherwise> <fmt:message bundle="${lang_bundle}" key="search.user"/>:</c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${item.getStatusId() == 1}"> <fmt:message bundle="${lang_bundle}"
                                                                                        key="search.innocent"/>
                                </c:when>
                                <c:when test="${item.getStatusId() == 2}"> <fmt:message bundle="${lang_bundle}"
                                                                                        key="search.warned"/> </c:when>
                                <c:otherwise> <fmt:message bundle="${lang_bundle}" key="search.banned"/></c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    <c:if test="${item.getRoleId() != 1}">
                        <div>
                            <form class="manip_button"
                                  action="/servlet?cmd=change_role&role_id=2&user_id=${item.getId()}" method="post">
                                <input type="submit"
                                       value="<fmt:message bundle="${lang_bundle}" key="search.moderator"/>"></form>
                            <form class="manip_button"
                                  action="/servlet?cmd=change_role&role_id=3&user_id=${item.getId()}" method="post">
                                <input type="submit" value="<fmt:message bundle="${lang_bundle}" key="search.user"/>">
                            </form>
                        </div>
                        <div>
                            <form class="manip_button"
                                  action="/servlet?cmd=change_user_status&status_id=1&user_id=${item.getId()}" method="post">
                                <input type="submit" value="<fmt:message bundle="${lang_bundle}" key="admin.amnesty"/>">
                            </form>
                            <form class="manip_button"
                                  action="/servlet?cmd=change_user_status&status_id=2&user_id=${item.getId()}" method="post">
                                <input type="submit" value="<fmt:message bundle="${lang_bundle}" key="admin.warn"/>">
                            </form>
                            <form class="manip_button"
                                  action="/servlet?cmd=change_user_status&status_id=3&user_id=${item.getId()}" method="post">
                                <input type="submit" value="<fmt:message bundle="${lang_bundle}" key="admin.ban"/>">
                            </form>
                        </div>
                    </c:if>
                </div>
            </c:forEach>
        </div>
        <div class="pages">
            <c:set var="page" value="${sessionScope.get('page')}"/>
            <c:forEach begin="1" end="${sessionScope.get('pages')}" var="i">
                <c:choose>
                    <c:when test="${i != page}">
                        <a href="/servlet?cmd=turn_users_search_page&page=${i}"><c:out value="${i}"/></a>
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
<%@include file="../../assets/footer.jsp" %>
</body>
</html>
