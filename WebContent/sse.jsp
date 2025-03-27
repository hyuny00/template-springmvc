<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>
	<%@ include file="/WEB-INF/jsp/framework/_includes/includeScript.jspf" %>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<input id="input"/>
<button id="send">send</button>
<pre id="messages"></pre>
<script>

	$(document).ready(function(){


		 	if (Notification && Notification.permission !== "granted") {
			    Notification.requestPermission(function (status) {
			      if (Notification.permission !== status) {
			        Notification.permission = status;
			      }
			    });
			  }
	});


    const eventSource = new EventSource("/api/subscribe2");
	//const eventSource = new EventSource("/api/test");

   	eventSource.onmessage = function(e) {
	   console.log(e);
	};

	eventSource.onerror = function(e) {
	   console.log(e);
	};

	eventSource.onmessage = function(e) {
	    document.querySelector("#messages").appendChild(document.createTextNode(e.data + "\n"));


	    if (Notification && Notification.permission === "granted") {

	        var i = 0;
	        // 어떤 브라우저(파이어폭스 등)는 일정 시간 동안 알림이 너무 많은 경우 차단하기 때문에 인터벌 사용.

	          // 태그 덕분에 "안녕! 9" 알림만 보여야 함
	          var n = new Notification("안녕! ", {tag: "테스트", body: e.data});


	      }

	      // 사용자가 알림을 받을지 말지 답하지 않은 경우
	      // 참고: 크롬 때문에 권한 속성이 설정됐는지 알 수 없으므로
	      // "기본" 값을 확인하는 것은 안전하지 않음
	      else if (Notification && Notification.permission !== "denied") {
	        Notification.requestPermission(function (status) {
	          // 사용자가 ok한 경우
	          if (status === "granted") {

	        	  console.log(JSON.stringify(e.data));


	        	  var n = new Notification("안녕! ", {tag: "테스트", body: e.data});

	          }

	          // 그 외의 경우 일반적인 모달 alert로 폴백
	          else {
	            alert("안녕!");
	          }
	        });
	      }

	      // 사용자가 알림을 거부한 경우
	      else {
	        // 일반적인 모달 alert로 폴백
	        alert("안녕!");
	      }


	};

    document.querySelector("#send").addEventListener("click", function(event){
        fetch("/api/publish2?message="+document.querySelector("#input").value);
    });

</script>
</body>
</html>


