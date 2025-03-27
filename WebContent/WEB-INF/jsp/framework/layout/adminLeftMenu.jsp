<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf"%>

<script>
	function logout() {
		document.logoutForm.submit();
	}


	function updateUserForm() {
		document.logoutForm.action="/admin/user/detailUser";
		document.logoutForm.submit();
	}


</script>


<aside class="left-side-menu">

	<div class="user-info">
		<div class="user-info-profile">
			<a class="avatar" href="#">
				<i></i>
			</a>
			<sec:authorize access="isAuthenticated()">
				<span class="name"><span>${user.userNm}</span>님<br>환영합니다</span>
			</sec:authorize>
		</div>
		<div class="user-info-option">
			<a class="myinfo" href="javascript:updateUserForm();">
					개인정보관리<i></i>
			</a>

			<sec:authorize access="isAuthenticated()">
				<a class="logout" href="javascript:logout();">
					로그아웃<i></i>
				</a>
			</sec:authorize>
		</div>
		<%--
		<ul class="user-info-nums">
			<li><span class="tit"><i></i>미처리건</span><span class="num"><b>11</b>건</span></li>
			<li><span class="tit"><i></i>결재대기함</span><span class="num"><b>3</b>건</span></li>
		</ul>
		 --%>
	</div>



	<c:forEach items="${menuList.topMenuList}" var="topMenuList">
		<c:if test="${topMenuList.menuSeq eq param.topMenuSeq}">
			<c:set var="topMenuNm" value="${topMenuList.menuNm}" />
		</c:if>
	</c:forEach>


	<ul class="left-side-list" style="display: block;">
		<li class="tit">${topMenuNm}</li>
			<c:forEach items="${subMenuList}" var="subMenuList" >
				<c:if test="${ subMenuList.menuTypeCd eq '020'   or subMenuList.menuTypeCd eq '030' }">

						<li class="<c:if test="${param.upMenuSeq eq subMenuList.menuSeq}">active</c:if>">
								<c:set var="loop_flag" value="false" />
								<c:if test="${ subMenuList.subMenuCnt eq '0'  }">
									<c:forEach items="${authMenuList}" var="authMenuList">
									<c:if test="${not loop_flag}">
										<c:if test="${subMenuList.menuSeq eq authMenuList.menuSeq}">
											<a href="${subMenuList.menuUrl}?menuSeq=${subMenuList.menuSeq}&topMenuSeq=${param.topMenuSeq}">${subMenuList.menuNm}</a>
											<c:set var="loop_flag" value="true" />
										</c:if>
									</c:if>
									</c:forEach>
								</c:if>


								<c:set var="loop_flag" value="false" />
								<c:forEach items="${authMenuList}" var="authMenuList">
									<c:if test="${not loop_flag}">
										<c:if test="${subMenuList.menuSeq eq authMenuList.upMenuSeq}">
											<a href="#">${subMenuList.menuNm}<i class="fas fa-chevron-right"></i></a>
											<c:set var="loop_flag" value="true" />
										</c:if>
									</c:if>
								</c:forEach>
								<ul <c:if test="${param.upMenuSeq eq subMenuList.menuSeq}">style="display:block"</c:if> >
									<c:forEach items="${authMenuList}" var="authMenuList">
										<c:if test="${subMenuList.menuSeq eq authMenuList.upMenuSeq}">
											<li class="<c:if test="${param.menuSeq eq authMenuList.menuSeq}">active</c:if>">
												<a href="${basePath}${authMenuList.menuUrl}?menuSeq=${authMenuList.menuSeq}&topMenuSeq=${param.topMenuSeq}&upMenuSeq=${authMenuList.upMenuSeq}">${authMenuList.menuNm}</a>
											</li>
										</c:if>
									</c:forEach>
								</ul>
						</li>
			</c:if>
		</c:forEach>
	</ul>
</aside>
<form id="logoutForm" name="logoutForm" action="${basePath}/logout" method="POST">
	<input type="hidden" name="${ _csrf.parameterName }" value="${ _csrf.token }">
	<input type="hidden" name="userNo" value="${user.userNo}">
</form>
