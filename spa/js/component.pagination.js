// 페이징 컴포넌트 추가
(function($, ko, undefined){
"use strict";
ko.components.register("pagination", {
    viewModel: function(params){
        var self = this
        ,   maxPage = 1
        ;

        this.paging         = ko.observableArray([1]);
        this.navCount       = ko.observable( params.navCount || 10 );

        this.page           = params.page;
        this.count          = params.count;
        this.totalcount     = ko.observable(0);
        this.submit         = params.submit;
        this.callback       = params.callback;

        this.callback.onsuccess(function(data){
            self.totalcount( parseInt(data.count) );
            self.makePaging();
        });

        //params.callback();

        this.makePaging = function(){
            var aPaging = [],
                iPage = self.page(),
                iNavCount = self.navCount(),
                iRowCount = self.count(),
                iTotalCount = parseInt( this.totalcount() ),
                iMaxPage = Math.ceil( iTotalCount / iRowCount ),
                iStartPage = (Math.floor( ( iPage - 1 ) / iNavCount ) * iNavCount) + 1,
                iEndPage = iStartPage + iNavCount - 1,
                i
            ;
            
            if ((iMaxPage > iNavCount) && (iStartPage > iMaxPage)){
                iStartPage -= iNavCount;
            }
            
            if (iEndPage > iMaxPage){
                iEndPage = iMaxPage;
            }
            
            for(i = iStartPage; i <= iEndPage; i++){
                aPaging.push( i );
            }
            
            // console.log("page : ", iPage);
            // console.log("navCount : ", iNavCount);
            // console.log("totalCount : ", iTotalCount);
            // console.log("iMaxPage : ", iMaxPage);
            // console.log("iStartPage : ", iStartPage);
            // console.log("iEndPage : ", iEndPage);

            if (aPaging.length < 1){
                aPaging.push(1);
            }
            
            this.paging( aPaging );
            maxPage = iMaxPage;
        };

        this.onPageClick = function(val){
            var iPage = parseInt( val ),
                iCurrPage = parseInt( self.page() );
            
            if (iPage !== iCurrPage){
                self.page( iPage );
                self.submit();
            }
            
            return false;
        };
        
        this.onPrevClick = function(){
            var page = self.page();
            
            page--;
            
            if(page < 1){
                page = 1;
            }
            else{
                self.page( page );
                self.submit();
            }
            
            return false;
        };
        this.onNextClick = function(){
            var page = self.page();
            
            page++;
            
            if (page > maxPage){
                page = maxPage;
            }
            else{
                self.page( page );
                self.submit();
            }
            
            return false;
        };
    },
    template: 
    '<div class="paging-wrap text-center">\
        <div>\
            <a href="#" data-bind="click: onPrevClick"><span class="icon icon-arrow-left"></span>이전</a>\
            <!-- ko foreach: paging -->\
            <!-- ko if: ($parent.page() == $data) -->\
            <strong class="selected">[<!-- ko text: $data --><!-- /ko -->]</strong>\
            <!-- /ko -->\
            <!-- ko if: ($parent.page() != $data) -->\
            <a href="#" data-bind="click: $parent.onPageClick">[<!-- ko text: $data --><!-- /ko -->]</a>\
            <!-- /ko -->\
            <!-- /ko -->\
            <a href="#" data-bind="click: onNextClick">다음<span class="icon icon-arrow-right"></span></a>\
        </div>\
        <span class="abs-right">총: <span data-bind="text: totalcount"></span> 건</span>\
    </div>'
});
})(jQuery, ko);