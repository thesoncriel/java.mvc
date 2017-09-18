# Java MVC Library
2016년 제작.  
사내 인트라넷인 ONS를 유지보수 할 때 쓰였다.  
Spring 같은 MVC Framework가 전혀 적용되지 않아 추가 업무 시 많은 어려움이 있었다.  
그래서 부득이하게 간이 MVC 패턴을 적용시킬 수 있는 라이브러리를 만들게 되었다. 

## 백엔드 처리
시스템 사정상 RESTful로 이용하기 어려워서 미리 업무용으로 만든 Controller를 호출만 하는 간단한 jsp들을 만들어 이들을 이용토록 하였다.
Model측 역시 SQLMapper를 쓰기 어려워서 CRUD를 사용할 수 있는 기본 모델을 만들고, 이를 상속 받아 필요한 SQL을 수행, 그 결과물을 Map, List 및 Beans 등으로 받을 수 있게 하였다.

## ONS
Desktop용 사이트.  
Grid 출력 시 ActiveX로 이뤄져 있어서 IE8 이하 호환성 모드로 이용하는 특징이 있었다.  
때문에 프론트엔드 측은 부득이하게 AngularJS 등은 쓰지 못하고 구형 브라우저를 잘 지원하는 knockoutJS를 이용하게 되었다.  
타 부서가 요청한 여러가지 추가 업무에 주로 이용 되었고, 기존 기능을 유지보수 하는 것은 어쩔 수 없이 그 Rule에 따랐다.

**사용기술**
- JAVA 1.5
- Oracle 10G
- KnockoutJS
- jQuery & jQuery-UI

### 추가된 업무
* 계약서 파일 관리
* 라인넷 ERP 자료 연동
* OTP 발급 현황
* 컨텐츠 그룹 적용
* 성인셋탑 장비 관리
* 가맹점 등록 요청 수락 기능
* 접속 IP 허용/차단 설정

## ONS Mobile
지사들이 주로 이용하는 간편 모바일 시스템.  
모바일 시스템이면서 모바일로 쓰기 매우 불편하게 UI가 구성되어 있었다.  
때문에 추가 업무를 요청받은 부분에 한하여 AngularJS 등을 적용 시켰다.  
신규 업무 추가 시 백엔드쪽은 기존 ONS 것과 같은 방법으로 쓰였다.

**사용기술**
- JAVA 1.5
- Oracle 10G
- AngularJS & UI-Router
- jQuery
- Bootstrap

### 추가된 업무
* 가맹점 관리
	- 가맹점 등록 요청
	- 개통 관리 및 동기화
	- 계약서 관리
	- 가맹점 정보 확인
* 성인셋탑 관리
	- 셋탑 요청, 전달 및 반납
	- 지사별 셋탑 소유 및 적용 현황
	- 가맹점별 셋탑 적용/해제 기능

## 비고
사정상 보안에 민감한 내용과 하부 컨텐츠 및 SQL문, 백엔드 모듈 등은 제거 함.

## Caution
- 내용을 참고만 하되 복붙하심 안됩니다..

## Screen Shots
### ONS Mobile
![](https://github.com/thesoncriel/java.mvc/blob/master/screenshots/001.png)
![](https://github.com/thesoncriel/java.mvc/blob/master/screenshots/002.png)
![](https://github.com/thesoncriel/java.mvc/blob/master/screenshots/003.png)
![](https://github.com/thesoncriel/java.mvc/blob/master/screenshots/004.png)
![](https://github.com/thesoncriel/java.mvc/blob/master/screenshots/005.png)
![](https://github.com/thesoncriel/java.mvc/blob/master/screenshots/006.png)
![](https://github.com/thesoncriel/java.mvc/blob/master/screenshots/007.png)