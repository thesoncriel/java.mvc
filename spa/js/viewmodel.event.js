var ViewModelEvent = (function($, ko, undefined){
"use strict";
    return function(viewModel){
        var self = this
        ,   evt_onButtonClick = []
        ,   evt_onTabClick = []
        ,   evt_onSubmit = []
        ,   evt_onDataField = []
        ,   evt_onBeforeSubmit = undefined
        ,   evt_onItemEdited = []
        ,   evt_onItemEditCancel = []
        ,   evt_onSuccess = []

        ,   evt_rowSelected = []
        ,   evt_rowDeselected = []
        ,   evt_onItemDelete = []
        ,   evt_onItemAdd = []
        ,   evt_onItemAllow = []

        ,   evt_onItemClick = []
        ,   evt_onItemChange = []
        ,   evt_onAllSelect = []

        ,   evt_onProgress = []
        ;

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
        };

        this.onsubmit = function(callback){
            evt_onSubmit.push(callback);
        };
        this._onsubmit = function(data, url, param, ref){
            var i, aFnEvt = evt_onSubmit,
                iLen = aFnEvt.length,
                viewModel = self
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



        this.onsuccess = function(callback){
            evt_onSuccess.push(callback);
        };
        this._onsuccess = function(item, viewModel){
            var i, aFnEvt = evt_onSuccess,
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

        // 프로그레스바 전용[시작]

        this.onprogress = function(callback){
            evt_onProgress.push(callback);
        };
        this._onprogress = function(item, viewModel){
            var i, aFnEvt = evt_onProgress,
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
        // 프로그레스바 전용[종료]






        
        this.onrowselected = function(callback){
            evt_rowSelected.push(callback);
        };
        this._onrowselected = function(item, event, viewModel){
            var i, aFnEvt = evt_rowSelected,
                iLen = aFnEvt.length;
            
            try{
                for(i = 0; i < iLen; i++){
                    aFnEvt[i].call(this, item, event, viewModel);
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
                    aFnEvt[i].call(viewModel, item, event, viewModel);
                }
            }
            catch(e){}
        };



        this.onitemdelete = function(callback){
            evt_onItemDelete.push(callback);
        };
        this._onitemdelete = function(item, event, viewModel){
            var i, aFnEvt = evt_onItemDelete
            ,   iLen = aFnEvt.length
            ;
            
            try{
                for(i = 0; i < iLen; i++){
                    if (aFnEvt[i].call(viewModel, item, event, viewModel) === false){
                        return false;
                    }
                }
            }
            catch(e){}

            return true;
        };

        this.onitemadd = function(callback){
            evt_onItemAdd.push(callback);
        };
        this._onitemadd = function(item, event, viewModel){
            var i, aFnEvt = evt_onItemAdd
            ,   iLen = aFnEvt.length
            ;
            
            try{
                for(i = 0; i < iLen; i++){
                    // console.log("_onitemadd");
                    // console.log(item);
                    if (aFnEvt[i].call(viewModel, item, event, viewModel) === false){
                        return false;
                    }
                }
            }
            catch(e){}

            return true;
        };

        //evt_onItemAllow
        this.onItemAllow = function(item, e){
            this._onitemallow(e, item, self);

            return false;
        };
        this.onitemallow = function(callback){
            evt_onItemAllow.push(callback);
        };
        this._onitemallow = function(item, event, viewModel){
            var i, aFnEvt = evt_onItemAllow
            ,   iLen = aFnEvt.length
            ;
            
            try{
                for(i = 0; i < iLen; i++){
                    // console.log("_onitemadd");
                    // console.log(item);
                    if (aFnEvt[i].call(viewModel, item, event, viewModel) === false){
                        return false;
                    }
                }
            }
            catch(e){}

            return true;
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

        this.onitemedited = function(callback){
            evt_onItemEdited.push( callback );
        };
        this._onitemedited = function(item, event, viewModel){
            var i, aFnEvt = evt_onItemEdited
            ,   iLen = aFnEvt.length
            ;
            
            try{
                for(i = 0; i < iLen; i++){
                    if (aFnEvt[i].call(viewModel, item, event, viewModel) === false){
                        return false;
                    }
                }
            }
            catch(e){}

            return true;
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
    }
})(jQuery, ko);