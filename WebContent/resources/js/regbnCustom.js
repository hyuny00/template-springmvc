
$(document).on('keyup blur', 'input[data-number-check]', function(){
	var f=$(this).attr('data-number-check');
	var regex;
	if(f){
		regex = new RegExp("^[0-9]+(\.?)([0-9]{0,"+f+"}?)$");
	} else {
		regex = new RegExp("^[0-9]+$");
	}

	if (!regex.test($(this).val())) {
		if(f){
			$(this).val($(this).val().replace(/[^0-9|\.]/gi,''));
		} else {
			$(this).val($(this).val().replace(/[^0-9]/gi,''));
		}

	}
});

//길이체크
function fn_lenChk(targetContext, targetObj, maxLength) {
    var len = 0;
    var newtext = "";
    for(var i=0 ; i < targetObj.value.length; i++) {
        var c = escape(targetObj.value.charAt(i));

        if ( c.length == 1 ) len ++;
        else if ( c.indexOf("%u") != -1 ) len += 2;
        else if ( c.indexOf("%") != -1 ) len += c.length/3;

        if( len <= maxLength ) newtext += unescape(c);
    }

    if ( len > maxLength ) {

        alert(targetContext+" "+maxLength+" 자를 초과할 수 없습니다.");
        //cfAlertMsg(JMSG_COMF_ERR_007, [maxLength]);
        targetObj.value = newtext;
        targetObj.focus();
        return false;
    }
}

//2차분류 조회
function fn_selectSch2stList(dtlCd, upCdDiv, id){

	$.ajax({
		url: '/regbn/proc/selectSch2stList',
	    type:'post',
	    contentType:"application/x-www-form-urlencoded; charset=UTF-8",
	    data: {"dtlCd": dtlCd, "upCdDiv" : upCdDiv},
	    success: function(data) {
	    	if(data){
	    		var y_html = "";
	    		var y_html = "<option value=''>전체</option>";
	    		$.each(data, function(i) {
					y_html += "<option value='"+data[i].code+"'>"+data[i].value+"</option>";
		    	});

	    		$("#"+id).html(y_html);
				if(upCdDiv == 'CZ014000'){
					$('#'+id).select2({
			    		placeholder: '선택'
			    	});
				}
	    	}
	    },
	    error: function(err) {
	    }
	});
}

//2차분류 조회
function fn_selectSch2stDetail(dtlCd, upCdDiv, id){

	$.ajax({
		url: '/regbn/proc/selectSch2stList',
	    type:'post',
	    contentType:"application/x-www-form-urlencoded; charset=UTF-8",
	    data: {"dtlCd": dtlCd, "upCdDiv" : upCdDiv},
	    success: function(data) {
	    	if(data){
	    		var y_html = "<option value=''>전체</option>";

	    		$.each(data, function(i) {
					y_html += "<option value='"+data[i].code+"'>"+data[i].value+"</option>";
		    	});

	    		$("#"+id).html(y_html);

	    		if(dtlCd == "706"){
	    			$("[name=wuser]").parent().show();
	    		}else{
	    			$("[name=wuser]").parent().hide();
	    		}

	    	}
	    },
	    error: function(err) {
	    }
	});
}

//팝업생성
function fn_popupCreate(){
	var $obj = $("[data-popup-layer]");
	var len = $obj.length-1;
	var idx = 0;
	if(len != -1){
		idx = Number($obj.eq(len).attr("id").substr(8, $obj.eq(len).attr("id").length))+1;
	}
	var id = "popLayer"+idx;

	if(len == -1){
		$(".wrap").append("<div class='dim' data-popup-dim onclick='fn_popupCloseAll()'></div>");
	}
	$(".wrap").append("<div id='"+id+"' class='popup-box' data-popup-layer></div>");
	return id;
}

//팝업생성
function fn_popupOpen(id){
	$("#"+id).fadeIn();
	if($("[data-popup-layer").length == 1);
	$("[data-popup-dim]").fadeIn();
}


//팝업닫기
function fn_popupClose($obj){
	$obj.closest("[data-popup-layer]").fadeOut();

	if($("[data-popup-layer]").length == 1){
		$("[data-popup-dim]").fadeOut();
		setTimeout(function(){
			$obj.closest("[data-popup-layer]").remove();
			$("[data-popup-dim]").remove();
		}, 300);
	}else{
		setTimeout(function(){
			$obj.closest("[data-popup-layer]").remove();
		}, 300);
	}
}

