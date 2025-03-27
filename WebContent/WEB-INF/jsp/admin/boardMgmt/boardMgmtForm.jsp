<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>


    <script type="text/javaScript">


    $(document).ready(function(){

    	$('#brdTypeCd').select2();
    	$('#attcFileAblNum').select2();
    	$('#useYn').select2();

    	if($("#brdSeq").val()==''){

    		$("#replyAbl_y").attr("checked","true");
    		$("#attcFileAbl_y").attr("checked","true");

    	}


    });


    function fn_list(){
    	document.detailForm.action = "${basePath}/admin/boardMgmt/selectBoardMgmtList";
       	document.detailForm.submit();

    }



    function fn_cancel(){
       	document.detailForm.reset();

       	$('#brdTypeCd').val('${result.brdTypeCd}').select2();
    	$('#attcFileAblNum').val('${result.attcFileAblNum}').select2();
    }

    function fn_save(){

    	if($("#brdNm").val()==''){
    		alert("게시판명을 입력하세요");
    		$("#brdNm").focus();
    		return;
    	}

    	if($("#brdExpl").val()==''){
    		alert("게시판설명을 입력하세요");
    		$("#brdExpl").focus();
    		return;
    	}


    	var  brdTypeCd=$("#brdTypeCd option:selected").val();

    	if(brdTypeCd=='02'){
    		var replyAblYn =$("input[name='replyAblYn']:checked").val();

    		if(replyAblYn=='Y'){
    			alert("공지사항은 답글가능여부는 불가능합니다.");
    			return;

    		}
    	}


    	var checkAttcFileAblYn =$("input[name='attcFileAblYn']:checked").val();

    	if(checkAttcFileAblYn=='Y'){

    		if($("#attcFileAblNum").val() !="" && $("#attcFileAblNum").val()==0){
    			alert("0 이상을 선택하세요");
    			return;
    		}

    	}

    	if(checkAttcFileAblYn=='N'){

    		if($("#attcFileAblNum").val()!=0){
    			$("#attcFileAblNum").val("0").select2();
    		}

    	}

    	if(!confirm('저장하시겠습니까?')) return;

   		document.detailForm.action = "${basePath}/admin/boardMgmt/saveBoardMgmt";

       	document.detailForm.submit();
    }

    function changeBrdType(){

    	var  code=$("#brdTypeCd option:selected").val();

    	if(code=='01'){
    		$("#replyAbl_n").attr("checked", false);
    		$("#replyAbl_y").attr("checked", true);
    	}

    	if(code=='02'){
    		$("#replyAbl_y").attr("checked", false);
    		$("#replyAbl_n").attr("checked", true);
    	}

    }


    function changeAttcFileAbl(){
    	var  code=$("input[name=attcFileAblYn]:checked").val();

    	if(code=='Y'){
    		$("#attcFileAblNum").val("").select2();
    		$("#attcFileAblNum").attr("readonly", false);
    	}
		if(code=='N'){

			$("#attcFileAblNum").val("0").select2();

			$("#attcFileAblNum").attr("readonly", true);
    	}

    }

    //게시판 관리 삭제
   function fn_delete() {

    	if(!confirm('게시글을 삭제하시겠습니까?')) return;

		document.detailForm.action = "/admin/boardMgmt/deleteBoardMgmt";
		document.detailForm.submit();
	}

    </script>

