<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>

    <script type="text/javaScript" >

	    $(document).ready(function(){
	    	// select2 초기화
	    	$('#test').select2(
	    		{width: '20%'}
	    	);


	    	//$('#test2').select2();
	    	$('#test2').select2({
	    		maximumSelectionLength:2
	    	});

	    	$('#test3').select2();


	    	$("#btn-excel").on("click", function () {
	              var $preparingFileModal = $("#preparing-file-modal");
	              $preparingFileModal.dialog({ modal: true });
	              $("#progressbar").progressbar({value: false});
	              $.fileDownload("/file/largeExcelDown", {

	            	  httpMethod: "post",
            	 	  data:$("#listForm").serialize(),

	                  successCallback: function (url) {
	                      $preparingFileModal.dialog('close');
	                  },
	                  failCallback: function (responseHtml, url) {
	                      $preparingFileModal.dialog('close');
	                      $("#error-modal").dialog({ modal: true });
	                  }
	              });
	              return false;
          	});


	    });


        /* 글 수정 화면 function */
        function sampleDetail(id) {
        	document.listForm.id.value = id;
           	document.listForm.action = "${basePath}/sample/selectSample";
           	document.listForm.submit();
        }

        /* 글 등록 화면 function */
        function sampleForm() {
           	document.listForm.action = "${basePath}/sample/sampleForm";
           	document.listForm.submit();
        }

        /* 글 목록 화면 function */
        function sampleList() {
        	document.listForm.pageNo.value = 1;
        	document.listForm.action = "${basePath}/sample/selectSampleList";
           	document.listForm.submit();
        }



        function selectCode( ) {


        	var code=$("#test option:selected").data("code")

	    	$.ajax({
	    		url: '/common/selectCode',
			    type:'post',
			    contentType:"application/x-www-form-urlencoded; charset=UTF-8",
			    data: {"code": code},
			    success: function(data) {

			    	$('#test3').empty();

			    	if(data){

			    		data.unshift({id: "", text:"선택"});

			    		$('#test3').select2({
				    		placeholder: '선택',
				    		data: data
				    	});

			    		//$('#test3').val('');
			    		//$('#test3').trigger('change');


			    		$('#test2').select2().next().hide();

			    	}

			    },
			    error: function(err) {
			    }
			});

	    }




    </script>
<section id="section" class="section">
  <div class="main-cont-box">
    <form  id="listForm" name="listForm"  method="post" action="${basePath}/sample/selectSampleList">
    	<!-- 메뉴, 페이징 파라메터-->
		<jsp:include page="/WEB-INF/jsp/framework/_includes/includePageParam.jsp" flush="true"/>
        <input type="hidden" name="id" value="">
        	<!-- // 타이틀 -->
        	<div id="search">
        		<ul>
        			<li>
        				<select name="schCondition" >
        					<option value="1" <c:if test="${param.schCondition eq '1'}">selected </c:if> >Name</option>
        					<option value="0" <c:if test="${param.schCondition eq '0'}">selected </c:if> >ID</option>
        				</select>
        			</li>
        			<li>
                        <input type="text"  name="schKeyword" value="${param.schKeyword}" />
                    </li>
        			<li>
        	            <span class="btn_blue_l">
        	                <a href="javascript:sampleList();">search</a>
        	            </span>
        	        </li>

        	        <li>
        				<select name="test" id="test"  onchange="selectCode();">
      							<option>전체</option>
      							<c:forEach var="code" items="${sampleCodeList}" varStatus="status">
      								<option value="${code.code}" data-code="${code.cdSeq}"> ${code.value}</option>
      							</c:forEach>
        				</select>

        				<select name="test3" id="test3" style="width:300px">
        					<option >전체</option>
        				</select>

        				<select name="test2" id="test2" style="width:300px" multiple="multiple">
        						<option>중소기업</option>
								<option>소상공인</option>
								<option>중앙부처</option>
								<option>지자체</option>
								<option>관행</option>
								<option>현장대기</option>
        				</select>
        			</li>


                </ul>
        	</div>
        	<!-- List -->
        	<div id="table">
        		<table width="80%" border="0" cellpadding="0" cellspacing="0" summary="카테고리ID, 케테고리명, 사용여부, Description, 등록자 표시하는 테이블">
        			<colgroup>
        				<col width="10%"/>
        				<col width="20%"/>
        				<col width="15%"/>
        				<col width="15%"/>
        				<col width="15%"/>
        				<col width="15%"/>
        			</colgroup>
        			<tr>
        				<th align="center">id</th>
        				<th align="center">name</th>
        				<th align="center">useYn</th>
        				<th align="center">description</th>
        				<th align="center">regUser</th>
        				<th align="center">구분</th>
        			</tr>
        			<c:forEach var="result" items="${list}" varStatus="status">
            			<tr>
            				<td align="center" class="listtd"><a href="javascript:sampleDetail('${result.id}')">${result.id}</a></td>
            				<td align="left" class="listtd">${result.name}</td>
            				<td align="center" class="listtd">${result.useYn}</td>
            				<td align="center" class="listtd">${result.description}</td>
            				<td align="center" class="listtd">${result.regUser}</td>
            				<td align="center" class="listtd">${etcCode[result.etcCode]}</td>
            			</tr>
        			</c:forEach>
        		</table>
        	</div>

        	<div >

        	</div>
        	<!-- /List --> ${isNext}
    ${pageable.prev}oooooooooo  ${pageable.next}
 ${getTotalCount}AAAAAAAAA   ${getTotalPage}"JJJJJJJJJ"${pageable.totalCount}dddddd${pageable.pageNo}llllllllll${pageable2.totalCount}
        	<jsp:include page="/WEB-INF/jsp/framework/_includes/paging.jsp" flush="true"/>

        	<div id="sysbtn">
        	  <ul>
        	      <li>
        	          <span class="btn_blue_l">
        	              	<a href="javascript:sampleForm();">create</a>

       	               	

                      </span>
                      <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_USER')">
						   [권한 체크]
					  </sec:authorize>

						<button id="btn-excel" >[엑셀 다운로드]</button>


                  </li>
              </ul>
        	</div>
    </form>
		<table><tr><td>
		<div id="appendLayer" > </div>
		</td></tr></table>
	</div>

		<div id="popLayer" style="display:none;width:1000px;height:400px;border:4px solid #ddd;background:#fff;"> </div>

</section>