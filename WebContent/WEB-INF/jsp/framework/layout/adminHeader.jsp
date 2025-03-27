<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf"%>
<header class="header" id="header">




	<nav class="gnb">
		<h1 class="logo">
			<a href="/main">
				<img alt="" src="${basePath}/img/logo.png">ODA통합정보포털
			</a>
		</h1>
		<ul>
			<c:forEach items="${menuList.topMenuList}" var="topMenuList">
				<c:forEach items="${topMenuSeqList}" var="topMenuSeqList">
					<c:if test="${topMenuSeqList eq topMenuList.menuSeq }">
						<c:if test="${topMenuSeqList eq 1 or  topMenuList.menuSeq eq '12'}">
							<li class="<c:if test="${param.topMenuSeq eq topMenuList.menuSeq}">active</c:if>">
								<c:if test="${empty topMenuList.menuUrl }">
								  	<c:set var="andChar" value="?" />
								</c:if>
								<c:if test="${not empty topMenuList.menuUrl }">
								  	<c:set var="andChar" value="&" />
								</c:if>
								<a title="${topMenuList.menuNm}" href="${topMenuList.menuUrl}${andChar}topMenuSeq=${topMenuList.menuSeq}">${topMenuList.menuNm}</a>
							</li>
						</c:if>
					</c:if>
				</c:forEach>
			</c:forEach>


		</ul>
		<form id="searechForm" name="searechForm" action="${basePath}/searech" method="POST">
			<input type="hidden" name="${ _csrf.parameterName }" value="${ _csrf.token }">
			<div class="search">
				<input class="input" type="text" placeholder="검색 조건 입력">
				<button type="submit">검색</button>
			</div>
		</form>
	</nav>
</header>