<section id="section" class="section">

	<div class="main-cont-box">
		<form id="detailForm" method="post" name="detailForm">
			<!-- 검색조건, 메뉴, 페이징 파라메터-->
			<jsp:include page="/WEB-INF/jsp/framework/_includes/includePageParam.jsp" flush="true" />
			<jsp:include page="/WEB-INF/jsp/framework/_includes/includeSchParam.jsp" flush="true" />

			<input type="hidden" name="brdSeq" id="brdSeq" value="${result.brdSeq}">

			<!-- 타이틀 -->
			<h2 class="page-title">
				<span>게시판 관리</span>
			</h2>
			<blockquote class="page-desc">게시판을 생성하신 후 메뉴 및 권한을 등록하시면 사용하실 수 있습니다.</blockquote>
			<h3 class="page-subtitle" style="padding-bottom: 13px;">게시판 관리</h3>

			<div>
				<table class="table border2">
					<colgroup>
						<col style="width:18%;" />
						<col style="width:32%;" />
						<col style="width:18%;" />
						<col />
					</colgroup>
					<tbody>
						<tr>
							<th class="tbtd_content"><label for="brdNm">게시판 명</label><span class="orange">*</span></th>
							<td class="tbtd_content" colspan="3">
								<input type="text" class="input" id="brdNm" name="brdNm" value="${result.brdNm}" maxlength="25" />
							</td>
						</tr>

						<tr>
							<th class="tbtd_content"><label for="brdExpl">게시판 설명</label><span class="orange">*</span></th>
							<td class="tbtd_content" colspan="3">
								<textarea class="textarea" id="brdExpl" name="brdExpl">${result.brdExpl}</textarea>
							</td>
						</tr>

						<tr>
							<th><label for="brdTypeCd">게시판 유형</label></th>
							<td>
								<div class="selectbox">
									<select name="brdTypeCd" id="brdTypeCd" onchange="changeBrdType();">
										<c:forEach var="code" items="${brdTypeList}" varStatus="status">
											<option value="${code.code}" <c:if test="${result.brdTypeCd eq code.code}">selected</c:if> >${code.value}</option>
										</c:forEach>
									</select>
								</div>
							</td>

							<th><label for="replyAbl_y">답글 가능여부</label></th>
							<td>
								<div class="inputbox">
									<div class="radiobox">
										<input class="radio"  <c:if test="${result.replyAblYn eq 'Y'}">checked</c:if>  name="replyAblYn" value="Y" id="replyAbl_y" type="radio" >
										<label class="label" for="replyAbl_y"><span>가능</span></label>
									</div>
									<div class="radiobox">
										<input class="radio"  <c:if test="${result.replyAblYn eq 'N'}">checked</c:if>  name="replyAblYn" id="replyAbl_n" value="N" type="radio" >
										<label class="label" for="replyAbl_n"><span>불가능</span></label>
									</div>
								</div>
							</td>
						</tr>

						<tr>
							<th><label for="attcFileAbl_y">첨부파일 가능여부</label></th>
							<td>
								<div class="inputbox">
									<div class="radiobox">
										<input type="radio" class="radio"  <c:if test="${result.attcFileAblYn eq 'Y'}">checked</c:if>  name="attcFileAblYn" id="attcFileAbl_y" value="Y"  onchange="changeAttcFileAbl();">
										<label class="label" for="attcFileAbl_y"><span>가능</span></label>
									</div>
									<div class="radiobox">
										<input type="radio"  class="radio" <c:if test="${result.attcFileAblYn eq 'N'}">checked</c:if>  name="attcFileAblYn" id="attcFileAbl_n" value="N" onchange="changeAttcFileAbl();">
										<label class="label" for="attcFileAbl_n"><span>불가능</span></label>
									</div>
								</div>
							</td>

							<th><label for="attcFileAblNum">첨부파일 가능숫자</label></th>
							<td>
								<div class="selectbox">
									<select name="attcFileAblNum" id="attcFileAblNum">
										<option value="">제한없음</option>
										<c:forEach var="attcNum" begin="0" end="10">
											<option value="${attcNum}"  <c:if test="${result.attcFileAblNum eq attcNum}">selected</c:if> >${attcNum}</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<th><label for="brdNm">게시판 URL</label></th> <!-- W3C에서 위 id랑 겹침 그래서 1 추가 -->
							<td colspan="3">
								<c:if test="${not empty result.brdUrl}">
									<input type="text" class="input" id="brdNm" name="brdUrl" value="${result.brdUrl}" maxlength="255" readonly />
								</c:if>
								<c:if test="${empty result.brdUrl}">
									<input type="text" class="input" id="brdNm" name="brdUrl" value="/common/board/" maxlength="255" readonly />
								</c:if>
							</td>
						</tr>
						<tr>
							<th><label for="useYn">사용여부</label></th>
							<td colspan="3">
								<div class="selectbox">
									<select name="useYn" id="useYn">
										<option value="Y" <c:if test="${result.useYn eq 'Y'}">selected </c:if>>Y</option>
										<option value="N" <c:if test="${result.useYn eq 'N'}">selected </c:if>>N</option>
									</select>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>


			<div class="button" style="text-align:right;">
				<button type="button" class="btn-submit-s navy" onclick="javascript:fn_save();">저장</button>


				<!-- <button type="button" class="btn-submit-s red" id="deleteBoardMgmt"  onclick="javascript:fn_delete();">삭제</button> -->

				<button type="button" class="btn-submit-s" onclick="javascript:fn_cancel();">취소</button>
				<button type="button" class="btn-submit-s white" onclick="javascript:fn_list();">목록</button>
			</div>

		</form>
	</div>
	<!-- //end) .main-cont-box -->
</section>