//팝업닫기
function fn_popupCloseAll(){
	$("[data-popup-layer]").fadeOut();
	$("[data-popup-dim]").fadeOut();
	setTimeout(function(){
		$("[data-popup-layer]").remove();
		$("[data-popup-dim]").remove();
	}, 300);
}

//프린트
function fn_print(){
	window.print();
}

//포커스
function fn_scroll($target){
	var offset = $($target).offset();
    $("html, body").animate({scrollTop : offset.top-130}, 400);
    if($target.substr(0,1) == "#"){
    	$($target).focus();
    }
}

String.prototype.formatDay = function(format) {
	var dateStr = ""
	if (format == "yyyymmdd") {
		dateStr = this;
	} else if (format == "yyyy-mm-dd") {
		dateStr = this.substr(0,4)+"-"+this.substr(4,2)+"-"+this.substr(6,2);
	} else if (format == "yyyy.mm.dd") {
		dateStr = this.substr(0,4)+"."+this.substr(4,2)+"."+this.substr(6,2);
	} else if (format == "yyyy/mm/dd") {
		dateStr = this.substr(0,4)+"/"+this.substr(4,2)+"/"+this.substr(6,2);
	} else if (format == "yyyy년mm월dd일") {
		dateStr = this.substr(0,4)+"년"+this.substr(4,2)+"월"+this.substr(6,2)+"일";
	}
	return dateStr;
}


function hwpCtrlPopup(fileId, fileNm, temp){

 	if(fileNm.lastIndexOf('.hwp') == -1){
 		alert("hwp파일만 가능합니다.");
 		return;
 	}

 	$("#hwpFileId").val(fileId);
	$("#hwpFileNm").val(fileNm);
	$("#hwpTemp").val(temp);

	 var myForm = document.detailForm;
	 //var url = "/test/popForm.do";

	 var getFileBase64StrUrl="/file/hwpCtrlPopup";

	 window.open("" ,"popForm",  "toolbar=no, width=1280, height=810, directories=no, status=no,    scrollorbars=no, resizable=yes");
	 myForm.action =getFileBase64StrUrl;
	 myForm.method="post";
	 myForm.target="popForm";

	 myForm.submit();


}

//한글파일 수정 팝업
/*
function hwpCtrlPopup(fileId, fileNm, temp){
    var hwpCtrlUrl="http://10.188.139.89:8077/webhwpctrl";
   // var hwpCtrlUrl="http://10.47.25.36:8080/webhwpctrl";


	var head= document.getElementsByTagName('head')[0];

	var script= document.createElement('script');
	script.type= 'text/javascript';
	script.id="hwpscript1";
	script.src= '/js/hangulroForm/HangulroFormManager.min.js?version=1.0.0.7';

	var hwpscript= document.getElementById("hwpscript1");
	if(hwpscript!=null){
		head.removeChild(hwpscript);
	}
	head.appendChild(script);


	script= document.createElement('script');
	script.type= 'text/javascript';
	script.id="hwpscript2";
	script.src= hwpCtrlUrl+'/js/hwpctrlapp/utils/util.js';
	hwpscript= document.getElementById("hwpscript2");
	if(hwpscript!=null){
		head.removeChild(hwpscript);
	}
	head.appendChild(script);


	script= document.createElement('script');
	script.type= 'text/javascript';
	script.id="hwpscript3";
	script.src= hwpCtrlUrl+'/js/hwpctrlapp/hwpCtrlApp.js';
	hwpscript= document.getElementById("hwpscript3");
	if(hwpscript!=null){
		head.removeChild(hwpscript);
	}
	head.appendChild(script);

	script= document.createElement('script');
	script.type= 'text/javascript';
	script.id="hwpscript4";
	script.src= hwpCtrlUrl+'/js/webhwpctrl.js';
	hwpscript= document.getElementById("hwpscript4");
	if(hwpscript!=null){
		head.removeChild(hwpscript);
	}
	head.appendChild(script);

 	if(fileNm.lastIndexOf('.hwp') == -1){
 		alert("hwp파일만 가능합니다.");
 		return;
 	}

 	$("#hwpFileId").val(fileId);
	$("#hwpFileNm").val(fileNm);
	$("#hwpTemp").val(temp);

 	var getFileBase64StrUrl="/file/commonHtrlBase64/"+fileId+"/"+encodeURI(fileNm)+"/"+temp;

 	$.ajax({
		url : getFileBase64StrUrl,
		type : 'post',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",

		success : function(data) {

			$("#base64Data").val(data);

			var id = fn_popupCreate();
			$("#"+id).load("/common/hwpCtrlEditor", function() {});
			fn_popupOpen(id);

		},
		error : function(err) {
		}
	});



}
*/
$(document).ready(function(){
	//detail search
	$('div#detailSchArea').hide();
	$('#detailSchBtn').on('click', function(){
	    $('div#detailSchArea').slideToggle(180);
	    return false;
	});
});

