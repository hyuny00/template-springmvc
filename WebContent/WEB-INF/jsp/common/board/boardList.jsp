<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>


    <script type="text/javaScript">


    $(document).ready(function(){

    	$('#schType').select2();


    });


    function fn_list(){

    	document.listForm.pageNo.value = 1;
    	document.listForm.action = "${basePath}/common/board/${brdSeq}/list";
       	document.listForm.submit();

    }

    function fn_detail(postSeq){

    	document.listForm.action = "${basePath}/common/board/${brdSeq}/selectBoard";

    	$('#postSeq').val(postSeq);
       	document.listForm.submit();

    }


    function fn_reset(){
    	$('#schType').val("").select2();
    	$('#schContent').val("")
    }


    function fn_createForm() {
    	document.listForm.action = "${basePath}/common/board/${brdSeq}/boardForm";
       	document.listForm.submit();
    }

    </script>

<section id="section" class="section">

	<div class="main-cont-box">
		<form id="listForm" name="listForm" action="${basePath}/common/board/${brdSeq}/list" method="post">
		<input type="hidden" name="brdSeq" id="brdSeq" value="${brdSeq}">
		<input type="hidden" name="postSeq" id="postSeq" value="">


		<!-- 메뉴, 페이징 파라메터-->
		<jsp:include page="/WEB-INF/jsp/framework/_includes/includePageParam.jsp" flush="true"/>

			<h2 class="page-title">
				<span>${allMenuMap[param.menuSeq].menuNm}</span>
			</h2>
			<blockquote class="page-desc">${allMenuMap[param.menuSeq].etc}</blockquote>
			<div class="detail-search">
				<div id="detail-search" class="detail-search mT20">
					<table class="table border">
						<colgroup>
							<col style="width: 12%">
							<col >
						</colgroup>
						<tbody>
							<tr>
								<th><label for="게시판유형">게시판검색</label></th>
								<td style="text-align:left;">
									<div class="inputbox">
										<span>
											<select name="schType" id="schType">
												<option value="">전체</option>
												<option value="01" <c:if test="${param.schType eq '01'}">selected</c:if> >제목</option>
												<option value="02" <c:if test="${param.schType eq '02'}">selected</c:if> >내용</option>
												<option value="03" <c:if test="${param.schType eq '03'}">selected</c:if> >작성자</option>
											</select>
										</span>
	                                    <span style="width:100%;"><input type="text" class="input" id="schContent" name="schContent" value="${param.schContent}" maxlength="25" onKeypress="javascript:if(event.keyCode==13) {fn_list()}"></span>
                                    </div>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="list-back">
						<ul class="btn-list">
							<li><a href="javascript:fn_list();" class="btn-submit-s navy">검색</a></li>
							<li><a href="javascript:fn_reset();" class="btn-submit-s gray btn-close">초기화</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="table-scroll">
				<div class="table">
					<table class="table">
						<colgroup>
		       				<col width="8%"/>
		       				<col/>
		       				<col width="10%"/>
		       				<col width="9%"/>

		       				<c:if test="${boardInfo.replyAblYn eq 'Y'}">
		       					<col width="8%"/>
		       				 </c:if>
		       				<col width="10%"/>
		       			</colgroup>

						<thead>
							<tr>
								<th>게시글번호</th>
								<th>제목</th>
								<th>작성자</th>
								<th>조회수</th>
								<c:if test="${boardInfo.replyAblYn eq 'Y'}">
									<th>댓글수</th>
								</c:if>
								<th>작성일</th>

							</tr>
						</thead>
						<tbody>
							<c:forEach var="result" items="${list}" varStatus="status">
								<tr onclick="fn_detail('${result.postSeq}');">
									<td>
										<c:out value="${result.postSeq}" />
									</td>
									<td class="left">
										<c:out value="${result.postTtl}" />
									</td>
									<td align="left" class="listtd">
										<c:out value="${result.userNm}" />
									</td>
									<td>
										<c:out value="${result.hit}" />
									</td>

									<c:if test="${boardInfo.replyAblYn eq 'Y'}">
										<td>
											<c:out value="${result.replyCnt}" />
										</td>
									</c:if>
									<td>
										<fmt:formatDate value="${result.rgstDttm}" pattern="yyyy-MM-dd hh:mm"/>
									</td>

								</tr>
							</c:forEach>

						</tbody>
					</table>
				</div>
				<jsp:include page="/WEB-INF/jsp/framework/_includes/paging.jsp" flush="true" />

				<div class="list-back">
					<ul class="btn-list">
						<c:set var="checkUrl" value="/common/board/${brdSeq}/boardForm"/>
						<sec:authorize access="hasAnyRole(${pathAuthMap[checkUrl]})">
							<li class="mT20"><a href="javascript:fn_createForm();" class="btn-submit-s navy">등록</a></li>
						</sec:authorize>


					</ul>
				</div>
			</div>
			<!-- //end) .table-scroll -->
		</form>
	</div>
	<!-- //end) .main-cont-box -->
</section>