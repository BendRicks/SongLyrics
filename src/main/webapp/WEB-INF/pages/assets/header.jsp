<header class="header">
    <div class="header_block">
        <a href="/servlet?cmd=main_page" class="logo">SongLyr</a>
        <a href="/servlet?cmd=track_search" class="nav_tab">
            <nav class="nav_tab_text"><fmt:message bundle="${lang_bundle}" key="header.tracks"/></nav>
        </a>
        <a href="/servlet?cmd=album_search" class="nav_tab">
            <nav class="nav_tab_text"><fmt:message bundle="${lang_bundle}" key="header.albums"/></nav>
        </a>
        <a href="/servlet?cmd=performer_search" class="nav_tab">
            <nav class="nav_tab_text"><fmt:message bundle="${lang_bundle}" key="header.performers"/></nav>
        </a>
        <c:if test="${sessionScope.get('USER_SESSION_INFO') != null && sessionScope.get('USER_SESSION_INFO').getRoleId() == 1}">
            <a href="/servlet?cmd=user_search" class="nav_tab">
                <nav class="nav_tab_text"><fmt:message bundle="${lang_bundle}" key="header.users"/></nav>
            </a>
        </c:if>
    </div>
    <div class="header_block">
        <c:choose>
            <c:when test="${sessionScope.get('USER_SESSION_INFO') != null}">
                <a href="/servlet?cmd=user_page" class="user_link"><c:out
                        value="${sessionScope.get('USER_SESSION_INFO').getUsername()}"/></a>
                <a href="/servlet?cmd=log_out" class="user_link"><fmt:message bundle="${lang_bundle}" key="header.log_out"/></a>
            </c:when>
            <c:otherwise>
                <a href="/servlet?cmd=sign_up" class="account_action"><fmt:message bundle="${lang_bundle}" key="header.sign_up"/></a>
                <a href="/servlet?cmd=sign_in" class="account_action"><fmt:message bundle="${lang_bundle}" key="header.sign_in"/></a>
            </c:otherwise>
        </c:choose>
    </div>
</header>