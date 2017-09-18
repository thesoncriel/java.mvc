(function(angular, $, undefined){
	"use strict";

	var app = angular.module("ons.controller", ["common.service"]);

	// 요청/배송/반납 목록
	app.controller("H2tvEquipList", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "$timeout", "resizeParent", "uibButtonConfig",
		function($scope, $rootScope, $location, $document, $state, $stateParams, $timeout, resizeParent, uibButtonConfig){
		
		var sFormName = "form_H2tvEquipList"
		,	objform
		;

		$scope.loading = false;
		$scope.param = {
			page: 1,
			count: 10,
			status: ",10,20"
		};
		$scope.status = {
			p10: true,
			p20: true,
			p30: false,
			p90: false
		};

		$scope.totalcount = 0;
		$scope.navCount = 5;
		$scope.loaded = false;
		$scope.isHQ = false;

		$scope.$watchCollection("status", function(){
			var sStatus = "";

			if ($scope.loading){
				return;
			}

			angular.forEach($scope.status, function(val, key){
				if (val){
					sStatus += "," + key.replace("p", "");
				}
			});

			$scope.param.status = sStatus;
			objform.submit();
		});

		$scope.onStatusChange = function(){

		};

		$scope.onPageChange = function(){
			objform.submit();
		};

		$scope.$on(sFormName + ".before", function($event, $data){
			$scope.loading = true;
			
			resizeParent();
		});

		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result;
				
			$scope.list = mData.data;
			$scope.totalcount = mData.count;
			$scope.loading = false;
			$scope.loaded = true;
			$scope.isHQ = mData.info === "00020";

			resizeParent();
		});
		
		$scope.$on(sFormName + ".fail", function($event, $data){
			$scope.loading = false;
			resizeParent();
		});
		
		$scope.$on(sFormName + ".ready", function($event, $data){
			objform = $data.form;
		});

		$scope.$on("parent.equip.list.refresh", function($event, $data){
			console.log("refresh");
			$scope.param.page = 1;
			objform.submit();
		});

	}]);
	
	// 요청추가
	app.controller("H2tvEquipReqAdd", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "$timeout", "resizeParent",
		function($scope, $rootScope, $location, $document, $state, $stateParams, $timeout, resizeParent){
		
		var sFormName = "form_H2tvEquipReqAdd"
		,	objform
		;

		$scope.loading = false;
		$scope.param = {
			equip_type: "ASTB",
			req_cnt: "1",
			sms_receiver: "",
			reason: ""
		};

		$scope.$on(sFormName + ".before", function($event, $data){
			$scope.loading = true;
			
			resizeParent();
		});

		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result;
				
			$scope.loading = false;
			$location.path("/req");
		});
		
		$scope.$on(sFormName + ".fail", function($event, $data){
			$scope.loading = false;
		});
		
		$scope.$on(sFormName + ".ready", function($event, $data){
			objform = $data.form;

			resizeParent();
		});

	}]);


	// 요청/배송/반납 취소
	app.controller("H2tvEquipExecute", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "$timeout", "resizeParent",
		function($scope, $rootScope, $location, $document, $state, $stateParams, $timeout, resizeParent){
		
		var sFormName = "form_H2tvEquipExecute"
		,	objform
		;

		$scope.loading = false;
		$scope.param = {
			id_req: "",
			id_trans: ""
		};

		$scope.$on(sFormName + ".before", function($event, $data){
			$scope.loading = true;
			
			resizeParent();
		});

		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result;
				
			$scope.loading = false;

			$scope.$emit("parent.equip.list.refresh", false);
			console.log("emit!");
		});
		
		$scope.$on(sFormName + ".fail", function($event, $data){
			$scope.loading = false;
		});
		
		$scope.$on(sFormName + ".ready", function($event, $data){
			objform = $data.form;
		});

	}]);

	


	// 요청 출하
	app.controller("H2tvEquipReqTransAdd", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "$timeout", "resizeParent",
		function($scope, $rootScope, $location, $document, $state, $stateParams, $timeout, resizeParent){
		
		var sFormName = "form_H2tvEquipReqTransAdd"
		,	objform
		;

		$scope.loading = false;
		$scope.param = {
			id_req: $stateParams.id_req
		};

		$scope.id_req = $stateParams.id_req;
		$scope.selectedList = [];

		$scope.onItemClick = function(item){
			if (item.active === undefined){
				item.active = true;
			}
			else{
				item.active = !item.active;
			}
		};

		$scope.$on("equip.item.selected", function($event, $data){
			$scope.selectedList = $data.list;

			//$location.path("accept");
			$state.go("^.accept", {id_req: $scope.param.id_req});
		});

		$scope.$on(sFormName + ".before", function($event, $data){
			$scope.loading = true;
			
			resizeParent();
		});
		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result;
				
			$scope.loading = false;

			//$scope.$emit("equip.list.refresh");
		});
		$scope.$on(sFormName + ".fail", function($event, $data){
			$scope.loading = false;
		});
		$scope.$on(sFormName + ".ready", function($event, $data){
			objform = $data.form;
			resizeParent();
		});

	}]);

	// 소유 장비 목록
	app.controller("H2tvEquipOwnList", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "$timeout", "resizeParent",
		function($scope, $rootScope, $location, $document, $state, $stateParams, $timeout, resizeParent){
		
		var sFormName = "form_H2tvEquipOwnList"
		,	objform
		,	iReqCnt = +$stateParams.req_cnt
		,	iIdReq = +$stateParams.id_req
		;

		$scope.loading = false;
		$scope.param = {
			id_req: $stateParams.id_req,
			page: 1,
			count: 10,
			countAlt: 10,
			transret: ""
		};

		$scope.navCount = 5;
		$scope.custCount = 0;
		$scope.transCount = 0;
		$scope.req_cnt = iReqCnt;
		$scope.selectedCount = 0;

		// id_req가 0 이면 반납하는 프로세스.
		if ($stateParams.id_req !== undefined && +$stateParams.id_req === 0){
			$scope.param.transret = "true";
		}

		if ($stateParams.id_req === undefined){
			$scope.param.corp_use = "true";
		}

		function getListByPage(page, count){
			var listRaw = $scope.listRaw
			,	retList
			;

			if (angular.isArray(listRaw) === false){
				return null;
			}

			retList = listRaw.slice( ((page - 1) * count), (page * count) );

			return retList;
		};
		function getListByParam(){
			return getListByPage( $scope.param.page, $scope.param.countAlt );
		};

		$scope.comparator = function(val1, val2){
			
		};

		$scope.onItemClick = function(item){
			if (item.active === undefined){
				item.active = true;
			}
			else{
				item.active = !item.active;
			}

			if (item.active){
				$scope.selectedCount++;
			}
			else{
				$scope.selectedCount--;
			}

			if (iIdReq > 0){
				if ($scope.selectedCount > $scope.req_cnt){
					alert("요청된 개수는 " + $scope.req_cnt + "개 입니다.");

					item.active = false;
					$scope.selectedCount--;
				}
			}
		};
		$scope.onPageChange = function(){
			if ($scope.param.corp_use === "true"){
				objform.submit();
			}
			else{
				$scope.list = getListByParam();
			}
		};

		$scope.onClickSelect = function(){
			var list = $scope.listRaw.filter(function(val, index, arr){
				return val.active === true;
			});

			if (!list || list.length === 0){
				alert("장비를 선택 해 주세요.");

				return;
			}

			if ((iReqCnt > 0) && (list.length !== iReqCnt)){
				alert("요청된 개수와 보낼 장비의 개수가 맞지 않습니다.");

				return;
			}

			$scope.$emit("equip.item.selected", {list: list});
		};

		$scope.$on(sFormName + ".before", function($event, $data){
			$scope.loading = true;
			
			resizeParent();
		});
		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data
			,	mResult = mData.result
			,	iCustCnt = 0
			,	iTransCnt = 0
			;
			
			$scope.listRaw = mData.data;
			$scope.list = getListByParam();
			$scope.totalcount = mData.count;
			$scope.loading = false;

			angular.forEach(mData.data, function(item, key){
				if (item.id_cust){
					iCustCnt++;
				}
				if (item.id_trans > 0){
					iTransCnt++;
				}
			});

			$scope.custCount = iCustCnt;
			$scope.transCount = iTransCnt;

			//$scope.$emit("equip.list.refresh");

			resizeParent();
		});
		$scope.$on(sFormName + ".fail", function($event, $data){
			$scope.loading = false;
		});
		$scope.$on(sFormName + ".ready", function($event, $data){
			objform = $data.form;
			resizeParent();
		});

	}]);



	// 요청 출하
	app.controller("H2tvEquipTransAccept", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "$timeout", "resizeParent",
		function($scope, $rootScope, $location, $document, $state, $stateParams, $timeout, resizeParent){
		
		var sFormName = "form_H2tvEquipTransAccept"
		,	objform
		;

		$scope.loading = false;
		$scope.param = {
			id_req: $stateParams.id_req,
			no_delivery: "",
			notice: ""
		};

		$scope.list = $scope.selectedList;
		$scope.msgAccept = "출하를 하시겠습니까?";

		if ($stateParams.id_req === 0){
			$scope.msgAccept = "반납을 하시겠습니까?";
		}

		function removeArrayItem(arr, item){
			var index = arr.indexOf( item );

			return arr.splice(index, 1);
		};

		$scope.onItemRemove = function(item){
			removeArrayItem($scope.list, item);
		};

		// $scope.$on("equip.item.selected", function($event, $data){
		// 	$scope.selectedList = $data.list;

		// 	$state.href("transadd.accept", {id_req: $scope.param.id_req});
		// });

		$scope.$on(sFormName + ".before", function($event, $data){
			var sNoDelivery = $data.param.no_delivery
			,	list, i = -1, iLen
			,	jsonList
			;

			$scope.loading = true;

			if (!$scope.list || $scope.list.length === 0){
				alert("선택된 장비가 없습니다.\n뒤로 가셔서 다시 선택해 주세요.");

				$event.preventDefault();

				return;
			}

			if (!sNoDelivery || sNoDelivery.length < 5){
				alert("송장번호를 입력해 주세요.");

				$event.preventDefault();

				//$data.param.prevent = true;

				return;
			}

			list = $scope.list;
			iLen = list.length;
			jsonList = [];

			while(++i < iLen){
				jsonList.push({
					id_equip: list[i].id_equip
				});
			}

			$data.param.json = angular.toJson( {reqList: jsonList} );
			
			resizeParent();
		});
		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result;
				
			$scope.loading = false;

			if (mResult.valid === false){
				return;
			}

			if (mData.info > 0){
				if ($scope.param.id_req > 0){
					alert("출하 하였습니다.");
					$location.path("/trans");
				}
				else{
					alert("반납 하였습니다.");
					$location.path("/transret");
				}
				
				
			}
			else{
				alert("출하에 실패 했습니다.");
			}

			//$scope.$emit("equip.list.refresh");

		});
		$scope.$on(sFormName + ".fail", function($event, $data){
			$scope.loading = false;
		});
		$scope.$on(sFormName + ".ready", function($event, $data){
			objform = $data.form;
			resizeParent();
		});

	}]);


	// 요청 출하
	app.controller("H2tvEquipTransMod", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "$timeout", "resizeParent",
		function($scope, $rootScope, $location, $document, $state, $stateParams, $timeout, resizeParent){
		
		var sFormName = "form_H2tvEquipTransMod"
		,	sFormNameDetail = "form_H2tvEquipTransDetail"
		,	objform
		;

		$scope.loading = false;
		$scope.param = {
			id_trans: $stateParams.id_trans,
			no_delivery: "",
			notice: ""
		};

		$scope.list = $scope.selectedList;

		$scope.$on( sFormNameDetail + ".before", function($event, $data){
			$scope.loading = true;
		});
		$scope.$on( sFormNameDetail, function($event, $data){
			var mData = $data.response.data
			,	mResult = mData.result
			;

			$scope.list = mData.data;
			$scope.param.no_delivery = mData.info.no_delivery;
			$scope.param.notice = mData.info.notice;

			$scope.loading = false;

			resizeParent();
		});
		$scope.$on( sFormNameDetail + ".fail", function($event, $data){
			$scope.loading = false;
		});

		$scope.$on(sFormName + ".before", function($event, $data){
			var sNoDelivery = $data.param.no_delivery
			,	list, i = -1, iLen
			,	jsonList
			;

			$scope.loading = true;

			if (!sNoDelivery || sNoDelivery.length < 5){
				alert("송장번호를 입력해 주세요.");

				$event.preventDefault();

				return;
			}

			resizeParent();
		});
		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result;
				
			$scope.loading = false;

			if (mResult.valid === false){
				return;
			}

			if (mData.info > 0){
				alert("변경 되었습니다.");
				if (!$scope.param._ret){
					$location.path("/trans");
				}
				else{
					$location.path("/transret");
				}
			}
			else{
				alert("변경에 실패 했습니다.");
			}

			//$scope.$emit("equip.list.refresh");

		});
		$scope.$on(sFormName + ".fail", function($event, $data){
			$scope.loading = false;
		});
		$scope.$on(sFormName + ".ready", function($event, $data){
			objform = $data.form;
			resizeParent();
		});

	}]);



	app.controller("H2tvEquipCustList", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "$timeout", "resizeParent",
		function($scope, $rootScope, $location, $document, $state, $stateParams, $timeout, resizeParent){
		
		var sFormName = "form_H2tvEquipCustList"
		,	objform
		;

		$scope.loading = false;
		$scope.param = {
			search_method: "0",
			page: 1,
			count: 500
		};

		$scope.totalcount = 0;
		$scope.navCount = 5;
		$scope.loaded = false;
		$scope.isHQ = false;

		$scope.$on(sFormName + ".before", function($event, $data){
			$scope.loading = true;
			$scope.isHQ = false;
			
			resizeParent();
		});

		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result;
				
			$scope.list = mData.data;
			$scope.totalcount = mData.count;
			$scope.loading = false;
			$scope.loaded = true;
			$scope.isHQ = mData.info === "00020";

			resizeParent();
		});
		
		$scope.$on(sFormName + ".fail", function($event, $data){
			$scope.loading = false;
			resizeParent();
		});
		
		$scope.$on(sFormName + ".ready", function($event, $data){
			objform = $data.form;
		});

		$scope.$on("parent.equip.list.refresh", function($event, $data){
			console.log("refresh");
			$scope.param.page = 1;
			objform.submit();
		});

	}]);



	app.controller("H2tvEquipNobrodChange", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "$timeout", "resizeParent",
		function($scope, $rootScope, $location, $document, $state, $stateParams, $timeout, resizeParent){
		
		var sFormName = "form_H2tvEquipNobrodChange"
		,	objform
		;

		$scope.loading = false;
		$scope.param = {
			id_equip: "",
			no_brod: "1"
		};

		$scope.onNobrodChange = function(index){
			$scope.$emit("equip.item.no_brod.change", index, $scope.param.no_brod);
			objform.submit();
		};

		$scope.$on(sFormName + ".before", function($event, $data){
			$scope.loading = true;
			
			resizeParent();
		});
		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result;
			
			$scope.loading = false;

			//$scope.$emit("equip.list.refresh");
		});
		$scope.$on(sFormName + ".fail", function($event, $data){
			$scope.loading = false;
		});
		$scope.$on(sFormName + ".ready", function($event, $data){
			objform = $data.form;
			resizeParent();
		});
	}]);


	// 장비 할당 서비스
	app.service("h2tvEquipCustAssignService", function(){
		return function($scope){
			function clearActivatedItems(item){
				if ($scope.activeItem === item){
					return;
				}

				if ($scope.activeItem){
					$scope.activeItem.active = false;
				}
			};

			return {
				onItemClick: function(item){
					clearActivatedItems(item);

					if (item.active === undefined){
						item.active = true;
					}
					else{
						item.active = !item.active;
					}

					$scope.activeItem = item;

					if (item.active){
						$scope.$emit("equip.item.select", item);
					}
					else{
						$scope.$emit("equip.item.deselect", item);
					}
				},

				removeArrayItem: function(arr, item){
					var index = arr.indexOf( item );

					return arr.splice(index, 1);
				}
			};
		};
	});


	// 가맹점 할당 장비
	app.controller("H2tvEquipCustOwnList", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "$timeout", "resizeParent", "h2tvEquipCustAssignService",
		function($scope, $rootScope, $location, $document, $state, $stateParams, $timeout, resizeParent, h2tvService){
		
		var sFormName = "form_H2tvEquipCustOwnList"
		,	objform
		,	service = h2tvService($scope)
		;

		$scope.nowLoading = false;
		$scope.param = {
			id_cust: $stateParams.id_cust
		};

		$scope.navCount = 5;
		$scope.activeItem = undefined;

		$scope.onItemClick = service.onItemClick;

		$scope.$on(sFormName + ".before", function($event, $data){
			$scope.nowLoading = true;
			
			resizeParent();
		});
		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result;
			
			$scope.list = mData.data;
			$scope.totalcount = mData.count;
			$scope.nowLoading = false;

			$scope.$emit("equip.maxAssign.send", mData.etc);
			$scope.$emit("equip.cust.list.change", $scope.list.length);
			$scope.$emit("equip.cust.svc.info", mData.info);
		});
		$scope.$on(sFormName + ".fail", function($event, $data){
			$scope.nowLoading = false;
		});
		$scope.$on(sFormName + ".ready", function($event, $data){
			objform = $data.form;
			resizeParent();
		});

		$scope.$on("equip.item.add.cust", function($event, item){
			var list = $scope.list
			;

			item.yn_use = "Y";
			item.no_brod = "1";
			item.active = false;

			if (angular.isArray(list) && 
				(list.length > 0) &&
				(list[0].no_brod == "1")
			){
				console.log("equip.item.add.cust", list);
				item.no_brod = "2";
			}

			$scope.list.push(item);

			$scope.$emit("equip.cust.list.change", $scope.list.length);
		});
		$scope.$on("equip.item.add.corp", function($event, item){
			service.removeArrayItem($scope.list, item);

			$scope.$emit("equip.cust.list.change", $scope.list.length);
		});

		$scope.$on("equip.item.no_brod.change", function($event, index, no_brod){
			$scope.list[index].no_brod = no_brod;
			//console.log($event, index, no_brod);
		});
	}]);

	// 지사 소유 장비
	app.controller("H2tvEquipCorpOwnList", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "$timeout", "resizeParent", "h2tvEquipCustAssignService",
		function($scope, $rootScope, $location, $document, $state, $stateParams, $timeout, resizeParent, h2tvService){
		
		var sFormName = "form_H2tvEquipCorpOwnList"
		,	objform
		,	service = h2tvService($scope)
		;

		$scope.nowLoading = false;
		$scope.param = {
			page: 1,
			count: 10,
			transret: "true"
		};

		$scope.navCount = 5;
		$scope.activeItem = undefined;

		$scope.onItemClick = service.onItemClick;

		$scope.onPageChange = function(){
			objform.submit();
		};

		$scope.$on(sFormName + ".before", function($event, $data){
			$scope.nowLoading = true;
			
			resizeParent();
		});
		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result;
			
			$scope.list = mData.data;
			$scope.totalcount = mData.count;
			$scope.nowLoading = false;

			//$scope.$emit("equip.list.refresh");
		});
		$scope.$on(sFormName + ".fail", function($event, $data){
			$scope.nowLoading = false;
		});
		$scope.$on(sFormName + ".ready", function($event, $data){
			objform = $data.form;
			resizeParent();
		});

		$scope.$on("equip.item.add.cust", function($event, item){
			service.removeArrayItem($scope.list, item);
		});
		$scope.$on("equip.item.add.corp", function($event, item){
			item.yn_use = "N";
			item.no_brod = "";
			item.active = false;
			$scope.list.push(item);
		});
	}]);

	// 요청/배송/반납 취소
	app.controller("h2tvEquipCustAssign", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "$timeout", "resizeParent", "h2tvEquipCustAssignService",
		function($scope, $rootScope, $location, $document, $state, $stateParams, $timeout, resizeParent, h2tvService){
		
		var sFormNameCust = "form_h2tvEquipMoveCust"
		,	sFormNameCorp = "form_h2tvEquipMoveCorp"
		,	sFormNameSync = "form_SMSAsync"
		,	objformCust
		,	objformCorp
		,	objformSync
		,	selectedFromCust
		,	selectedFromCorp
		;

		$scope.nowLoading = false;
		$scope.isMaxAssign = false;
		$scope.maxAssign = 2;
		$scope.paramCust = {
			id_equip: "",
			id_cust: $stateParams.id_cust
		};
		$scope.paramCorp = {
			id_equip: "",
			id_cust: $stateParams.id_cust
		};
		$scope.paramSync = {};
		$scope.svcInfo = {};


		// 가맹점으로 올리기
		$scope.$on(sFormNameCust + ".before", function($event, $data){
			if (!selectedFromCorp){
				$event.preventDefault();

				return;
			}

			if ($scope.isMaxAssign){
				alert("해당 가맹점은 이미 '최대 할당상태' 입니다.");
				$event.preventDefault();

				return;
			}

			$scope.nowLoading = true;
		});

		$scope.$on( sFormNameCust, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result;

			$scope.nowLoading = false;

			if (mResult.valid){
				$scope.$broadcast("equip.item.add.cust", selectedFromCorp );
			}
			
			selectedFromCorp = undefined;
		});
		
		$scope.$on(sFormNameCust + ".fail", function($event, $data){
			$scope.nowLoading = false;
		});
		
		$scope.$on(sFormNameCust + ".ready", function($event, $data){
			objformCust = $data.form;
		});


		// 지사로 내리기
		$scope.$on(sFormNameCorp + ".before", function($event, $data){
			if (!selectedFromCust){
				$event.preventDefault();

				return;
			}

			$scope.nowLoading = true;
		});

		$scope.$on( sFormNameCorp, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result;
				
			$scope.nowLoading = false;

			$scope.$broadcast("equip.item.add.corp", selectedFromCust );
			selectedFromCust = undefined;
		});
		
		$scope.$on(sFormNameCorp + ".fail", function($event, $data){
			$scope.nowLoading = false;
		});
		
		$scope.$on(sFormNameCorp + ".ready", function($event, $data){
			objformCorp = $data.form;
		});


		// SMS 서버 동기화 하기
		$scope.$on(sFormNameSync + ".before", function($event, $data){
			$scope.nowLoading = true;
		});

		$scope.$on( sFormNameSync, function($event, $data){
			var mData = $data.response.data,
				mResult = mData.result;

			if (mResult.valid){
				alert("SMS 동기화에 성공 하였습니다.");
			}
			
			$scope.nowLoading = false;
		});
		
		$scope.$on(sFormNameSync + ".fail", function($event, $data){
			$scope.nowLoading = false;
		});
		
		$scope.$on(sFormNameSync + ".ready", function($event, $data){
			objformSync = $data.form;
		});


		// 장비 아이템 상태 변경 이벤트
		$scope.$on("equip.item.select", function($event, item){
			if (item.yn_use === "Y"){
				$scope.paramCorp.id_equip = item.id_equip;
				selectedFromCust = item;
			}
			else{
				$scope.paramCust.id_equip = item.id_equip;
				selectedFromCorp = item;
			}
		});
		$scope.$on("equip.item.deselect", function($event, item){
			if (item.yn_use === "Y"){
				$scope.paramCorp.id_equip = "";
				selectedFromCust = undefined;
			}
			else{
				$scope.paramCust.id_equip = "";
				selectedFromCorp = undefined;
			}
		});

		$scope.$on("equip.cust.list.change", function($event, length){
			if (length >= $scope.maxAssign){
				$scope.isMaxAssign = true;
			}
			else{
				$scope.isMaxAssign = false;
			}
		});

		$scope.$on("equip.maxAssign.send", function($event, maxAssign){
			try{
				$scope.maxAssign = parseInt( maxAssign );
			}
			// 만약 문제가 생긴다면 기본값 2로 사용함.
			catch(e){
				$scope.maxAssign = 2;
			}
			
		});

		$scope.$on("equip.cust.svc.info", function($event, info){
			if (info){
				$scope.svcInfo = info;
			}
			else{
				$scope.svcInfo = {};
			}
		});
	}]);



	// 배송중인 장비 목록
	app.controller("H2tvEquipListByTrans", 
		["$scope", "$rootScope", "$location", "$document", "$state", "$stateParams", "$timeout", "resizeParent",
		function($scope, $rootScope, $location, $document, $state, $stateParams, $timeout, resizeParent){
		
		var sFormName = "form_H2tvEquipListByTrans"
		,	objform
		;

		$scope.loading = false;
		$scope.param = {
			id_trans: ""
		};

		$scope.$on(sFormName + ".before", function($event, $data){
			$scope.loading = true;
			
			resizeParent();
		});

		$scope.$on( sFormName, function($event, $data){
			var mData = $data.response.data
			,	mResult = mData.result
			,	sMsg = ""
			,	iLen, i, list = mData.data
			;

			if (angular.isArray(list) === false){	
				$scope.loading = false;

				return;
			}
			else if (list.length === 0){
				alert("검색 결과가 없습니다.");

				$scope.loading = false;

				return;
			}

			list = mData.data
			iLen = list.length;
			i = -1

			while(++i < iLen){
				sMsg += "\n" + list[i].no_serial + " : " + list[i].mac;
			}

			$scope.loading = false;

			alert( "배송중인 장비" + sMsg );

			resizeParent();
		});
		
		$scope.$on(sFormName + ".fail", function($event, $data){
			$scope.loading = false;
			resizeParent();
		});
		
		$scope.$on(sFormName + ".ready", function($event, $data){
			objform = $data.form;
		});

	}]);



})(angular, jQuery);