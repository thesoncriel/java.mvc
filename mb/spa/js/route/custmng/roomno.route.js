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
				templateUrl: sViewRoot + "custmng/roomno.custlist.html"
			},
			{
				name: "clientlist",
				url: "/clientlist/:id_cust",
				templateUrl: sViewRoot + "custmng/roomno.clientlist.html"
			}
		];
	};
})(angular);