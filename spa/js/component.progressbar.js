// 프로그레스바 컴포넌트
(function($, ko, undefined){
"use strict";
ko.components.register("progressbar", {
    viewModel: function(params){
        var self = this
        ;

        this.visible        = params.visible || ko.observable(true);
        this.callback       = params.callback;
        this.desc           = params.desc || "진행중... ";
        this.progress       = params.progress || ko.observable(100);
        this.cur            = params.cur || ko.observable(0);
        this.max            = params.max || ko.observable(0);
        this.err            = params.err || ko.observable(0);
        this.showDetail     = params.showDetail || false;
        this.showError      = params.showError || false;

        if (this.callback){
            this.callback.onprogress(function(item. viewModel){
                self.setInfo(item);
            });
        }

        this.setInfo = function(info){
            var cur, max, err;

            if (!info){
                return;
            }

            if (info.cur){
                cur = parseInt(info.cur);
                this.curr(cur);
            }
            if (info.max){
                max = parseInt(info.max);
                this.max(max);
            }
            if (info.err){
                err = parseInt(info.err);
                this.error(err);
            }

            if (cur && max){
                this.progress( cur / max * 100 );
            }
        };
    },
    template: 
    '<div data-bind="visible: visible">\
        <div class="progress-bar">\
            <div data-bind="style: {\'width\': progress() + \'%\'}"></div>\
        </div>\
        <b data-bind="text: desc"></b> \
        <!-- ko if: showDetail -->\
            <span data-bind="text: curr"></span> / <span data-bind="text: max"></span> \
          <!-- ko if: showError -->\
            &nbsp; | 오류(<!-- ko text: error --><!-- /ko -->) \
          <!-- /ko -->\
            - 진행율: <!-- ko text: Math.floor(progress()) --><!-- /ko -->%\
        <!-- /ko -->\
    </div>'
});
})(jQuery, ko);