function fn_chrgpChangeHstr(type, propSeq){
	var data = {};
	data.chrgDivCd = type;
	data.propSeq = propSeq;

	var id = fn_popupCreate();
	$("#"+id).load("/regbn/popup/chrgpChangeHstr", data, function() {
		$("#"+id).css("min-width","320px");
		$("#"+id).css("width","320px");
	});
	fn_popupOpen(id);
}

function fn_searchPopup(){
	var data = {};
	data.collection = "prop";
	data.keyword = $("[name=subKeyword]").val();

	var id = fn_popupCreate();
	$("#"+id).load("/regbn/popup/searchPopup", data, function() {});
	fn_popupOpen(id);
}

function fn_addSimPropRgstNo(rgstNo, $obj){
	$("[name=simPropRgstNo]").val(rgstNo);
	fn_popupClose($obj);
}


jQuery.fn.serializeObject = function() {
	    var o = null;
	    try {
	        if (this[0].tagName && this[0].tagName.toUpperCase() == "FORM") {
	            var arr = this.serializeArray();
	            if (arr) {
	                o = {};
	                jQuery.each(arr, function() {
	                	if (o[this.name]) {
 	       		        if (!o[this.name].push) {
 	       		            o[this.name] = [o[this.name]];
 	       		        }
 	       		        o[this.name].push(this.value || '');
 	       		    } else {
 	       		        o[this.name] = this.value || '';
 	       		    }
	                });
	            }//if ( arr ) {
	        }
	    } catch (e) {
	        alert(e.message);
	    } finally {
	    }

	    return o;
	};

	function isNull(value) {
		var _chkStr = value+"";
		if (_chkStr == "" || _chkStr == null || _chkStr == "null") {
			return true;
		} else {
			return false;
		}
	}

	function isUndefinded(value) {
		if (typeof value === 'undefined' || value === 'undefined' || value === undefined) {
			return true;
		} else {
			return false;
		}
	}

	function nvl(value, replaceval) {
		if (isNull(value) || isUndefinded(value)) {
			if (isUndefinded(replaceval)) {
				return "";
			} else {
				return replaceval;
			}
		} else {
			return value;
		}
	}

	/**
	 * @type   : function
	 * @access : public
	 * @desc   : input 필드나 textarea 의 사이즈를 제한하여 지정된 길이 이상
	 *           입력할 경우 추가입력된 메세지를 출력하고 추가 입력된 문자는 삭제함
	 *
	 * <xmp>
	 *  <INPUT TYPE="text" NAME="txtDesc" OnKeyUp="cfLengthCheck(this, 100);">
	 *  <TEXTAREA name="txtDesc" rows="10" cols="60" OnKeyUp="cfLengthCheck(this, 4000);"></TEXTAREA>
	 * </xmp>
	 * @param  : targetObj - Textarea Object
	 * @param  : maxLength - Max Length(영문기준)
	 * @return :
	 */
	function cfLengthCheck(targetContext, targetObj, maxLength) {
	    var len = 0;
	    var newtext = "";
	    for(var i=0 ; i < targetObj.value.length; i++) {
	        var c = escape(targetObj.value.charAt(i));

	        if ( c.length == 1 ) len ++;
	        else if ( c.indexOf("%u") != -1 ) len += 2;
	        else if ( c.indexOf("%") != -1 ) len += c.length/3;

	        if( len <= maxLength ) newtext += unescape(c);
	    }

	    if ( len > maxLength ) {

	        alert(targetContext+" "+maxLength+" 자를 초과할 수 없습니다.");
	        //cfAlertMsg(JMSG_COMF_ERR_007, [maxLength]);
	        targetObj.value = newtext;
	        targetObj.focus();
	        return false;
	    }
	}

	/**
	 * @type   : function
	 * @access : public
	 * @desc   : input 필드나 textarea 의 사이즈를 제한하여 지정된 길이 이상
	 *           입력할 경우 추가입력된 메세지를 출력하고 추가 입력된 문자는 삭제함
	 *
	 * <xmp>
	 *  <INPUT TYPE="text" NAME="txtDesc" OnKeyUp="cfLengthCheck(this, 100);">
	 *  <TEXTAREA name="txtDesc" rows="10" cols="60" OnKeyUp="cfLengthCheck(this, 4000);"></TEXTAREA>
	 * </xmp>
	 * @param  : targetObj - Textarea Object
	 * @param  : maxLength - Max Length(영문기준)
	 * @return :
	 */
	function cfLengthCheck(targetContext, targetObj, maxLength) {
	    var len = 0;
	    var newtext = "";
	    for(var i=0 ; i < targetObj.value.length; i++) {
	        var c = escape(targetObj.value.charAt(i));

	        if ( c.length == 1 ) len ++;
	        else if ( c.indexOf("%u") != -1 ) len += 2;
	        else if ( c.indexOf("%") != -1 ) len += c.length/3;

	        if( len <= maxLength ) newtext += unescape(c);
	    }

	    if ( len > maxLength ) {

	        alert(targetContext+" "+maxLength+" 자를 초과할 수 없습니다.");
	        //cfAlertMsg(JMSG_COMF_ERR_007, [maxLength]);
	        targetObj.value = newtext;
	        targetObj.focus();
	        return false;
	    }
	}




 	var LAW_OBJECT = {
 			listObj: null
 			, detailObj: null
 			, totalCnt: 0
 			, flag: 0
 			, search: function(pageNo) {
 				PROCESSING.open();
 				$("#lawApiForm #pageNo").val(pageNo);

 				var obj = $("#lawApiForm").serializeObject();

 				$.ajax({
 			   		url: '/api/law/selectLawApiList',
 				    type:'post',
 				    contentType:"application/json; charset=UTF-8",
 				    dataType : 'json',
 				    data: JSON.stringify(obj),
 				    success: function(data) {
 				    	LAW_OBJECT.listObj = data.list;
 				    	LAW_OBJECT.totalCnt = data.pageable.totalCount;
 				    	LAW_OBJECT.draw(pageNo);
 				    },
 				    error: function(err) {
 				    	PROCESSING.close();
 				    }
 				});
 			}
 			, draw: function(pageNo) {
 				var list = this.listObj;
 				var totalCnt = this.totalCnt;
 				var pageSize = parseInt($("#lawApiForm #pageSize").val());
 				$("#lawApiForm #totalCount").text(totalCnt);
 				$("#lawApiForm #pageNo").val(pageNo);

 				// 페이징 처리 ( parameter : target, 리스트 전체 개수, 리스트 전체, 현재 페이지, 한 페이지에 나타낼 데이터 수, callback 함수명 )
 				$("#lawApiForm #pagingArea").html("");
 		    	customPaging("#lawApiForm #pagingArea", totalCnt, list, pageNo, pageSize, "LAW_OBJECT.search");

 				var str = [];
 				for (var i=0; i<list.length; i++) {
 					var item = list[i];
 					str.push("<tr onclick='LAW_OBJECT.detail(\""+item.법령id+"\");'>");
 					str.push("<td>"+item.id+"</td>");
 					str.push("<td class='left'>"+item.법령명한글+"</td>");
 					str.push("<td>"+item.소관부처명+"</td>");
 					str.push("<td>"+item.제개정구분명+"</td>");
 					str.push("<td>"+item.법령구분명+"</td>");
 					str.push("<td>제 "+item.공포번호+"호</td>");
 					str.push("<td>"+item.공포일자.formatDay("yyyy-mm-dd")+"</td>");
 					str.push("<td>"+item.시행일자.formatDay("yyyy-mm-dd")+"</td>");
 					str.push("</tr>");
 				}
 				$("#lawApiForm #lawListArea").empty();
 		    	$("#lawApiForm #lawListArea").append(str.join(""));

 		    	PROCESSING.close();
 			}
 			, detail: function(lawId, joId) {
 				if (LAW_OBJECT.flag == 0) {
 					LAW_OBJECT.flag = 1;
 					var obj = {"lawId":lawId};
 					if (nvl(joId) != "") {
 						obj.joId = joId;
 					}
 					obj.returnFormId = $("[name=returnFormId]").val();

 					var popId = fn_popupCreate();
 					$("#"+popId).load("/api/law/lawApiDetail", obj, function() {
 						LAW_OBJECT.flag = 0;
 					});
 					fn_popupOpen(popId);
 				}
 			}
 			, reset: function() {
 				$("#lawApiForm").find("input[type=text]").val('');
 				$("#lawApiForm").find("select").each(function() {
 					$(this).find("option").eq(0).prop("selected",true);
 				});
 				$("#lawApiForm #pageNo").val(1);
 				$("#lawApiForm #pageSize").val(10);
 				$("#lawApiForm #listPageSize").val(10);
 			}
 			, changePageSizeScipt: function(val) {
 				$("#lawApiForm #pageSize").val(val);
 				this.search(1);
 			}
 		}