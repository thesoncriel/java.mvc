(function(angular, undefined){
	"use strict";

	var app = angular.module("common.directive", []);

	app.directive('ngHtml', ['$compile', function($compile) {
	    return function(scope, elem, attrs) {
	        if(attrs.ngHtml){
	            elem.html(scope.$eval(attrs.ngHtml));
	            $compile(elem.contents())(scope);
	        }
	        scope.$watch(attrs.ngHtml, function(newValue, oldValue) {
	            if (newValue && newValue !== oldValue) {
	                elem.html(newValue);
	                $compile(elem.contents())(scope);
	            }
	        });
	    };
	}]);
	
	// 출처: http://jsfiddle.net/Tentonaxe/V4axn/
	app.directive('contenteditable', ["$interval", "$timeout", "$rootScope", function($interval, $timeout, $rootScope) {
	    return {
	      restrict: 'A', // only activate on element attribute
	      require: '?ngModel', // get a hold of NgModelController
	      link: function(scope, element, attrs, ngModel) {
	      	var timer;
	      	
	        if(!ngModel) return; // do nothing if no ng-model
	
	        // Specify how UI should be updated
	        ngModel.$render = function() {
	          element.html(ngModel.$viewValue || '');
	        };
	
	        // Listen for change events to enable binding
	        element.on('blur keyup change', function() {
	          scope.$apply(read);
	        });
	        
	        $timeout(read, 500);;
	        //read(); // initialize
	
	        // Write data to the model
	        function read() {
	          var html = element.html();
	          // When we clear the content editable the browser leaves a <br> behind
	          // If strip-br attribute is provided then we strip this out
	          if( attrs.stripBr && html == '<br>' ) {
	            html = '';
	          }
	          ngModel.$setViewValue(html);
	        }
	        
	        $rootScope.$on("contenteditable.refresh", function(){
	        	read();
	        });
	      }
	    };
	}]);
})(angular);