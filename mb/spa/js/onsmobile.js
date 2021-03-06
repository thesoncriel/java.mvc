var appBase = (function(angular, undefined){
	"use strict";
	
	var sViewRoot = "/mb/spa/view/";
	
	// 최초 기본 모듈 생성
	var app = angular.module("onsmobile", ["ui.router", "ui.bootstrap", "ngAnimate", "ngSanitize", "ngAsyncform", "common.directive", "ons.controller"]);
	
	// 무한스크롤 요청시간 제한 설정
	//angular.module('infinite-scroll').value('THROTTLE_MILLISECONDS', 1000);
    //console.log("app" ,aRoute);
        
    app.config([
    	"$provide",
    	"$stateProvider",
    	"$urlRouterProvider",
    	"$httpProvider",
    	"$sceDelegateProvider",
    function(
    	$provide,
    	$stateProvider,
    	$urlRouterProvider,
    	$httpProvider,
    	$sceDelegateProvider
    ){
    	// Appling Router [begin]
    	var mRouteInfo,
    		aRoute = onsroute(sViewRoot);

    	// 공용
    	$stateProvider.state({
			name: "login",
			url: "/login",
			templateUrl: sViewRoot + "login.html"
		});

    	for(var i = 0, iLen = aRoute.length; i < iLen; i++){
    		
    		mRouteInfo = aRoute[ i ];
    		
    		//console.log("mRouteInfo", mRouteInfo);
    		
    		$stateProvider.state(mRouteInfo);
    	}
    	
    	mRouteInfo = undefined;
    	aRoute = undefined;
    	
    	//$urlRouterProvider.otherwise('/login');
    	// Appling Router [end]
    	
    	$sceDelegateProvider.resourceUrlWhitelist(['**']);
    	
    	
    }]);
    
    app.run(["$rootScope", "$state", "$stateParams",  '$window', '$templateCache', 
    function ($rootScope, $state, $stateParams, $window, $templateCache) {
    	// 클라이언트 REST 의 리다이렉트 구현
    	$rootScope.$on("$stateChangeStart", function(evt, to, params){
    		if (to.redirectTo){
    			evt.preventDefault();
    			$state.go(to.redirectTo, params, {location: "replace"});
    		}
    	});
	}]);

    //angular.element( document ).find("html").attr("ng-app", "itm");
    //angular.bootstrap(document, ["itm"]);

	app.controller("base", ["$scope", "$rootScope", "$httpBackend", function($scope, $rootScope, $httpBackend){
		$scope.onCloseClick = function(){
            window.close();

            setTimeout(function(){
                history.back();
            }, 250);
        };
	}]);

	

	return app;
})(angular);