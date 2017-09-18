// 그리드 부분 추가
(function($, ko, undefined){
ko.components.register("grid", {
    viewModel: function(params){
        var self = this
        ,   mCss = params.css || {}
        ,   selectedIndex = -1
        ,   selectedData
        ,   needSelected = params.select === true
        ,   multiSelected = params.multi === true
        ;

        this.list = params.list;
        this.aleadySearch = params.aleadySearch;
        this.loading = params.loading || ko.observable(false);
        this.addButton = params.addButton || ko.observable(false);
        this.callback = params.callback || {};

        this.header = ko.observableArray([]);
        this.keys = ko.observableArray([]);
        this.colcount = ko.pureComputed(function(){
            try{
                return this.header().length;
            }
            catch(e){
                return 0;
            }
            
        }, this);

        this.selectedItem   = ko.observable();
        
        if (this.callback){
            this.callback.onsuccess(function(res){
                res.data = self.addSelectedField(res.data);
            });
        }
        
        

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
                this.callback._ondatafield( list[i], this );
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
                    self.callback._onrowselected(item, e, self);
                    self.selectedItem(item);
                }
                else{
                    self.callback._onrowdeselected(item, e, self);
                }

                return;
            }

            return true;
        };
        
        
        this.onItemDelete = function(item, e){
            e.stopPropagation();

            console.log(item);
            console.log(e);

            if (self.callback._onitemdelete(item, e, self)){
                self.list.remove(item);
            }
        };
        this.onItemEdit = function(item, e){
            e.stopPropagation();
            self.callback._onitemedit(item, e, self);
        };
        this.onItemModify = function(item, e){
            e.stopPropagation();
            self.callback._onitemmodify(item, e, self);
        };
        this.onItemAllow = function(item, e){
            e.stopPropagation();
            self.callback._onitemallow(item, e, self);
        };
        this.onItemAdd = function(item, e){
            var mItem = {
                __new: true,
                editing: ko.observable(true)
            };

            e.stopPropagation();

            self.callback._onitemadd(mItem, e, self);

            console.log(mItem);

            self.list.push(mItem);

            setTimeout(function(){
                $("input.direct-editable:visible").focus();
            },100);
        };
        this.onItemEditing = function(item, e){
            var code = e.keyCode || e.which
            ,   name = e.currentTarget.name
            ,   value = e.currentTarget.value
            ;
            // console.log("onItemEditing");
            // console.log(item);

            if (code === 13){
                if (!value){
                    alert("값이 비었습니다.");

                    return false;
                }

                item[ name ]( value );

                delete item.__new;

                self.callback._onitemedited(item, e, self);

                return false;
            }

            if (code === 27){
                if ((!item[ name ] || !item[ name ]()) && value === "" && item.__new){
                    self.list.remove(item);
                }
                else{
                    item.editing(false);
                }
                //evt_onItemEditCancel.call(self, item, e, self );

                return false;
                
            }

            //item[ e.currentTarget.name ]( e.currentTarget.value + String.fromCharCode(code) );

            return true;
        };







        function initField(field){
            var key = ""
            ,   aHeader = self.header
            ,   aKeys = self.keys
            ;

            for(key in field){
                aHeader.push( field[key] );
                aKeys.push( key );
            }
        }

        this.css = function(key){
            return mCss[key] || "";
        };

        initField(params.field);
    },
    template: 
    '<div class="spread spread-line6">\
        <table>\
            <thead>\
                <tr data-bind="foreach: header">\
                    <th data-bind="text: $data"></th>\
                </tr>\
            </thead>\
            <tbody data-bind="foreach: {data: list, as: \'item\'}">\
                <tr data-bind="click: $parent.onRowSelect">\
                    <!-- ko foreach: {data: $parent.keys, as : \'key\'} -->\
                    <!-- ko if: key == \'_del\' -->\
                    <td><button class="btn btn-w40" data-bind="click: function(data, event){ $parents[1].onItemDelete(item, event); }">삭제</button></td>\
                    <!-- /ko -->\
                    <!-- ko if: key == \'_mod\' -->\
                    <td><button class="btn btn-w40" data-bind="click: function(data, event){ $parents[1].onItemModify(item, event); }">수정</button></td>\
                    <!-- /ko -->\
                    <!-- ko if: key == \'_allow\' -->\
                    <td><button class="btn btn-w40" data-bind="click: function(data, event){ $parents[1].onItemAllow(item, event); }">허용</button></td>\
                    <!-- /ko -->\
                    <!-- ko ifnot: (key == \'_del\' || key == \'_mod\' || key == \'_allow\') -->\
                    <td data-bind="attr: {\'class\': $parents[1].css(key)}">\
                        <!-- ko if: item.editing -->\
                        <input class="direct-editable" type="text" data-bind="value: item[key], attr:{name: key}, event: {keyup: function(data, event){ return $parents[1].onItemEditing(item, event); } }">\
                        <!-- /ko -->\
                        <!-- ko ifnot: item.editing -->\
                        <span data-bind="text: item[key]"></span>\
                        <!-- /ko -->\
                    </td>\
                    <!-- /ko -->\
                    <!-- /ko -->\
                </tr>\
            </tbody>\
            <tbody data-bind="visible: !loading() && list().length == 0">\
                <tr>\
                    <td class="text-center" data-bind="attr: {\'colspan\': colcount, \'colSpan\': colcount}">\
                    <!-- ko if: aleadySearch -->\
                    자료가 존재하지 않습니다.<!-- /ko -->\
                    <!-- ko ifnot: aleadySearch -->\
                    자료를 \'조회\' 하십시오. <!-- /ko -->\
                    </td>\
                </tr>\
            </tbody>\
            <tbody data-bind="visible: addButton">\
                <tr>\
                    <td data-bind="attr: {\'colspan\': colcount, \'colSpan\': colcount}"><a href="#" class="btn block" data-bind="click: onItemAdd">&#10133; 추가</a></td>\
                </tr>\
            </tbody>\
            <tbody data-bind="visible: loading">\
                <tr>\
                    <td class="text-center" data-bind="attr: {\'colspan\': colcount, \'colSpan\': colcount}">Now Loading ...</td>\
                </tr>\
            </tbody>\
        </table>\
    </div>'
});
})(jQuery, ko);