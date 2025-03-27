<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>

<script type="text/javascript">
    // 쿠키에서 JWT 토큰 가져오는 함수
    function getCookie(name) {
        let matches = document.cookie.match(new RegExp("(^| )" + name + "=([^;]+)"));
        return matches ? matches[2] : null;
    }

    // JWT 쿠키 확인 후 /admin/user/userList로 이동
    function redirectToUserList() {
        let jwtToken = getCookie("JWT"); // 쿠키에서 jwt 가져오기

        alert(jwtToken);
        if (jwtToken) {
            // JWT가 존재하면 페이지 이동
            window.location.href = "/admin/user/userList";
        } else {
            alert("로그인이 필요합니다."); // JWT 없을 경우 처리
        }
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
