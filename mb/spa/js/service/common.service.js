(function(angular, $, undefined){
	"use strict";

	var module = angular.module("common.service", []);

	module.service("resizeParent", function(){
		return function(){
			setTimeout(function(){
				console.log( "resizeParent", document.body.scrollHeight );
				if (window.parent.resizeIFrame){
					window.parent.resizeIFrame( $("body").innerHeight() );
				}
			},250);
		};
	});
// 자바스크립트 기능 부족한 것 보충
Array.prototype.filter||(Array.prototype.filter=function(r){"use strict";if(void 0===this||null===this)throw new TypeError;var t=Object(this),e=t.length>>>0;if("function"!=typeof r)throw new TypeError;for(var i=[],o=arguments.length>=2?arguments[1]:void 0,n=0;e>n;n++)if(n in t){var f=t[n];r.call(o,f,n,t)&&i.push(f)}return i});
if (!Array.prototype.indexOf){
	Array.prototype.indexOf = function(obj){
		var iLen = this.length
		,	i = -1
		;

		while(++i < iLen){
			if(this[i]==obj){
                return i;
            }
		}

		return -1;
    }
}

})(angular, jQuery);

