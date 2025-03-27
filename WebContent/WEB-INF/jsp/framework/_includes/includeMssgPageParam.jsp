<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>

<input type="hidden" name="topMenuSeq" value="${param.topMenuSeq}" id="topMenuSeq"/>
<input type="hidden" name="menuSeq" value="${param.menuSeq}" id="menuSeq" />
<input type="hidden" name="upMenuSeq" value="${param.upMenuSeq}" id="upMenuSeq"/>

<input type="hidden" name="pageNo1" value="<c:if test="${empty params.pageNo1}">1</c:if><c:if test="${not empty params.pageNo1}">${params.pageNo1}</c:if>"/>
<input type="hidden" name="pageSize1" value="<c:if test="${empty params.pageSize1}">10</c:if><c:if test="${not empty params.pageSize1}">${params.pageSize}</c:if>"/>

<input type="hidden" name="pageNo2" value="<c:if test="${empty params.pageNo2}">1</c:if><c:if test="${not empty params.pageNo2}">${params.pageNo2}</c:if>"/>
<input type="hidden" name="pageSize2" value="<c:if test="${empty params.pageSize2}">10</c:if><c:if test="${not empty params.pageSize2}">${params.pageSize2}</c:if>"/>


<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
