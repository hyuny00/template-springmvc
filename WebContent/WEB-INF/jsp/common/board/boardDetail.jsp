<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf"%>


<script type="text/javaScript">
	$(document).ready(function() {
		location.href = "#view_reply_" + "${replySeq}";
	});

	function fn_list() {
		document.detailForm.action = "${basePath}/common/board/${param.brdSeq}/list";
		document.detailForm.submit();

	}

	function fn_updateForm() {
		document.detailForm.action = "${basePath}/common/board/${param.brdSeq}/updateBoardForm";
		document.detailForm.submit();

	}

	function fn_reply() {
		document.detailForm.action = "${basePath}/common/board/${param.brdSeq}/insertBoardReply";
		document.detailForm.submit();

	}

	function fn_delete(){

		if(!confirm("삭제하시겠습니까?")) return;

		document.detailForm.action = "${basePath}/common/board/${param.brdSeq}/deleteBoard";
		document.detailForm.submit();
	}



	function editReplayForm(replySeq) {

		$("#view_reply_" + replySeq).hide();
		$("#edit_reply_" + replySeq).show();

		$("#btn_editReply_" + replySeq).hide();
		$("#btn_saveReply_" + replySeq).show();

	}

	function fn_editReply(replySeq) {

		var replyCnts = $("#replyCnts_" + replySeq).val();

		$.ajax({
			url : '/common/board/${param.brdSeq}/updateBoardReply',
			type : 'post',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data : {
				replySeq : replySeq,
				replyCnts : replyCnts
			},
			success : function(data) {

				alert("저장되었습니다.");

				var replyCnts = data.replyCnts.replace(/(\n|\r\n)/g, "<br/>");

				$("#view_reply_" + replySeq).html(replyCnts);
				$("#edit_reply_" + replySeq).val(replyCnts);

				$("#view_reply_" + replySeq).show();
				$("#edit_reply_" + replySeq).hide();

				$("#updtDttm_reply_" + replySeq).html(
						moment(data.updtDttm).format('YYYY-MM-DD HH:MM'));

				$("#btn_editReply_" + replySeq).show();
				$("#btn_saveReply_" + replySeq).hide();

			},
			error : function(err) {
			}
		});

	}

	function fn_editReReply(replySeq) {

		var replyCnts = $("#edit_reReplyCnts_" + replySeq).val();

		$.ajax({
			url : '/common/board/${param.brdSeq}/updateBoardReply',
			type : 'post',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data : {
				replySeq : replySeq,
				replyCnts : replyCnts
			},
			success : function(data) {

				alert("저장되었습니다.");

				var replyCnts = data.replyCnts.replace(/(\n|\r\n)/g, "<br/>");

				$("#view_reply_" + replySeq).html(replyCnts);
				$("#edit_reReply_" + replySeq).val(replyCnts);

				$("#updtDttm_reReply_" + replySeq).html(
						moment(data.updtDttm).format('YYYY-MM-DD HH:MM'));

				$("#view_reply_" + replySeq).show();
				$("#edit_reReply_" + replySeq).hide();

			},
			error : function(err) {
			}
		});

	}

	function deleteReplay(replySeq) {

		if (!confirm("삭제하시겠습니까?"))
			return;

		var replyCnts = $("#replyCnts_" + replySeq).val();

		$.ajax({
			url : '/common/board/${param.brdSeq}/deleteBoardReply',
			type : 'post',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data : {
				replySeq : replySeq,
				replyCnts : replyCnts
			},
			success : function(data) {

				$("#view_reply_" + replySeq).show();
				$("#edit_reply_" + replySeq).hide();

				if (data.replyExist == 'Y') {
					alert("댓글이 존재합니다");
				}

				if (data.delYn == 'Y') {

					$("#list_" + replySeq).hide("slide", {
						direction : "right"
					}, 500);

					var replyNumCnt = $("#replyNumCnt").text();
					$("#replyNumCnt").text(replyNumCnt - 1);

				}

			},
			error : function(err) {
			}
		});

	}

	function reReplayForm(replySeq) {
		$("#reReply_" + replySeq).show();

		$("#reReplyCnts_"+replySeq).focus();
	}

	function fn_reReply(replySeq) {

		var replyCnts = $("#reReplyCnts_" + replySeq).val();

		document.detailForm.reReplyCnts.value = replyCnts;

		document.detailForm.upReplySeq.value = replySeq;

		document.detailForm.action = "${basePath}/common/board/${param.brdSeq}/insertBoardReply";
		document.detailForm.submit();

	}
	function editReReplayForm(replySeq) {
		$("#view_reply_"+replySeq).hide();
		$("#edit_reReply_"+replySeq).show();
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
			<input type="hidden" name="upReplySeq" id="upReplySeq" value="">
			<input type="hidden" name="reReplyCnts" id="reReplyCnts" value="">

			<!-- 타이틀 -->
			<h2 class="page-title">
				<span>${allMenuMap[param.menuSeq].menuNm}</span>
			</h2>
			<blockquote class="page-desc">${allMenuMap[param.menuSeq].etc}</blockquote>
			<div>
				<table class="table">
					<tbody>
						<tr>
							<th class="tit left">제목 : <c:out value="${result.postTtl}" /></th>
						</tr>
						<tr>
							<td class="left">
								<span class="writer">작성자 : <c:out value="${result.userNm}" /></span> <span class="date">작성일자 : <fmt:formatDate value="${result.rgstDttm}" pattern="yyyy-MM-dd hh:mm" /></span>
							</td>
						</tr>
						<tr>
							<td class="left">
								<div class="ck-container">${contents}</div>
							</td>
						</tr>
						<tr>
							<td>
								<c:if test="${boardInfo.attcFileAblYn eq 'Y'}">
									<jsp:include page="/file/uploadForm" flush="true">
										<jsp:param name="docId" value="${result.attcDocId}" />
										<jsp:param name="mode" value="view" />
										<jsp:param name="viewType" value="H" />
									</jsp:include>
								</c:if>
							</td>
						</tr>
					</tbody>
				</table>

			</div>
			<div class="button" align="right">
				<c:if test="${ (result.rgstpNo eq user.userNo) or fn:contains(user.authorities, 'ROLE_ADMIN')}">
					<button type="button" class="btn-submit-s" onclick="javascript:fn_updateForm();">수정</button>
					<button type="button" class="btn-submit-s red" id="deleteBoard" onclick="javascript:fn_delete();">삭제</button>
				</c:if>
				<button type="button" class="btn-submit-s" onclick="javascript:fn_list();">목록</button>
			</div>

			<c:if test="${boardInfo.replyAblYn eq 'Y'}">

				<div class="comment">
					<h5 class="comment-tit">
						<span>댓글(<span class="reply-nums" id="replyNumCnt">${fn:length(replyList)}</span>)
						</span>
					</h5>
					<div>
						<table class="table">
							<colgroup>
								<col width="18%" />
								<col width="" />
							</colgroup>
							<tbody>
								<tr>
									<th class="tbtd_content">댓글</th>
									<td class="tbtd_content left" colspan="3">
										<textarea class="textarea" style="width: 85%; height: 70px" id="replyCnts" name="replyCnts"></textarea>
										<button type="button" style="width: 12%; height: 70px" class="btn-submit-s black" onclick="javascript:fn_reply();">댓글쓰기</button>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="comment-view">
						<ul class="comment-list">
							<c:forEach var="reply" items="${replyList}" varStatus="status">
								<li id="list_${reply.replySeq}"><c:if test="${ empty reply.upReplySeq }">
										<div class="mycommentBox">
											<span class="user-name"><c:out value="${reply.userNm}" /></span>
											<p class="user-comment" id="view_reply_${reply.replySeq}">${fn:replace(reply.replyCnts, newLineChar,'<br/>')}</p>
											<p id="edit_reply_${reply.replySeq}" style="display: none">
												<textarea class="textarea" id="replyCnts_${reply.replySeq}" name="editReplyCnts"><c:out value="${reply.replyCnts}" /></textarea>
											</p>
											<span class="cmt-date" id="updtDttm_reply_${reply.replySeq}"> <fmt:formatDate value="${reply.updtDttm}" pattern="yyyy-MM-dd hh:mm" /></span>
											<div class="re-btn-box">
												<c:if test="${reply.delYn ne 'Y'}">
													<button type="button" class="btn-reply edit" id="btn_reReply_${reply.replySeq}" onclick="javascript:reReplayForm('${reply.replySeq}');">대댓글</button>
													<c:if test="${ (result.rgstpNo eq user.userNo) or fn:contains(user.authorities, 'ROLE_ADMIN')}">
														<button type="button" class="btn-reply edit" id="btn_editReply_${reply.replySeq}" onclick="javascript:editReplayForm('${reply.replySeq}');">수정</button>
														<button type="button" class="btn-reply edit" id="btn_saveReply_${reply.replySeq}" style="display: none" onclick="javascript:fn_editReply('${reply.replySeq}');">저장</button>
														<button type="button" class="btn-reply del" id="btn_delReply_${reply.replySeq}" onclick="javascript:deleteReplay('${reply.replySeq}');">삭제</button>
													</c:if>
												</c:if>
											</div>



										</div>
									</c:if>

									<ul  id="reReply_${reply.replySeq}" style="display: none">
										<li><span class="user-name"><c:out value="${reply.userNm}" /></span>
											<p class="user-comment">
												<textarea class="textarea" style="width: 90%; height: 90px" id="reReplyCnts_${reply.replySeq}"></textarea>
												<button type="button" style="width: 8%; height: 90px" class="btn-submit-s blue" onclick="javascript:fn_reReply('${reply.replySeq}');">저장</button>
											</p></li>
									</ul> <c:if test="${not empty reply.upReplySeq}">
										<ul class="replyBox">
											<li><span class="user-name"><c:out value="${reply.userNm}" /></span>
												<p class="user-comment" id="view_reply_${reply.replySeq}">${fn:replace(reply.replyCnts, newLineChar,'<br/>')}</p>
												<p id="edit_reReply_${reply.replySeq}" style="display: none">
													<textarea class="textarea" style="width: 90%; height: 50px" id="edit_reReplyCnts_${reply.replySeq}">${reply.replyCnts}</textarea>
													<button type="button" style="width: 8%; height: 50px" class="btn-submit-s blue" onclick="javascript:fn_editReReply('${reply.replySeq}');">저장</button>
												</p> <span class="cmt-date" id="updtDttm_reReply_${reply.replySeq}"><fmt:formatDate value="${reply.updtDttm}" pattern="yyyy-MM-dd hh:mm" /></span>
												<div class="re-btn-box">
													<c:if test="${ (result.rgstpNo eq user.userNo) or fn:contains(user.authorities, 'ROLE_ADMIN')}">
														<button type="button" class="btn-reply edit" id="btn_editReply_${reply.replySeq}" onclick="javascript:editReReplayForm('${reply.replySeq}');">수정</button>
														<button type="button" class="btn-reply del" id="btn_delReply_${reply.replySeq}" onclick="javascript:deleteReplay('${reply.replySeq}');">삭제</button>
													</c:if>
												</div></li>
										</ul>
									</c:if></li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</c:if>
		</form>
	</div>

	<!-- //end) .main-cont-box -->
</section>








