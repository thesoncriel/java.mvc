/**
 * converted stringify() to jQuery plugin.
 * serializes a simple object to a JSON formatted string.
 * Note: stringify() is different from jQuery.serialize() which URLEncodes form elements
 * UPDATES:
 *      Added a fix to skip over Object.prototype members added by the prototype.js library
 * USAGE:
 *  jQuery.ajax({
 *      data : {serialized_object : jQuery.stringify (JSON_Object)},
 *      success : function (data) {
 *
 *      }
 *   });
 *
 * CREDITS: http://blogs.sitepointstatic.com/examples/tech/json-serialization/json-serialization.js
 */
jQuery.extend({
    stringify  : function stringify(obj) {
        var t = typeof (obj);
        if (t != "object" || obj === null) {
            // simple data type
            if (t == "string") obj = '"' + obj + '"';
            return String(obj);
        } else {
            // recurse array or object
            var n, v, json = [], arr = (obj && obj.constructor == Array);

            for (n in obj) {
                v = obj[n];
                t = typeof(v);
                if (obj.hasOwnProperty(n)) {
                    if (t == "string") v = '"' + v + '"'; else if (t == "object" && v !== null) v = jQuery.stringify(v);
                    json.push((arr ? "" : '"' + n + '":') + String(v));
                }
            }
            return (arr ? "[" : "{") + String(json) + (arr ? "]" : "}");
        }
    }
});

/******************************************************************************/
// 유틸리티 구성 부분 [시작]


var Util = (function($, ko, undefined){
    "use strict";

    // 출처: http://stackoverflow.com/questions/979975/how-to-get-the-value-from-the-url-parameter
    var QueryString = (function () {
      // This function is anonymous, is executed immediately and 
      // the return value is assigned to QueryString!
      var query_string = {};
      var query = window.location.search.substring(1);
      var vars = query.split("&");
      for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
            // If first entry with this name
        if (typeof query_string[pair[0]] === "undefined") {
          query_string[pair[0]] = decodeURIComponent(pair[1]);
            // If second entry with this name
        } else if (typeof query_string[pair[0]] === "string") {
          var arr = [ query_string[pair[0]],decodeURIComponent(pair[1]) ];
          query_string[pair[0]] = arr;
            // If third or later entry with this name
        } else {
          query_string[pair[0]].push(decodeURIComponent(pair[1]));
        }
      } 
      return query_string;
    })();

    return {
        serializeMap : function( jqElem, arrToOneStr, delimiter ){
            var mData = {};
            var jqInput = null;
            var sName = "";
            var sValue = "";
            var sType = "";
            var sTmp = "";
            var isArrToOneStr = $.isArray(arrToOneStr);
            var sDelimiter = delimiter || ",";
            var mCacheChecked = {};
            
            jqElem.find("input[type!='button'][type!='submit'][type!='image'][data-placeholder-active!='true'], select, textarea").each(function(index){
                jqInput = $(this);
                
                if (jqInput.attr("disabled") !== undefined) return;
                
                sType = jqInput.attr("type");
                
                sName = jqInput.attr("name");
                sValue = jqInput.val();
                
                if (mData.hasOwnProperty( sName ) === false){
                    if ((sType === "checkbox") || (sType === "radio")){
                        if (this.checked === true){
                            mData[ sName ] = sValue;
                        }
                        else{
                            mCacheChecked[ sName ] = "";
                        }
                    }
                    else{
                        mData[ sName ] = sValue;
                    }
                }
                else {
                    if ((sType === "checkbox") || (sType === "radio")){
                        if (mCacheChecked.hasOwnProperty( sName ) === true){
                            mData[ sName ] = "";
                            delete mCacheChecked[ sName ];
                        }
                        if (this.checked === false){
                            sValue = "";
                        }
                    }
                    
                    if (isArrToOneStr === true){
                        if (arrToOneStr.indexOf( sName ) >= 0){
                            mData[ sName ] = mData[ sName ] + sDelimiter + sValue;
                        }
                    }
                    else{
                        if ($.isArray(mData[ sName ]) === false){
                            sTmp = mData[ sName ];
                            mData[ sName ] = [];
                            mData[ sName ][0] = sTmp;
                        }
                        mData[ sName ][ mData[ sName ].length ] = sValue;
                    }
                }
            });
            
            return mData;
        },

        placeholders: (function($){
            var placeholder = window.Placeholders || false
            ,   fnFocusIn
            ;

            fnFocusIn = function(){
                $(this)
                .removeClass("placeholdersjs")
                .removeClass("placeholderjs");
            };

            return function(elem){
                var jqElem;

                if (!placeholder || placeholder.nativeSupport){
                    return;
                }

                jqElem = $(elem);

                jqElem.find("[placeholder]")
                .each(function(index){
                    $(this)
                    .unbind("focusin", fnFocusIn)
                    .bind("focusin", fnFocusIn);

                    placeholder.enable(this);
                });
            };
        })(jQuery),

        getParam: function(){
            return QueryString;
        },

        addDate : function(date, intVal, useFmt){
            var dateData = null;
            var saDate = null;
            
            if (date instanceof Date){
                dateData = date;
            }
            else{
                //saDate = date.split("-");
                dateData = new Date( date );
                //dateData.setFullYear( saDate[0] );
                //dateData.setMonth( parseInt(saDate[1]) - 1 );
                //dateData.setDate( saDate[2] );
            }
            dateData.setDate( dateData.getDate() + intVal );

            if (useFmt === false){
                return dateData;
            }

            return this.dateFormat(dateData);
        },

        dateFormat : function(date, delimiter) {
            var sDelimiter = delimiter || "-";
            
            if (date instanceof Date){
                var iMonth = date.getMonth() + 1;
                var iDate = date.getDate();
                
                return date.getFullYear() + sDelimiter + ((iMonth < 10)? "0" + iMonth : iMonth) + sDelimiter + ((iDate < 10)? "0" + iDate : iDate);
            }
            else{
                return date.substring(0, 4) + sDelimiter + date.substring(4, 6) + sDelimiter + date.substring(6, 8);
            }
        },

        getPrevMonthFirstDate: function(dt){
            var date = dt || new Date(),
                retDate = new Date( date )
            ;
            
            retDate.setMonth( retDate.getMonth() - 1 );
            retDate.setDate( 1 );

            if (retDate.getMonth() === date.getMonth()){
                retDate.setMonth( retDate.getMonth() - 1 );
            }

            return retDate;
        }
    };
})($, ko);
// 유틸리티 구성 부분 [종료]


