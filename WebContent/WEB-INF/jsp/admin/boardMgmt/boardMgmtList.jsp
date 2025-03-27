<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>


    <script type="text/javaScript">


    $(document).ready(function(){

    	$('#schBrdTypeCd').select2();
    	$('#schUseYn').select2();


    });


    function fn_list(){

    	document.listForm.pageNo.value = 1;
    	document.listForm.action = "${basePath}/admin/boardMgmt/selectBoardMgmtList";
       	document.listForm.submit();

    }

    function fn_detail(brdSeq){

    	$('#brdSeq').val(brdSeq);
    	document.listForm.action = "${basePath}/admin/boardMgmt/selectBoardMgmt";
       	document.listForm.submit();

    }


    function fn_reset(){
    	$('#schBrdTypeCd').val("").select2();
    	$('#schUseYn').val("").select2();
    }


    function fn_createForm() {
    	document.listForm.action = "${basePath}/admin/boardMgmt/boardMgmtForm";
       	document.listForm.submit();
    }

    </script>

<section id="section" class="section">

	<div class="main-cont-box">
		<form id="listForm" name="listForm" action="${basePath}/admin/boardMgmt/selectBoardMgmtList" method="post">
		<!-- 메뉴, 페이징 파라메터-->
		<jsp:include page="/WEB-INF/jsp/framework/_includes/includePageParam.jsp" flush="true"/>
		<input type="hidden" name="brdSeq" id="brdSeq">

			<h2 class="page-title">
				<span>게시판관리 </span>
			</h2>
			<blockquote class="page-desc">게시판을 관리할 수 있습니다</blockquote>
			<div class="detail-search">
				<div id="detail-search" class="detail-search mT20">
					<table class="table border">
						<colgroup>
							<col style="width:12%;">
							<col style="width:38%;">
							<col style="width:12%;">
							<col>
						</colgroup>
						<tbody>
							<tr>
								<th><label for="schBrdTypeCd">게시판유형</label></th>
								<td>
									<div class="selectbox">
										<select name="schBrdTypeCd" id="schBrdTypeCd">
											<option value="">전체</option>
											<c:forEach var="code" items="${brdTypeList}" varStatus="status">
												<option value="${code.code}" <c:if test="${code.code eq param.schBrdTypeCd }">selected</c:if>>${code.value}</option>
											</c:forEach>
										</select>
									</div>
								</td>
								<th><label for="schUseYn">사용여부</label></th>
								<td>
									<div class="selectbox">
										<select name="schUseYn" id="schUseYn">
											<option value="">전체</option>
											<option value="Y" <c:if test="${param.schUseYn eq 'Y'}">selected </c:if>>Y</option>
											<option value="N" <c:if test="${param.schUseYn eq 'N'}">selected </c:if>>N</option>
										</select>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="list-back">
						<ul class="btn-list">
							<li><a href="javascript:fn_list();" class="btn-submit-s stblue">검색</a></li>
							<li><a href="javascript:fn_reset();" class="btn-submit-s gray btn-close">초기화</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="table-scroll">
				<div class="table">
					<table class="table">
						<colgroup>
		       				<col style="width:8%;"/>
		       				<col/>
		       				<col style="width:15%;"/>
		       				<col style="width:45%;"/>
		       				<col style="width:8%;"/>
		       				<col style="width:8%;"/>
		       			</colgroup>
						<thead>
							<tr>
								<th>게시판번호</th>
								<th>게시판명</th>
								<th>게시판유형</th>
								<th>게시판url</th>
								<th>생성일</th>
								<th>사용여부</th>

							</tr>
						</thead>
						<tbody>
							<c:forEach var="result" items="${list}" varStatus="status">
								<tr>
									<td>
										<c:out value="${result.brdSeq}" />
									</td>

									<td><a href="javascript:fn_detail('${result.brdSeq}');"><span class="tit">${result.brdNm}</span></a></td>

									<td>
										<c:out value="${brdTypeCode[result.brdTypeCd]}" />
									</td>
									<td>
										<c:out value="${result.brdUrl}" />
									</td>
									<td>
										<fmt:formatDate value="${result.rgstDttm}" pattern="yyyy.MM.dd"/>
									</td>
									<td>
										<c:out value="${result.useYn}" />
									</td>
								</tr>
							</c:forEach>
							<c:if test="${empty list}" >
	                        	<tr>
	                        		<td colspan="7" style="text-align: center;">검색 결과가 없습니다.</td>
	                        	</tr>
                        	</c:if>
						</tbody>
					</table>
				</div>
				<jsp:include page="/WEB-INF/jsp/framework/_includes/paging.jsp" flush="true" />

				<div class="list-back">
					<ul class="btn-list">
						<li class="mT20"><a href="javascript:fn_createForm();" class="btn-submit-s navy">등록</a></li>
					</ul>
				</div>
			</div>
			<!-- //end) .table-scroll -->
		</form>
	</div>
	<!-- //end) .main-cont-box -->
</section>
