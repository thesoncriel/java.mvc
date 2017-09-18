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
				templateUrl: sViewRoot + "custmng/lookup.custlist.html"
			},
			{
				name: "custdetail",
				url: "/custdetail/:id_cust",
				templateUrl: sViewRoot + "custmng/lookup.custdetail.html"
			}
		];
	};
})(angular);