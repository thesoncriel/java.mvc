var onsroute = (function(angular, undefined){
	"use strict";

	return function(rootPath){
		var sViewRoot = rootPath || "/";

		return [
			{
				name: "index",
				url: "/",
				redirectTo: "reqlist",
				params: {
					req_state: "N"
				}
			},
			{
				name: "reqlist",
				url: "/reqlist/:req_state",
				templateUrl: sViewRoot + "custmng/regreq.list.html"
			}
		];
	};
})(angular);