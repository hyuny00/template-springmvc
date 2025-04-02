<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/framework/_includes/includeTags.jspf" %>
  <link rel="stylesheet" href="/css/gis/leaflet.css" />
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
<style>
    #map {
      width: 100%;
      height: 500px;
    }
  </style>
<section id="section" class="section">
    <div class="main-cont-box">
        main-cont-boxmain-cont-boxmain-cont-box
        
         <div id="map"></div>
  <script src="/js/gis/leaflet.js"></script>
  <script>
    // 지도 생성
    const map = L.map('map').setView([-6.2088, 106.8456], 12); // 자카르타 좌표, 줌 레벨 12

    // OpenStreetMap 타일 레이어 추가
    L.tileLayer('http://localhost:8080/tiles/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    // 마커 생성
    const marker = L.marker([-6.2088, 106.8456]).addTo(map);

    // 마커에 팝업 내용 설정
    marker.bindPopup('<b>자카르타</b><br>인도네시아의 수도');

    // 마커에 마우스 오버 시 팝업 자동 열기
    marker.on('mouseover', function() {
      marker.openPopup();
    });
  </script>
    </div>
</section>





