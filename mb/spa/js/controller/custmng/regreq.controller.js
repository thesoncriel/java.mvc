(function(angular, $, undefined){
	"use strict";

	var app = angular.module("ons.controller", ["common.service"]);

	// 등록 요청 목록
	app.controller("CustRegReqListController", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "$timeout", "resizeParent",
		function($scope, $rootScope, $location, $document, $state, $stateParams, $timeout, resizeParent){
		
		var sFormName = "form_custRegReqList",
			objform,
			modalSvcImg,
			endvar
			;
		
		$scope.loading = false;
		$scope.param = {
			req_state: $stateParams.req_state,
			page: 1,
			count: 10
		};

		$scope.id_reg_seq = "";
		$scope.navCount = 5;
		$scope.totalcount = 0;
		$scope.uploading = false;

		$scope.onSvcShowClick = function(seq){

		};

		$scope.onPageChange = function(){
			objform.submit();
		};

		$scope.$on("form_custRegReqImgUpload.before", function($event, $data){
			$scope.uploading = true;
		});

		$scope.$on("form_custRegReqImgUpload", function($event, $data){
			var msg = $data.result.msg,
				path = $data.data;

			if (msg){
				alert(msg);
			}
			else{
				$scope.param.page = 1;
				$scope.onPageChange();
			}

			$scope.uploading = false;
		});
		
		$scope.$on(sFormName + ".before", function($event, $data){
			$scope.loading = true;
			
			resizeParent();
			// modalSvcImg = $(".modal");
			// modalSvcImg.modal({
			// 	show: false,
			// 	backdrop: false
			// });
		});
		
		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result;
				
			$scope.list = mData.data;
			$scope.totalcount = mData.count;
			$scope.loading = false;
			resizeParent();
		});
		
		$scope.$on(sFormName + ".fail", function($event, $data){
			$scope.loading = false;
			resizeParent();
		});
		
		$scope.$on(sFormName + ".ready", function($event, $data){
			objform = $data.form;
		});

		$scope.$on("$destroy", function(){
			modalSvcImg.data("bs.modal", null);
			modalSvcImg = undefined;
		});
	}]);
})(angular, jQuery);