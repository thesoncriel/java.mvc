$(function(){
	"use strict";

	var vmContgrpList = new ViewModel( "#panel_contgrpList", {id_series: "", id_contents: "", id_group: 0}, {} );
	var vmContgrpModify = new ViewModel( "#panel_contgrpModify", {id_group: "", group_name: ""}, {} );
	var vmContList = new ViewModel( "#panel_contList" );
	var vmContApply = new ViewModel( "#panel_contApply", {id_contents: "", id_group: ""} );
	var vmContApplyAll = new ViewModel( "#panel_contApplyAll", {apply: false} );

	function disableAllEditing(vm, item){
		var list = vm.list()
		,	i = -1
		,	iLen = list.length
		,	mRemovable
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
	}
	// 자료를 ajax로 받은 직후 각 필드의 변경이 필요할 때 사용한다.
	vmContgrpList.ondatafield(function(item, vm){
		//item.id_group = ko.observable( item.id_group );
		item.editing = ko.observable( false );
		item.group_name = ko.observable( item.group_name );
	});
	// 각 행을 클릭 했을 때 발생되는 이벤트.
	// 여기선 행을 더블 클릭 했을 때 에디터 모드로 변경하기 위해 사용함.
	vmContgrpList.onitemclick(function(e, item, vm){
		
		disableAllEditing(vm, item);

		item.editing(true);
		vmContgrpList.editingItem = item;
		$(e.currentTarget).next().focus().select();

		e.currentTarget.stopPropagation();
		console.log("onitemclick");
	});
	// 사용자가 입력 후 엔터를 쳐서 입력이 종료 되었다 판단되면 수행된다.
	vmContgrpList.onitemediting(function(e, item, vm){
		item.editing(false);

		vmContgrpList.loading(true);

		vmContgrpModify.submit({
			id_group: item.id_group,
			group_name: item.group_name()
		});
	});
	// 사용자가 입력 중 esc를 눌렀을 때 수행된다.
	// 에지터 모드를 되돌릴 때 사용.
	vmContgrpList.onitemeditcancel(function(e, item, vm){
		item.editing(false);

		if (item.id_group <= 0){
			vmContgrpList.list.remove( item );
		}
	});
	// 각 행이 선택 되었을 때 발생되는 이벤트.
	// 에디터 모드인 행이 있을 때 이를 다시 되돌리기 위한 목적으로 사용.
	vmContgrpList.onrowselected(function(e, item, vm){
		if (item.editing() === true){

			return false;
		}

		disableAllEditing(vm);
		vm.prop.id_group( parseInt( item.id_group ) );
		markApplyAll();
	});
	// 추가 버튼을 눌렀을 때 수행된다.
	vmContgrpList.onbuttonclick(function(e, vm){
		var list = vmContgrpList.list()
		,	iLen = list.length
		,	item
		,	newItem
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

		vmContgrpList.list.push( newItem );
		vmContgrpList.editingItem = newItem;

		setTimeout(function(){
			$(vmContgrpList.getContext()).find("input:visible").focus();
		},100);
		// vmContgrpModify.submit({
		// 	id_group: 
		// });
	});

	// vmContgrpList.onsubmit(function(res){
		
	// });

	// 수정/추가 변경에 성공 했을 때 수행된다.
	vmContgrpModify.onsubmit(function(res){
		var mInfo = res.data.info;

		console.log(res);

		if (mInfo.success && vmContgrpList.editingItem){
			vmContgrpList.editingItem.id_group = parseInt(mInfo.id_group);
		}

		vmContgrpList.loading(false);
	});




	


	// 컨텐츠 목록에 ID_GROUP 적용된 것을 확인하고 마크(mark) 한다.
	function markApply(item){
		var iIdGroup = vmContgrpList.prop.id_group();

		if ( (iIdGroup & item.id_group_info()) > 0 ){
			item.selected(true);
		}
		else{
			item.selected(false);
		}
	}
	// 모든 컨텐츠 목록을 대상으로 ID_GROUP 적용 여부를 마크(mark) 한다.
	function markApplyAll(){
		var list = vmContList.list()
		,	i = -1
		,	iLen = list.length
		;

		while(++i < iLen){
			markApply( list[i] );
		}
	}
	// 특정 컨텐츠를 ID_GROUP에 적용/해제 한다.
	function contApply(vmContApply, item, isApply){
		var id_group = vmContgrpList.prop.id_group();

		vmContApply.submit({
			id_contents: item.id_contents,
			id_group: id_group,
			apply: isApply? "yes" : "no"
		});

		vmContApply.usedItem = item;
		vmContApply.usedApply = isApply;
	}
	// 포함된 컨텐츠 그룹을 모두 출력 한다.
	function getContentsGroupNames(id_group_info, report){
		var idGroupInfo = parseInt( id_group_info )
		,	list = vmContgrpList.list()
		,	i = -1
		,	iLen = list.length
		,	aNames = []
		;

		while(++i < iLen){
			if (idGroupInfo & parseInt( list[i].id_group )){
				aNames.push( list[i].group_name() );
			}
		}

		if (!report){
			return aNames;
		}

		if (aNames.length === 0){
			return "==적용된 그룹==\n없음.";
		}

		return "==적용된 그룹==\n" + aNames.join("\n");
	}

	function getAllItems(idGroup, isApply){
		var	list = vmContList.list()
		,	item
		,	aRet = []
		,	mItem
		,	idGroupInfo = 0
		,	sApply = (isApply === true) ? "yes" : "no"
		,	i = -1
		,	iLen = list.length
		;

		console.log(list);

		while(++i < iLen){

			item = list[i];
			item.selected( isApply );
			idGroupInfo = item.id_group_info();

			if (isApply){
				item.id_group_info( idGroupInfo | idGroup );
			}
			else{
				item.id_group_info( idGroupInfo ^ idGroup );
			}

			mItem = {
				id_group: idGroup,
				id_contents: item.id_contents,
				apply: sApply
			};
			aRet.push(mItem);
		}

		return aRet;
	}

	vmContList.ondatafield(function(item, vm){
		item.id_group_info = ko.observable( parseInt( item.id_group_info ) );

		markApply(item);
	});

	


	vmContList.onrowselected(function(e, item, vm){
		if (vmContgrpList.prop.id_group() > 0){
			contApply(vmContApply, item, true);
		}
	});
	vmContList.onrowdeselected(function(e, item, vm){
		if (vmContgrpList.prop.id_group() > 0){
			contApply(vmContApply, item, false);
		}
	});
	vmContApply.onsubmit(function(res){
		var id_group_info = vmContApply.usedItem.id_group_info();
		var id_group = vmContgrpList.prop.id_group();

		if (res.data.result.valid){
			if (vmContApply.usedApply){
				vmContApply.usedItem.id_group_info( id_group_info | id_group );
			}
			else{
				vmContApply.usedItem.id_group_info( id_group_info ^ id_group );
			}
		}
		
	});

	vmContList.onbuttonclick(function(e, item){
		var evt = e || window.event;

		// 이벤트 전파 방지
		evt.stopPropagation();

		alert( getContentsGroupNames( item.id_group_info(), true ) );
	});

	vmContList.onallselect(function(e, item){
		var jqBtn = $(e.currentTarget)
		,	sType = jqBtn.data("type")
		,	bApply = sType === "select"
		,	idGroup = vmContgrpList.prop.id_group()
		,	mReq
		,	sJson
		;

		if (idGroup <= 0){
			alert("그룹을 선택해 주세요.");

			return false;
		}

		mReq = {
			reqList: getAllItems( idGroup, bApply )
		};
		console.log("한번에 보내기");
		console.log(mReq);

		if (window.JSON){
            sJson = JSON.stringify(mReq);
        }
        else{
            sJson = $.stringify(mReq);
        }

		vmContApplyAll.prop.apply( bApply );
		vmContApplyAll.submit({json: sJson});
		vmContList.working(true);
	});

	vmContApplyAll.onsubmit(function(res){
		vmContList.working(false);
	});

	vmContgrpList.load();
	vmContList.load();
});