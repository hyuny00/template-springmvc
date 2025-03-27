<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>

<script type="text/javascript">
   
    // JWT 쿠키 확인 후 /admin/user/userList로 이동
    function redirectToUserList() {
      
          //  window.location.href = "/admin/user/userList";
       
    }
 
    // 페이지 로드 시 자동 실행
    window.onload = function () {
        redirectToUserList();
    };
</script>

<section id="section" class="section">
    <div class="main-cont-box">
        main-cont-boxmain-cont-boxmain-cont-box
    </div>
</section>
