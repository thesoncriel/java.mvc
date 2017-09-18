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

(function(){
    if (!window.console){
        window.console = {
            log: function(){},
            dir: function(){}
        };
    }
})();


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
        getVal: function(arg){
            if ($.isFunction( arg )){
                return arg();
            }

            return arg;
        },
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

            dataReqUrl,
            dataReqMethod = "",
            
            lastActionUrl = undefined,
            lastParam = undefined,

            _submitRef = undefined,
            
            
            

            endvar;
        
        this.list           = ko.observableArray();
        
        this.page           = ko.observable(1);
        this.count          = ko.observable(10);

        this.totalcount     = ko.observable(0);

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

        this.callback = new ViewModelEvent(this);

        function init(){
            var jqForm = self.getForm()
            ,   sNeedSelected = jqForm.data("need-selected")
            ,   sMultiSelected = jqForm.data("multi-selected")
            ,   jqContext
            ,   sNavCount
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

        
        this.bind = function(name, handler){
            var sName = "on" + name;

            if (this.callback.hasOwnProperty(sName)){
                this.callback[sName]( handler, this );
            }
        };
        this.trigger = function(name){
            var sName = "_on" + name
            ,   i = 0
            ,   iLen = arguments.length
            ,   args = []
            ;

            if (this.callback.hasOwnProperty(sName) === false){
                return;
            }

            while(++i < iLen){
                args.push( arguments[i] );
            }

            this.callback[sName].apply(this, args);
        };
        

        this.onSubmitUpload = function(e){
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

                jqFrame.unbind();

                self.loading(false);
            };

            jqFrame.bind("load", fnEvent);

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
            var sUrl = dataReqUrl,
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

            bBeforeRet = self.trigger("beforesubmit", sUrl, mParam );

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
                    if (sType === "success" || sType === "list"){
                        self.trigger("success", data);
                        self.list( data.data );
                        self.totalcount( data.count );
                        //self.makePaging();
                    }
                    else if (sType === "upload"){

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

                self.trigger("submit", data, lastActionUrl, lastParam, _submitRef);
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
       

        
        
        function disableAllEditing(vm, item){
            var list = vm.list()
            ,   i = -1
            ,   iLen = list.length
            ,   mRemovable
            ;

            while(++i < iLen){
                if (list[i] !== item){
                    list[i].editing(false);
                }
                if (list[i].id_group === 0){
                    mRemovable = list[i];
                }
            }

            vm.list.remove( mRemovable );
        };

        // TODO: 미완성
        this.applyEditing = function(pkField, field){
            // 자료를 ajax로 받은 직후 각 필드의 변경이 필요할 때 사용한다.
            this.ondatafield(function(item, vm){
                item.editing = ko.observable( false );
                item[field] = ko.observable( item[field] );
            });
            // 각 행을 클릭 했을 때 발생되는 이벤트.
            // 여기선 행을 더블 클릭 했을 때 에디터 모드로 변경하기 위해 사용함.
            this.onitemclick(function(e, item, vm){
                
                disableAllEditing(vm, item);

                item.editing(true);
                vm.editingItem = item;
                $(e.currentTarget).next().focus().select();

                e.currentTarget.stopPropagation();
            });
            // 사용자가 입력 중 esc를 눌렀을 때 수행된다.
            // 에지터 모드를 되돌릴 때 사용.
            this.onitemeditcancel(function(e, item, vm){
                var pkFld = item[ pkField ];
                var pkVal = $.isFunction( pkFld ) ? pkFld() : pkFld;

                item.editing(false);

                if (item.id_group <= 0){
                    vm.list.remove( item );
                }
            });
            // 각 행이 선택 되었을 때 발생되는 이벤트.
            // 에디터 모드인 행이 있을 때 이를 다시 되돌리기 위한 목적으로 사용.
            this.onrowselected(function(e, item, vm){
                if (item.editing() === true){

                    return false;
                }

                disableAllEditing(vm);
                vm.prop.id_group( parseInt( item.id_group ) );
                markApplyAll();
            });
            // 추가 버튼을 눌렀을 때 수행된다.
            this.onbuttonclick(function(e, vm){
                var list = vm.list()
                ,   iLen = list.length
                ,   item
                ,   newItem
                ;

                if (iLen > 0){
                    item = list[iLen - 1];

                    if (item.id_group === 0 && item.editing() === true){
                        return false;
                    }

                    disableAllEditing(vm, item);
                }

                newItem = {
                    id_group: 0,
                    group_name: ko.observable(""),
                    editing: ko.observable(true),
                    selected: ko.observable(false)
                };

                vm.list.push( newItem );
                vm.editingItem = newItem;

                setTimeout(function(){
                    $(vm.getContext()).find("input:visible").focus();
                },100);
                // vmContgrpModify.submit({
                //  id_group: 
                // });
            });
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
