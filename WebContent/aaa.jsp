<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>
	<%@ include file="/WEB-INF/jsp/framework/_includes/includeScript.jspf" %>
	<%@ include file="/WEB-INF/jsp/framework/_includes/includeCss.jspf" %>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

<script>
window.addEventListener('load', function () {
  // 처음에는 알림 권한이 있는지 확인함
  // 없으면 권한 요구
  if (Notification && Notification.permission !== "granted") {
    Notification.requestPermission(function (status) {
      if (Notification.permission !== status) {
        Notification.permission = status;
      }
    });
  }

  var button = document.getElementsByTagName('button')[0];

  button.addEventListener('click', function () {

	  $("#dialog-message").dialog();

    // 사용자가 알림을 받는 데 동의한 경우
    // 알림 10개를 보내본다
    if (Notification && Notification.permission === "granted") {

      var i = 0;
      // 어떤 브라우저(파이어폭스 등)는 일정 시간 동안 알림이 너무 많은 경우 차단하기 때문에 인터벌 사용.
      var interval = window.setInterval(function () {
        // 태그 덕분에 "안녕! 9" 알림만 보여야 함
        var n = new Notification("안녕! " + i, {tag: '알림너무많음'});
        if (i++ == 9) {
          window.clearInterval(interval);
        }
      }, 200);
    }

    // 사용자가 알림을 받을지 말지 답하지 않은 경우
    // 참고: 크롬 때문에 권한 속성이 설정됐는지 알 수 없으므로
    // "기본" 값을 확인하는 것은 안전하지 않음
    else if (Notification && Notification.permission !== "denied") {
      Notification.requestPermission(function (status) {
        // 사용자가 ok한 경우
        if (status === "granted") {
          var i = 0;
          // 어떤 브라우저(파이어폭스 등)는 일정 시간 동안 알림이 너무 많은 경우 차단하기 때문에 인터벌 사용.
          var interval = window.setInterval(function () {
            // 태그 덕분에 "안녕! 9" 알림만 보여야 함
            var n = new Notification("안녕! " + i, {tag: '알림너무많음'});
            if (i++ == 9) {
              window.clearInterval(interval);
            }
          }, 200);
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
  });
});
</script>
<body>
<button>알림 실행!</button>
</body>


<div id="dialog-message" title="Download complete">
  <p>
    <span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
    Your files have downloaded successfully into the My Downloads folder.
  </p>
  <p>
    Currently using <b>36% of your storage space</b>.
  </p>
</div>

</html>