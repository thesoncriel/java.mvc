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
	
	// 가맹점 계약 이미지 상세
	app.controller("CustSvcImgDetailController", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "$timeout", "resizeParent",
		function($scope, $rootScope, $location, $document, $state, $stateParams, $timeout, resizeParent){
		
		var sFormName = "form_custSvcImgList",
			objform
			;

		$scope.loading = false;
		$scope.id_cust_svc = "";
		$scope.svcInfo = [];
		$scope.svcImgList = [];
		$scope.selected = -1;
			
		$scope.param = {
			id_cust: $stateParams.id_cust
		};

		var SvcimgService = function(){
			this.svcimgSizeCheck = function(list, id_cust_svc){
				var i = -1,
					iLen = list.length;

				while(++i < iLen){
					if (!list[i]){
						list[i] = {
							id_cust_svc: id_cust_svc,
							seq: i + 1,
							yn_confirm: "N",
							yn_default: "N",
							file_size: 0
						};
					}

					//console.log("svcimgSizeCheck.while", list[i]);
				}

				//console.log("svcimgSizeCheck", list);

				return list;
			};

			this.svcimgCheckAndFill = function(data, id_cust_svc){
				var i = -1,
					argForFilter = {id: id_cust_svc},
					list = data.filter(function(item, index, arr){
							return item.id_cust_svc === this.id;
						}, argForFilter),
					aRet = [null, null, null],
					iLen = aRet.length,
					item,
					iItemSeq = 0,
					iCurrSeq = 0
					;

				//console.log("svcimgCheckAndFill.list");
				//console.log(JSON.stringify(list));

				if (list.length === 3){
					return list;
				}

				while(++i < iLen){
					item = list[i];

					if (item){
						iItemSeq = parseInt( item.seq );

						aRet[ iItemSeq - 1 ] = item;
					}
				}

				//console.log("svcimgCheckAndFill.return");
				//console.log(JSON.stringify(aRet));

				return this.svcimgSizeCheck(aRet, id_cust_svc);
			};

			this.svcimgDataSupplement = function(svcInfo, data){
				var i = -1,
					iLen = svcInfo.length,
					aRet = [],
					endvar;

				while(++i < iLen){
					aRet = aRet.concat( this.svcimgCheckAndFill(data, svcInfo[i].id_cust_svc) );
					//console.log("while", aRet);
				}

				return aRet;
			};
		};

		$scope.onClickSvcInfo = function(item, index){
			if ($scope.selected !== index){
				$scope.id_cust_svc = item.id_cust_svc;
				$scope.selected = index;
			}
			else{
				//$scope.id_cust_svc = "";
				//$scope.selected = -1;
			}
			resizeParent();
		};
		
		$scope.$on( sFormName + ".before", function($event, $data){
			$scope.loading = true;
		});
		
		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result,
				list = mData.data,
				service = new SvcimgService(),
				endvar;

			$scope.svcInfo = list;
			$scope.svcImgList = service.svcimgDataSupplement(list, mData.info);

			if (list.length > 0 ){
				$scope.id_cust_svc = list[0].id_cust_svc;
				$scope.selected = 0;
			}

			resizeParent();

			$scope.loading = false;			
		});
		
		$scope.$on( sFormName + ".ready", function($event, $data){
			objform = $data.form;
		});
	}]);

	// 계약서 자료 변경
	app.controller("CustSvcImgModifyController", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "$timeout", "resizeParent",
		function($scope, $rootScope, $location, $document, $state, $stateParams, $timeout, resizeParent){
		
		var sFormName_upload = "form_svcimgUpload",
			sFormName_default = "form_svcimgModifyDefault",
			sFormName_delete = "form_svcimgDelete",
			objform,
			item
			;

		$scope.setItem = function(value){
			if (item === value){
				return;
			}

			item = value;
		};

		function allDefaultToNo(list){
			var i = -1,
				iLen = list.length;

			while(++i < iLen){
				list[i].yn_default = "N";
			}
		};

		function clearStatus(){
			$rootScope.modifying = false;
			$rootScope.uploading = false;
		};

		$scope.$on( sFormName_upload + ".before", function($event, $data){
			$rootScope.uploading = true;
		});
		$scope.$on( sFormName_upload, function($event, $data){
			item.yn_confirm = "Y";

			alert( $scope.msgUploadComplete );

			clearStatus();
		});

		$scope.$on( sFormName_default + ".before", function($event, $data){
			if (item.yn_default !== "Y"){
				item.yn_default = "Y";
			}
			else{
				item.yn_default = "N";
			}

			$rootScope.modifying = true;
		});
		$scope.$on( sFormName_default, function($event, $data){
			var isDefault = "";

			if ($data.response.data.data){
				allDefaultToNo( $scope.currFiltered );
				isDefault = item.yn_default;
				item.yn_default = (isDefault !== "Y")? "Y" : "N";
			}
			clearStatus();
		});
		

		$scope.$on( sFormName_delete + ".before", function($event, $data){
			$rootScope.modifying = true;
		});
		$scope.$on( sFormName_delete, function($event, $data){
			var isDefault = "";

			if ($data.response.data.data){
				item.yn_confirm = "N";
				item.yn_default = "N";
			}
			clearStatus();
		});

		$scope.$on( sFormName_upload + ".fail", clearStatus);
		$scope.$on( sFormName_default + ".fail", clearStatus);
		$scope.$on( sFormName_delete + ".fail", clearStatus);
	}]);

	// 특정 가맹점의 계약 이미지 (팝업)
	app.controller("CustSvcImgPopupController", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "resizeParent",
		function($scope, $rootScope, $location, $document, $state, $stateParams, resizeParent){
		
		var sFormName = "",
			objform
			;
		
		$scope.id_cust_svc = $stateParams.id_cust_svc;
		$scope.seq = $stateParams.seq;

		
	}]);
})(angular);