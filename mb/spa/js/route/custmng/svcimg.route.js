var onsroute = (function(angular, undefined){
	"use strict";

	return function(rootPath){
		var sViewRoot = rootPath || "/";

		return [
			{
				name: "index",
				url: "/",
				redirectTo: "custlist"
			},
			{
				name: "custlist",
				url: "/custlist",
				templateUrl: sViewRoot + "custmng/svcimg.custlist.html"
			},
			{
				name: "svcdetail",
				url: "/svcdetail/:id_cust",
				templateUrl: sViewRoot + "custmng/svcimg.svcdetail.html?v=1"
			},
			{
				name: "svcimgpopup",
				url: "/svcimg/:id_cust_svc/:seq",
				template: function(){
					return '<img ng-src="/ctrl/custmng/svcimg.img.jsp?id_cust_svc={{id_cust_svc}}&seq={{seq}}" alt="">';
				},
				controller: "CustSvcImgPopupController"
			}
		];
	};
})(angular);