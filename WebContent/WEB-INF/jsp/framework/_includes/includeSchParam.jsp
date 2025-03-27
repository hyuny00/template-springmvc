<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>
<c:forEach var="par" items="${paramValues}">
    <c:if test="${fn:startsWith(par.key, 'sch')}">
    	 <c:if test="${par.key ne 'schOrg1stNm' and par.key ne 'schOrg2ndNm' }">
		  	<input type="hidden" name="${par.key}" value="${par.value[0]}"/>
		 </c:if>
    </c:if>
</c:forEach>
