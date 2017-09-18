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
	app.controller("ClientRoomNoListController", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "resizeParent",
		function($scope, $rootScope, $location, $document, $state, $stateParams, resizeParent){
		
		var sFormName = "form_clientRoomNoList",
			objform
			;
		
		$scope.max = 5;
		$scope.nm_cust = "";
		$scope.id_cust = $stateParams.id_cust;
		$scope.useCount = 0;
		$scope.otpCount = 0;
		$scope.loadingList = false;
			
		$scope.param = {
			id_cust: $stateParams.id_cust
		};
		
		$scope.$on( sFormName + ".before", function($event, $data){
			$scope.loadingList = true;
		});
		
		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result,
				list = mData.data,
				item,
				mInfo = mData.info,
				i = list.length,
				iCount = 0,
				iOtpCount = 0,
				endvar;
			
			while(i--){
				item = list[i];
				if (item.yn_use === "Y"){
					if (item.room_no){
						iCount++;
					}
					else{
						iOtpCount++;
					}
				}
			}
			
			$scope.list = list;
			$scope.useCount = iCount;
			$scope.otpCount = iOtpCount;
			
			if (mInfo){
				$scope.max = parseInt( mInfo.wan_client_limit );
				$scope.nm_cust = mInfo.nm_cust;
			}
			else{
				$scope.max = 5;
				$scope.nm_cust = "미정";
			}
			
			$scope.loadingList = false;
			resizeParent();
		});
		
		$scope.$on( sFormName + ".ready", function($event, $data){
			objform = $data.form;
		});
		
		$rootScope.$on("client.period.list.refresh", function($event){
			objform.submit();
		});
	}]);
	
	// 해당 클라이언트의 활성/비활성 설정
	app.controller("ClientRoomNoEnableController", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "resizeParent",
		function($scope, $rootScope, $location, $document, $state, $stateParams, resizeParent){
		
		var sFormName = "form_clientRoomEnable",
			objform
			;
		
		$scope.sending = false;
		
		$scope.$on( sFormName + ".before", function($event, $data){
			var yn_use = $data.param.yn_use,
				$parentScope = $scope.$parent.$parent,
				sRoomNo = $scope.clientItem.room_no,
				iRoomMax = parseInt( $parentScope.max ),
				iRoomCurr = $parentScope.useCount
				;
			
			// 방번호가 등록 되지 않은 OTP 대상자 Loutine 추가
			if (!sRoomNo){
				if (yn_use === "N"){
					alert( $scope.msgOtpEnableNotAllow );
					$data.param.prevent = true;
					
					return;
				}
			}
			
			if (yn_use === "Y"){
				$data.param.yn_use = "N";
				$scope.sending = true;
				
				return;
			}
			
			
			
			if (iRoomCurr >= iRoomMax){
				alert( $scope.msgEnableLimit );
				$data.param.prevent = true; // 현재 수행하려던 비동기 통신을 중단 한다.
				
				return;
			}
			
			$data.param.yn_use = "Y";
			$scope.sending = true;
		});
		
		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result,
				bReturn = mData.data,
				sYnUse = $scope.clientItem.yn_use,
				sRoomNo = $scope.clientItem.room_no,
				endvar;
			
			if (bReturn){
				if (sYnUse === "Y"){
					$scope.clientItem.yn_use = "N";
					
					if (sRoomNo){
						$scope.$parent.$parent.useCount--;
					}
					else{
						$scope.$parent.$parent.otpCount--;
					}
				}
				else{
					$scope.clientItem.yn_use = "Y";
					
					$scope.$parent.$parent.useCount++;
				}
			}
			
			$scope.sending = false;
		});
		
		$scope.$on( sFormName + ".fail", function($event, $data){
			alert( $scope.msgEnableFail );
			$scope.sending = false;
		});
	}]);
	
	// 동기화 기능
	app.controller("ClientPeriodController", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "$timeout", "resizeParent",
		function($scope, $rootScope, $location, $document, $state, $stateParams, $timeout, resizeParent){
		
		var sFormName = "form_periodList",
			sFormNameSubmit = "form_periodSubmit",
			objFormSubmit,
			list, periodIndex
			;

		$scope.working = false;
		$scope.progress = 0;
		$scope.succCount = 0;
		$scope.failCount = 0;
		$scope.passCount = 0;
		$scope.errList = [];
		$scope.param = {
			url: ""
			//,timeout: 5 // 타임아웃 테스트 시 사용
		};
		
		$scope.onClick_refresh = function(){
			console.log("refresh");
			$rootScope.$broadcast("client.period.list.refresh", {});
		};                   
		
		$scope.doPeriod = function(listData, index){
			var mItem = listData[ index ],
				iLen = listData.length
				;
			
			$scope.progress = Math.floor( index / iLen * 100 );
			
			if (!mItem || index >= listData.length){
				$scope.progress = 100;
				$scope.$broadcast("submit.finish", {});
				return;
			}
			
			list = listData;
			periodIndex = index;
			
			if (mItem.yn_use === "Y"){
				$scope.param.url = "http://" + mItem.priv_ip + ":" + mItem.port + $scope.periodPath;
				objFormSubmit.submit();
			}
			// 만약 yn_use가 사용 상태가 아니라면 현재 진행을 1 증가 시키고 동기화를 수행 한다.
			else if (++index < listData.length){
				$scope.passCount++;
				$scope.doPeriod(listData, index);
			}
			else{
				$scope.progress = 100;
				$scope.$broadcast("submit.finish", {});
			}
		};
		
		$scope.$on( sFormName + ".before", function($event, $data){
			$scope.working = true;
			$scope.progress = 0;
			$scope.succCount = 0;
			$scope.failCount = 0;
			$scope.passCount = 0;
			$scope.errList = [];
			periodIndex = 0;
		});
		
		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result,
				list = mData.data
				;
			
			$scope.doPeriod(list, 0);
		});
		
		$scope.$on( sFormName + ".fail", function($event, $data){
			alert( $scope.msgEnableFail );
		});
		
		$scope.$on( sFormNameSubmit + ".ready", function($event, $data){
			objFormSubmit = $data.form;
		});
		
		$scope.$on( sFormNameSubmit, function($event, $data){
			var mData = $data.response.data;
			
			// 비교값은 view 에서 설정 해 놓음.
			if (mData.info.resCode === $scope.periodResCode){
				$scope.succCount++;
			}
			else{
				$scope.failCount++;
				$scope.errList.push( $data.param.url );
			}
			
			periodIndex++;
			$scope.doPeriod(list, periodIndex);
		});
		$scope.$on( sFormNameSubmit + ".fail", function($event, $data){
			$scope.failCount++;
			periodIndex++;
			$scope.errList.push( $data.param.url );
			$scope.doPeriod(list, periodIndex);
		});
		
		$scope.$on( "submit.finish", function($event, $data){
			$timeout(function(){
				alert( $scope.msgPeriodSubmitSuccess + "\n" +
					"성공 : " + $scope.succCount + "\n" +
					"넘김 : " + $scope.passCount + "\n" +
					"실패 : " + $scope.failCount
				);
				$scope.working = false;
				resizeParent();
			}, 250);
		} );
	}]);
})(angular);