(function(angular, undefined){
	"use strict";

	var app = angular.module("ons.controller", ["common.service"]);

	// 해당 지사에 대한 가맹점 목록
	app.controller("CustListController", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "resizeParent",
		function($scope, $rootScope, $location, $document, $state, $stateParams, resizeParent){
		
		var sFormName = "form_custList",
			objform
			;
		
		$scope.loading = false;
		$scope.param = {
			search_method: "0",
			id_cust: ""
		};
		
		$scope.onClick_default = function(){
			$scope.param.search_method = "0";
			$scope.param.id_cust = "";
			
			objform.submit();
		};
		
		$scope.$on (sFormName + ".before", function($event, $data){
			$scope.loading = true;
			resizeParent();
		});
		
		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result;
				
			$scope.list = mData.data;
			$scope.loading = false;
			resizeParent();
		});
		
		$scope.$on (sFormName + ".fail", function($event, $data){
			$scope.loading = false;
			resizeParent();
		});
		
		$scope.$on (sFormName + ".ready", function($event, $data){
			objform = $data.form;
		});
	}]);
	
	// 가맹점에 대한 클라이언트 목록
	app.controller("CustDetailController", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "resizeParent",
		function($scope, $rootScope, $location, $document, $state, $stateParams, resizeParent){
		
		var sFormName = "form_custDetail",
			objform
			;
		
		$scope.detail = {};
		$scope.svcInfo = [];
		$scope.linenet = {};

		$scope.param = {
			id_cust: $stateParams.id_cust,
			with: "linenet"
		};
		
		$scope.$on( sFormName + ".before", function($event, $data){
			$scope.loadingList = true;
		});
		
		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result,
				mDetail = mData.data,
				endvar;
			
			$scope.detail = mDetail;
			$scope.svcInfo = mData.info;
			$scope.linenet = mData.etc;
			
			$scope.loadingList = false;
			resizeParent();
		});
		
		$scope.$on( sFormName + ".ready", function($event, $data){
			objform = $data.form;
		});

		$scope.$on( sFormName + ".fail", function($event, $data){
			alert( $scope.msgLookupDetailFail );
			$scope.sending = false;
			$scope.detail = {};
			$scope.svcInfo = [];
		});
	}]);
})(angular);