(function(){
    "use strict";

    if (!window.console){
        window.console = {
            log: function(args){
                
            }
        };
    }

    if (!Array.indexOf) {
      Array.prototype.indexOf = function (obj, start) {
        for (var i = (start || 0); i < this.length; i++) {
          if (this[i] == obj) {
            return i;
          }
        }
      }
    }

    if(typeof String.prototype.trim !== 'function') {
      String.prototype.trim = function() {
        return this.replace(/^\s+|\s+$/g, ''); 
      }
    }

    Array.prototype.filter||(Array.prototype.filter=function(r){"use strict";if(void 0===this||null===this)throw new TypeError;var t=Object(this),e=t.length>>>0;if("function"!=typeof r)throw new TypeError;for(var i=[],o=arguments.length>=2?arguments[1]:void 0,n=0;e>n;n++)if(n in t){var f=t[n];r.call(o,f,n,t)&&i.push(f)}return i});
})();

var ViewModel = (function($, ko, undefined){
    "use strict";

    // 뷰모델 구성 부분 [시작]
    var ViewModel = function(elem, observs, observArrs, fnEtc){
        var self = this,
            util = Util,
            context = $(elem),
            ctrlDown = false,
            selectedIndex = -1,
            selectedData,
            dataReqUrl,
            dataReqMethod = "",
            maxPage = 1,
            lastActionUrl = undefined,
            lastParam = undefined,
            needSelected = false,
            multiSelected = false,
            _submitRef = undefined,
            
            evt_rowSelected = [],
            evt_rowDeselected = [],
            //evt_onPaging = [],
            evt_onButtonClick = [],
            evt_onTabClick = [],
            evt_onSubmit = [],
            evt_onDataField = [],
            evt_onItemClick = [],
            evt_onItemChange = [],
            evt_onBeforeSubmit = undefined,
            evt_onItemEditing = undefined,
            evt_onItemEditCancel = undefined,
            evt_onAllSelect = [],

            endvar;
        
        this.list           = ko.observableArray();
        this.detail         = ko.observable(null);
        this.paging         = ko.observableArray([1]);
        this.page           = ko.observable(1);
        this.count          = ko.observable(10);
        this.navCount       = ko.observable(10);
        this.totalcount     = ko.observable(0);
        this.selectedItem   = ko.observable();
        this.selectedTab    = ko.observable("");
        this.aleadySearch   = ko.observable(false);
        this.loading        = ko.observable(false);
        this.progress       = ko.observable(0);
        this.working        = ko.observable(false);
        this.workCurr       = ko.observable(0);
        this.workMax        = ko.observable(0);
        this.workErr        = ko.observable(0);
        this.tmp            = {};
        this.param          = undefined;
        this.form           = undefined;
        this.prop           = {};
        this.confirmMsg     = "";

        function init(){
            var jqForm = self.getForm()
            ,   sNeedSelected = jqForm.data("need-selected")
            ,   sMultiSelected = jqForm.data("multi-selected")
            ,   jqContext
            ;

            dataReqUrl = jqForm.attr("action");
            dataReqMethod = jqForm.attr("method");

            if (sNeedSelected && sNeedSelected === "yes"){
                needSelected = true;
            }
            if (sMultiSelected && sMultiSelected === "yes"){
                multiSelected = true;
            }

            jqContext = $( context );

            jqContext.keydown(function(e){
                if (e.keyCode === 17){
                    ctrlDown = true;
                } 
            }).keyup(function(e){
                if (e.keyCode === 17){
                    ctrlDown = false;
                }
            });
            
            jqContext.find(".spread").keydown(function(e){
                var sTmp = "",
                    mData = selectedData,
                    sVal = "",
                    key;
                
                if (ctrlDown && e.keyCode === 67){
                    if (window.clipboardData){
                        for(key in mData){
                            sVal = mData[key];
                            
                            if (sVal instanceof Function){
                                continue;
                            }
                            sTmp += sVal + "\t";
                        }
                        
                        //window.clipboardData.setData("Text", sTmp);
                    }
                    ctrlDown = false;
                }
            });



            self.form = jqForm;
        } // init [end]

        function initProp(prop, observs, observArrs){
            var i, iLen,
                key, val,
                mParam = util.getParam()
            ;

            mParam = mParam || {};

            if (!observs){
                return;
            }

            if ($.isArray( observs )){
                i = -1;
                iLen = observs.length;

                while(++i < iLen){
                    key = observs[i];

                    if (mParam.hasOwnProperty( key )){
                        val = mParam[ key ];
                    }
                    else{
                        val = undefined;
                    }
                    
                    prop[ key ] = ko.observable( val );
                }
            }
            else if ($.isPlainObject( observs )){
                for(key in observs){
                    if (mParam.hasOwnProperty( key )){
                        val = mParam[ key ];
                    }
                    else{
                        val = observs[ key ];
                    }
                    
                    prop[ key ] = ko.observable( val );
                }
            }

            

            if (!observArrs || !$.isArray(observArrs)){
                return;
            }

            i = -1;
            iLen = observArrs.length;

            while(++i < iLen){
                prop[ observArrs[i] ] = ko.observableArray([]);
            }
        }// initProp [end]

        function apply($, ko, viewModel){
            var elem = viewModel.getContext();

            ko.applyBindings( viewModel, elem );

            $( elem ).find("input.datepicker")
            .each(function(index){
                $(this).datepicker({
                    dateFormat: "yy-mm-dd"
                });
            });

            util.placeholders( elem );
        }

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


        this.addSelectedField = function(list){
            var i, 
                iLen = 0,
                isNeedSelected = needSelected || multiSelected
            ;

            if ($.isArray(list) === false){
                return list;
            }

            i = list.length;

            while(i--){
                if (isNeedSelected){
                    list[i].selected = ko.observable(false);
                }
                this._ondatafield( list[i], this );
            }
            
            return list;
        };
        this.resetSelected = function(){
            var aList = this.list(),
                i = 0,
                iLen = aList.length
                ;
            
            for(i = iLen; i--;){
                aList[i].selected(false);
            }
        };
        this.getSelected = function(){
            return selectedData;
        };
        this.setSelected = function(data){
            selectedData = data;
        };
        this.getContext = function(){
            var cont = context;

            if (cont instanceof jQuery){
                return cont[0]
            }

            return cont;
        };
        this.incErrCount = function(){
            self.workErr( self.workErr() + 1 );
        };
        this.clearErrCount = function(){
            self.workErr(0);
        };

        this.onRowSelect = function(item, e){
            var bSelected = false,
                jqRow;

            if (multiSelected || needSelected){
                jqRow = $(e.currentTarget);
                bSelected = item.selected();
            }

            if(multiSelected){
                if (!bSelected){
                    jqRow.addClass("selected");
                }
                else{
                    jqRow.removeClass("selected");
                }
            }
            else if (needSelected){
                jqRow.siblings().removeClass("selected");
                jqRow.addClass("selected");
                self.resetSelected();
            }

            if (multiSelected || needSelected){
                self.setSelected(item);
                item.selected(!bSelected);

                if (!bSelected){
                    self._onrowselected(item, e, self);
                    self.selectedItem(item);
                }
                else{
                    self._onrowdeselected(item, e, self);
                }

                return;
            }

            return true;
        };
        this.onrowselected = function(callback){
            evt_rowSelected.push(callback);
        };
        this._onrowselected = function(item, event, viewModel){
            var i, aFnEvt = evt_rowSelected,
                iLen = aFnEvt.length;
            
            try{
                for(i = 0; i < iLen; i++){
                    aFnEvt[i].call(this, event, item, viewModel);
                }
            }
            catch(e){}
        };
        this.onrowdeselected = function(callback){
            evt_rowDeselected.push(callback);
        };
        this._onrowdeselected = function(item, event, viewModel){
            var i, aFnEvt = evt_rowDeselected,
                iLen = aFnEvt.length;
            
            try{
                for(i = 0; i < iLen; i++){
                    aFnEvt[i].call(this, event, item, viewModel);
                }
            }
            catch(e){}
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

        this.onButtonClick = function(viewModel, e){
            self._onbuttonclick(e, viewModel);
        };
        this.onbuttonclick = function(callback){
            evt_onButtonClick.push(callback);
        };
        this._onbuttonclick = function(e, viewModel){
            var i, aFnEvt = evt_onButtonClick,
                iLen = aFnEvt.length;

                //console.log("_onbuttonclick", e);
                
            try{
                for(i = 0; i < iLen; i++){
                    aFnEvt[i].call(this, e, viewModel);
                }
            }
            catch(e){
                
            }
        };

        this.onTabClick = function(viewModel, e){
            self._ontabclick(e, viewModel);

            return false;
        };
        this.ontabclick = function(callback){
            evt_onTabClick.push(callback);
        };
        this._ontabclick = function(e, viewModel){
            var i, aFnEvt = evt_onTabClick,
                sTabId,
                sHref,
                jqTab,
                jqSiblings,
                iLen = aFnEvt.length;
                
            try{
                jqTab = $(e.currentTarget);
                jqTab.siblings().removeClass("selected");
                jqTab.addClass("selected");
                sHref = jqTab[0].href;
                sTabId = sHref.split("#")[1];
                viewModel.selectedTab( sTabId );

                for(i = 0; i < iLen; i++){
                    aFnEvt[i].call(this, sTabId, e, viewModel);
                }
            }
            catch(e){
                console.log(e);
            }
        };
        

        this.onSubmitUpload = function(e){
            var self = this;
            var jqForm = $(context).find("form");
            var jqFrame = $("iframe[name='" + jqForm.attr("target") + "']");
            var docFrame = jqFrame[0].contentDocument;
            var fnEvent = function(e, a, c){
                var docFrame = e.currentTarget.contentDocument
                ,   jqForm = $(context).find("form")
                ,   sTarget = jqForm.attr("target")
                ,   jqFrame = $("iframe[name='" + sTarget + "']")
                ,   docFrame = jqFrame[0].contentDocument
                ,   text = ""
                ,   res
                ;

                if (!docFrame){
                    // IE6 이하에서는 요렇게 해야 한다..
                    docFrame = document.frames[sTarget].document;
                }
                
                text = docFrame.body.innerHTML;
                res = $.parseJSON(text);
                ;

                self._submitCommon(res, "upload");

                jqFrame.off("load");

                self.loading(false);
            };
            var bBeforeRet = true;
            var sUrl = jqForm.attr("action");

            if (evt_onBeforeSubmit !== undefined){
                bBeforeRet = self._onbeforesubmit( sUrl, {} );
            }
            if (bBeforeRet === false){
                return;
            }

            jqFrame.on("load", fnEvent);

            self.loading(true);

            return true;
        };

        this.onSubmit = function(e){
            var elemCtx = context,
                jqForm = $(elemCtx).find("form"),
                mParam = util.serializeMap(jqForm),
                sUrl = dataReqUrl,
                sMethod = dataReqMethod,
                sMsg = jqForm.data("confirm"),
                endvar;

            if (!mParam.page){
                self.page(1);
            }

            console.log(this);
            console.log(e);

            self.confirmMsg = sMsg;

            self.submit( mParam, sUrl, sMethod, sMsg );

            return false;
        };
        
        this.submit = function(param, ref){
            var self = this,
                sUrl = dataReqUrl,
                sMethod = dataReqMethod,
                page = self.page(),
                count = self.count(),
                mParam = self.param || {},
                confirmMsg = self.confirmMsg,
                jqForm,
                bBeforeRet = true,
                endvar
            ;

            //mParam.page = page;
            //mParam.count = count;

            if ((self.working() === false) && self.loading()){
                return;
            }

            if (confirmMsg){
                confirmMsg = confirmMsg.replace(/\\n/g, "\n");
                if (confirm( confirmMsg ) === false){
                    return;
                }
            }

            if (param === undefined){
                mParam = self.getParamFromForm();
            }

            mParam.page = page;
            mParam.count = count;

            if (param){
                mParam = $.extend(mParam, param);
                self.param = param;
            }

            if (evt_onBeforeSubmit !== undefined){
                bBeforeRet = self._onbeforesubmit( sUrl, mParam );
            }
            if (bBeforeRet === false){
                return;
            }

            if (mParam.count){
                self.count( parseInt(mParam.count) );
            }
            if (mParam.navCount){
                self.navCount( parseInt(mParam.navCount) );
            }
            
            lastActionUrl = sUrl;
            lastParam = mParam;

            self.aleadySearch(true);
            self.loading(true);
            _submitRef = ref;
            
            if (sMethod && sMethod.toLowerCase() === "post"){
                self._submitPost(sUrl, mParam);
            }
            else{
                self._submitGet(sUrl, mParam);
            }
        };

        this._submitGet = function(sUrl, mParam){
            mParam.__time = (new Date()).getTime();
            $.getJSON( sUrl, mParam, self._submitCommon )
            .fail(function(jqXhr, textStatus, errorThrown){
                alert( "데이터 통신 중 오류가 발생 했습니다.\n" + errorThrown );
                self.incErrCount();
            })
            .always(function(data){
                self.loading(false);
            });
        };

        this._submitPost = function(sUrl, mParam){
            $.post( sUrl, mParam, function(rawData){
                var data;

                try{
                    self._submitCommon( $.parseJSON( rawData ) );
                }
                catch(ex){
                    alert( "전달 받은 데이터를 파싱할 수 없습니다." );
                }
            })
            .fail(function(jqXhr, textStatus, errorThrown){
                alert( "데이터 통신 중 오류가 발생 했습니다.\n" + errorThrown );
                self.incErrCount();
            })
            .always(function(data){
                self.loading(false);
            });
        };

        this._submitCommon = function(data, type){
            var sMsg = ""
            ,   i = 0
            ,   iErrLen = 0
            ,   aErr
            ,   sType = type || "list"
            ;

            try{
                if ($.isArray( data.result.err ) ){
                    iErrLen = data.result.err.length;
                }
                
                if (data.result.auth === false){
                    alert("세션이 만료 되었거나 권한이 부족합니다.");

                    if (data.result.redirect === "login"){
                        location.href = "/index.jsp";
                    }
                    else{
                        location.href = data.result.redirect;
                    }
                    
                    return;
                }

                if (data.result.valid === false){
                    if (data.result.msg){
                        alert( data.result.msg );
                    }

                    return;
                }

                if (iErrLen === 0){
                    if (sType !== "upload"){
                        if ( $.isArray(data.data) ){
                            self.list( self.addSelectedField( data.data ) );
                        }
                        else{
                            self.detail( data.data );
                        }

                        self.totalcount( data.count || 0 );
                        self.makePaging();
                    }
                }
                else{
                    i = iErrLen;
                    aErr = data.result.err;
                    sMsg = "[오류가 발생 했습니다]";

                    while(i--){
                        sMsg += "\n" + aErr[i];
                    }
                    self.incErrCount();
                }

                self._onsubmit.call(self, data, lastActionUrl, lastParam);
            }
            catch(ex){
                sMsg = "서버에서 잘못된 데이터가 전송 되었습니다.";

                if (ex.description){
                    sMsg += "\n" + ex.description; 
                }
                else{
                    sMsg += ex;
                }
                console.log(ex);
                self.incErrCount();
            }

            if (sMsg){
                alert(sMsg);
            }
        };
        
        this.load = function(param){
            var mParam = param;

            self.clearParam();
            self.page(1);

            if ((mParam === undefined) || 
                ($.isEmptyObject(mParam) === true) ){
                mParam = self.getParamFromForm();
            }

            self.submit(mParam);
        };
        
        this.clearParam = function(){
            self.param = undefined;
        };

        this.getForm = function(){
            var jqForm = this.form;

            if (!jqForm || jqForm.length < 1){
                this.form = $(context).find("form");
            }

            return this.form;
        };

        this.getParamFromForm = function(){
            return util.serializeMap( this.form );
        }
       

        this.onbeforesubmit = function(callback){
            evt_onBeforeSubmit = callback;
        };
        this._onbeforesubmit = function(url, param){
            var bRet = true;

            try{
                bRet = evt_onBeforeSubmit( url, param );

                if (bRet === undefined){
                    bRet = true;
                }

                return bRet;
            }
            catch(e){
                return false;
            }
        }

        this.onsubmit = function(callback){
            evt_onSubmit.push(callback);
        };
        this._onsubmit = function(data, url, param){
            var i, aFnEvt = evt_onSubmit,
                iLen = aFnEvt.length,
                viewModel = self,
                ref = _submitRef
                ;
            
            if ( $.isPlainObject(data) === false ){
                data = undefined;
            }

            try{
                for(i = 0; i < iLen; i++){
                    aFnEvt[i].call(this, {
                        data: data,
                        url: url,
                        param: param,
                        ref: ref
                    }, viewModel);
                }
            }
            catch(e){}
        };

        this.onItemClick = function(item, e){
            self._onitemclick(e, item, self);

            return false;
        };
        this.onitemclick = function(callback){
            evt_onItemClick.push(callback);
        };
        this._onitemclick = function(e, item, viewModel){
            var i, aFnEvt = evt_onItemClick,
                iLen = aFnEvt.length;
            
            try{
                for(i = 0; i < iLen; i++){
                    aFnEvt[i].call(this, e, item, viewModel);
                }
            }
            catch(e){}
        };


        this.onItemChange = function(item, e){
            self._onitemchange(item, self, e);
        };
        this.onitemchange = function(callback){
            evt_onItemChange.push(callback);
        };
        this._onitemchange = function(item, viewModel, e){
            var i=-1, aFnEvt = evt_onItemChange
            ,   iLen = aFnEvt.length
            ;

            try{
                while(++i < iLen){
                    aFnEvt[i].call(viewModel, item, viewModel, e);
                }
            }
            catch(e){}
        };

        
        this.onItemEditing = function(item, e){
            var code = e.keyCode || e.which
            ;

            if (code === 13){
                item[ e.currentTarget.name ]( e.currentTarget.value );

                evt_onItemEditing.call(self, e, item, self);
                

                return false;
            }

            if (code === 27){
                evt_onItemEditCancel.call(self, e, item, self );
            }

            //item[ e.currentTarget.name ]( e.currentTarget.value + String.fromCharCode(code) );

            return true;
        };
        this.onitemediting = function(callback){
            evt_onItemEditing = callback;
        };


        // this.onItemEditCancel = function(item, event){
        //     try{
        //         evt_onItemEditCancel.call(self, event, item, self);
        //     }
        //     catch(e){
        //         console.log(e);
        //     }
        // };
        this.onitemeditcancel = function(handler){
            evt_onItemEditCancel = handler;
        };

        this.onAllSelect = function(item, e){
            var i = -1, aFnEvt = evt_onAllSelect, iLen = aFnEvt.length;

            try{
                while(++i < iLen){
                    aFnEvt[i](e, item, self);
                }
            }
            catch(e){
                console.log("onAllSelect");
                console.log(e);
            }
        };
        this.onallselect = function(handler){
            evt_onAllSelect.push( handler );
        };

        

        this.ondatafield = function(callback){
            evt_onDataField.push(callback);
        };
        this._ondatafield = function(item, viewModel){
            var i, aFnEvt = evt_onDataField,
                iLen = aFnEvt.length;
                
            try{
                for(i = 0; i < iLen; i++){
                    aFnEvt[i](item, viewModel);
                }
            }
            catch(e){
                console.log(e);
            }
        };

        
        init();
        initProp(this.prop, observs, observArrs);
        try{
            fnEtc.call(this);
        }
        catch(e){
            //console.log(e);
        }
        apply($, ko, this);
    };// ViewModel[end]
    // 뷰모델 구성 부분 [종료]

    return ViewModel;
})($, ko);
/*
Event
ondatafield( handler )
- handler : Function(item, viewModel)

onsubmit( handler )
- handler : Function(response, viewModel)
-- response : {data, url, param, ref}
--- ref : submit 수행 시 두번째로 넣은 변수.

ontabclick( handler )
- handler : Function(tabId, event, viewModel)
-- tabId : 하이퍼링크의 # 뒤의 이름. 없으면 undefined.
-- event : 탭버튼 클릭된 요소에서 발생된 이벤트 객체.

onbuttonclick( handler )
- handler : Function(event, viewModel)

onitemclick( handler )
- handler : Function(event, item, viewModel)

onrowselected( handler )
- handler : Function(event, item, viewModel)

*/

// execution sample
// $(function(){
    
//     var vmFileList = new ViewModel( $("#panel_EB11_fileList")[0], "/billing/invoice/cms/eb11_filelist.jsp", 1 );
//     var vmFileDataList = new ViewModel( $("#panel_EB11_fileDataList")[0], "/billing/invoice/cms/eb11_filedatalist.jsp", 2, 2 );
    
//     vmFileList.onrowselected(function(e){
//     	vmFileDataList.load({
//     		id_eb11: e.data.id_eb11
//     	});
//     });
    
//     for(var i = 10; i--;){
//         viewModel.list.push({
//             //num: 40 - i,
//             wdate: "2016-03-04",
//             status: 2,
//             filename: "EB140304",
//             sdate: "2016-03-03 12:10:20",
//             edate: "2016-03-04 06:10:20",
//             process: 2,
//             comment: "특이사항 없음",
            
//             result_total_count: 20,
//             result_normal_count: 17,
//             result_error_count: Math.floor((Math.random() * 10) + 1),
            
//             selected: ko.observable(false)
//         });
//     }
    
//     ko.applyBindings( vmFileList, vmFileList.getContext() );
//     ko.applyBindings( vmFileDataList, vmFileDataList.getContext() );
    
//     vmFileList.load();
// });