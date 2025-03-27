<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>


<c:if test="${params.typeCd eq 'receive'}">
	<th>첨부파일</th>
	<td id='123'>
		<jsp:include page="/file/uploadForm" flush="true">
			<jsp:param name="docId" value="${receiveDetail.attcDocId }"/>
			<jsp:param name="mode" value="view"/>
	 		<jsp:param name="viewType" value="V"/>
		</jsp:include>
	</td>
</c:if>

<c:if test="${params.typeCd eq 'send'}">
	<th>첨부파일</th>
	<td id='123'>
		<jsp:include page="/file/uploadForm" flush="true">
			<jsp:param name="docId" value="${sendDetail.attcDocId }"/>
			<jsp:param name="mode" value="view"/>
	 		<jsp:param name="viewType" value="V"/>
		</jsp:include>
	</td>
</c:if>
