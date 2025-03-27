<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>
<script type="text/javascript" src="${basePath}/js/ckeditor/ckeditor.js"></script>

    <script type="text/javaScript">


    $(document).ready(function(){



    });


    function fn_list(){
    	document.detailForm.action = "${basePath}/common/board/${param.brdSeq}/list";
       	document.detailForm.submit();

    }



    function fn_cancel(){
       	document.detailForm.reset();
    }

    function fn_save(){

    	if($("#postTtl").val()==''){
    		alert("제목을 입력하세요");
    		return;
    	}

    	if(CKEDITOR.instances.postCnts.getData() == "" ) {
			alert("내용을 입력하세요.");
			CKEDITOR.instances.postCnts.focus();
			return;
    	}


    	if($("#postSeq").val()==''){
   			document.detailForm.action = "${basePath}/common/board/${param.brdSeq}/insertBoard";
    	}else{
    		document.detailForm.action = "${basePath}/common/board/${param.brdSeq}/updateBoard";
    	}

       	document.detailForm.submit();
    }




    </script>

<section id="section" class="section">

	<div class="main-cont-box">
		<form id="detailForm" method="post" name="detailForm">
			<!-- 검색조건, 메뉴, 페이징 파라메터-->
			<jsp:include page="/WEB-INF/jsp/framework/_includes/includePageParam.jsp" flush="true" />
			<jsp:include page="/WEB-INF/jsp/framework/_includes/includeSchParam.jsp" flush="true" />

			<input type="hidden" name="brdSeq" id="brdSeq" value="${param.brdSeq}">
			<input type="hidden" name="postSeq" id="postSeq" value="${param.postSeq}">

			<!-- 타이틀 -->
			<h2 class="page-title">
				<span>${allMenuMap[param.menuSeq].menuNm}</span>
			</h2>
			<blockquote class="page-desc">${allMenuMap[param.menuSeq].etc}</blockquote>
			<h3 class="page-subtitle">게시글작성</h3>

			<div>
				<table class="table">
					<colgroup>
						<col width="18%" />
						<col width="32%" />
						<col width="18%" />
						<col width="" />
					</colgroup>
					<tbody>
						<tr>
							<th class="tbtd_content">제목</th>
							<td class="tbtd_content" colspan="3">
								<input type="text" class="input" id="postTtl" name="postTtl" value="${result.postTtl}" maxlength="25" />
							</td>
						</tr>

						<tr>
							<th class="tbtd_content">내용</th>
							<td class="tbtd_content" colspan="3">
								<textarea class="ckeditor" id="postCnts" name="postCnts">${result.postCnts}</textarea>
							</td>
						</tr>

						<c:if test="${boardInfo.attcFileAblYn eq 'Y'}">
							<tr>
								<th class="tbtd_content">첨부파일</th>
								<td class="tbtd_content" colspan="3">
									  <jsp:include page="/file/uploadForm" flush="true">
									   	<jsp:param name="refDocId" value="attcDocId"/>
									  	<jsp:param name="docId" value="${result.attcDocId}"/>
									  	<jsp:param name="maxFileCnt" value="${boardInfo.attcFileAblNum}"/>
									  	<jsp:param name="maxFileSize" value="20M"/>
									  </jsp:include>
								</td>
							</tr>
						</c:if>


					</tbody>
				</table>
			</div>


			<div class="button" align="right">
				<button type="button" class="btn-submit-s" onclick="javascript:fn_save();">저장</button>


				<button type="button" class="btn-submit-s" id="deleteBoardMgmt" style="display:none" onclick="javascript:fn_delete();">삭제</button>

				<button type="button" class="btn-submit-s" onclick="javascript:fn_cancel();">취소</button>
				<button type="button" class="btn-submit-s" onclick="javascript:fn_list();">목록</button>
			</div>





		</form>
	</div>
	<!-- //end) .main-cont-box -->
</section>






