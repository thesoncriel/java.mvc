var onsroute = (function(angular, undefined){
	"use strict";

	return function(rootPath){
		var sViewRoot = (rootPath || "/") + "h2tv/";

		return [
			{
				name: "index",
				url: "/",
				redirectTo: "reqadd"
			}
			// ,{
			// 	name: "req",
			// 	url: "/req",
			// 	abstract: true,
			// 	template: '<ui-view data-autoscroll="false"></ui-view>',
			// 	redirectTo: "req.list"
			// }
			,{
				name: "req",
				url: "/req",
				templateUrl: sViewRoot + "equip.req.html"
			}
			,{
				name: "reqadd",
				url: "/req/add",
				templateUrl: sViewRoot + "equip.req.add.html"
			}
			,{
				name: "transadd",
				url: "/trans/add/:id_req/:req_cnt",
				abstract: true,
				templateUrl: sViewRoot + "equip.trans.add.html"
			}
			,{
				name: "transadd.ownlist",
				parent: "transadd",
				url: "/ownlist",
				templateUrl: sViewRoot + "equip.trans.ownlist.html"
			}
			,{
				name: "transadd.accept",
				parent: "transadd",
				url: "/accept",
				templateUrl: sViewRoot + "equip.trans.accept.html"
			}
			,{
				name: "trans",
				url: "/trans",
				templateUrl: sViewRoot + "equip.trans.html"
			}
			,{
				name: "transmod",
				url: "/trans/mod/:id_trans",
				templateUrl: sViewRoot + "equip.trans.mod.html"
			}
			,{
				name: "transret",
				url: "/transret",
				templateUrl: sViewRoot + "equip.transret.html"
			}
			,{
				name: "transretmod",
				url: "/transret/mod/:id_trans",
				templateUrl: sViewRoot + "equip.transret.mod.html"
			}
			,{
				name: "cust",
				url: "/cust",
				templateUrl: sViewRoot + "equip.cust.list.html"
			}
			,{
				name: "assign",
				url: "/cust/assign/:id_cust",
				templateUrl: sViewRoot + "equip.cust.assign.html"
			}
			,{
				name: "ownlist",
				url: "/ownlist",
				templateUrl: sViewRoot + "equip.ownlist.html"
			}
		];
	};
})(angular);