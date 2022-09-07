<div class="search_bar_container">
    <form action="<c:out value="${form_action}"/>" method="post" class="search_form">
        <input class="search_input" type="text" id="search_input" name="query"
               placeholder="<fmt:message bundle="${lang_bundle}" key="search.input_placeholder"/>">
        <div>
            <input type="radio" name="verification" id="all_radio" value="0" checked>
            <label for="all_radio"><fmt:message bundle="${lang_bundle}" key="search.all"/></label>
            <input type="radio" name="verification" id="verified" value="1">
            <label for="verified"><fmt:message bundle="${lang_bundle}" key="search.verified"/></label>
            <input type="radio" name="verification" id="not_verified" value="2">
            <label for="not_verified"><fmt:message bundle="${lang_bundle}" key="search.not_verified"/></label>
            <c:if test="${sessionScope.get('USER_SESSION_INFO') != null && sessionScope.get('USER_SESSION_INFO').getRoleId() != 3}">
                <input type="radio" name="verification" id="banned" value="3">
                <label for="banned"><fmt:message bundle="${lang_bundle}" key="search.limited"/></label>
            </c:if>
            <input type="submit" value="<fmt:message bundle="${lang_bundle}" key="search.search"/>">
        </div>
    </form>
</div>