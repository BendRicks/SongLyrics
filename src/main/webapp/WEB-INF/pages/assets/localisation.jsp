<c:set var="locale" value="${sessionScope.get('locale')}"/>
<c:if test="${locale == null}">
  <fmt:setLocale value="ru"/>
</c:if>
<c:if test="${locale != null}">
  <fmt:setLocale value="${locale}"/>
</c:if>
<fmt:setBundle basename="lang" var="lang_bundle"/>