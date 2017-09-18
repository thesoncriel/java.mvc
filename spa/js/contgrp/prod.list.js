$(function(){
	"use strict";

	var vmProdList = new ViewModel( "#panel_prodList", {id_prod: "", id_group_info: 0} );
	var vmProdApply = new ViewModel( "#panel_prodApply", {id_prod: "", id_group: 0} );
	var vmContgrpList = new ViewModel( "#panel_contgrpList" );


	function getEnabledGroupCount( id_group_info ){
		var idGroupInfo = parseInt( id_group_info )
		,	i = -1
		,	iMask = 1
		,	iLen = 30
		,	iRet = 0
		;

		while(++i < iLen){
			if (idGroupInfo & (1 << i)){
				iRet++;
			}
		}

		return iRet;
	}
	// 그룹 목록에 ID_GROUP_INFO 의 적용된 것을 확인하고 마크(mark) 한다.
	function markApply(item, selected){
		var idGroupInfo = vmProdList.prop.id_group_info();

		if (selected !== undefined){
			item.selected( selected );

			return;
		}

		if ( (idGroupInfo & item.id_group) > 0 ){
			item.selected(true);
		}
		else{
			item.selected(false);
		}
	}
	// 모든 그룹 목록을 대상으로 ID_GROUP_INFO 의 적용 여부를 마크(mark) 한다.
	function markApplyAll(selected){
		var list = vmContgrpList.list()
		,	i = -1
		,	iLen = list.length
		;

		while(++i < iLen){
			markApply( list[i], selected );
		}
	}

	vmProdList.ondatafield(function(item, vm){
		var idGroupInfo = parseInt( item.id_group_info );
		var iCnt = getEnabledGroupCount( idGroupInfo );

		item.id_group_info = idGroupInfo;
		item.group_count = ko.observable( iCnt );
	});

	vmProdList.onrowselected(function(e, item, vm){
		vmProdList.prop.id_group_info( item.id_group_info );
		vmProdList.prop.id_prod( item.id_prod );

		markApplyAll();
	});

	vmProdList.onsubmit(function(res){
		try{
			console.log(res);

			if(res.data.result.valid){
				markApplyAll(false);
			}
		}
		catch(e){
			console.log(e);
		}
	});

	


	function prodApply(vmProdApply, item, isApply){
		vmContgrpList.loading( true );

		vmProdApply.submit({
			id_prod: vmProdList.prop.id_prod(),
			id_group: item.id_group,
			apply: isApply ? "yes" : "no"
		});

		vmProdApply.usedApply = isApply;
		vmProdApply.usedIdGroup = item.id_group;
	}



	vmContgrpList.ondatafield(function(item, vm){
		item.id_group = parseInt( item.id_group );
	});

	vmContgrpList.onrowselected(function(e, item, vm){
		prodApply( vmProdApply, item, true );
	});
	vmContgrpList.onrowdeselected(function(e, item, vm){
		prodApply( vmProdApply, item, false );
	});

	vmProdApply.onsubmit(function(res){
		var iCnt = 0
		,	selectedItem
		,	idGroupInfo
		,	idGroup = vmProdApply.usedIdGroup
		;

		try{
			if (res.data.result.valid){
				selectedItem = vmProdList.selectedItem();
				idGroupInfo = selectedItem.id_group_info;
				iCnt = selectedItem.group_count();

				// console.log(selectedItem);
				// console.log(idGroupInfo);
				// console.log(iCnt);

				if (vmProdApply.usedApply){
					iCnt++;
					selectedItem.id_group_info = idGroupInfo | idGroup;
				}
				else if (iCnt > 0){
					iCnt--;
					selectedItem.id_group_info = idGroupInfo ^ idGroup;
				}
				else{
					// console.log("왜? -_-");
					return;
				}
				
				selectedItem.group_count( iCnt );
				
			}
		}
		catch(e){
			//console.log(e);
		}

		vmContgrpList.loading( false );
	})

	vmProdList.load();
	vmContgrpList.load();
});