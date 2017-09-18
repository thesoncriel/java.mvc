$(function(){
"use strict";

var iTimerFlag = 0;
var sDate = Util.dateFormat( Util.getPrevMonthFirstDate() );
var vmCustErpUpload = new ViewModel("#panel_custErpUpload" );
var vmCustErpUploadProgress = new ViewModel("#panel_custErpUploadProgress", {errorText: "", showError: false}, ["error"]);
var vmCustDiffCheck = new ViewModel("#panel_custDiffCheck", {yyyymm_use: sDate});
var vmCustDiffList = new ViewModel("#panel_custDiffList");
var vmCustNewToTmp = new ViewModel("#panel_custNewToTmp", {yyyymm_use: sDate});
var vmCustNewNoEmailList = new ViewModel("#panel_custNewNoEmailList");
var vmCustEmailSync = new ViewModel("#panel_custEmailSync", {id_custs: ""});
var vmCustNewCreate = new ViewModel("#panel_custNewCreate", {date: sDate});
var vmCustNewCreateProgress = new ViewModel("#panel_custNewCreateProgress");
var vmCustNewList = new ViewModel("#panel_custNewList");


// 거래처 등록 파일 업로드
function stopCustErpUpload(){
	$.getJSON("/ctrl/lnerp/cust.erp.upload.stop.jsp", function(){

	}, function(){
		alert("작업 중단에 실패 했습니다.");
	});
}

stopCustErpUpload();

vmCustErpUpload.onbeforesubmit(function(){
	iTimerFlag = setTimeout(function(){
		if (vmCustErpUpload.loading() === false){
			return;
		}
		vmCustErpUpload.loading(true);
		vmCustErpUploadProgress.working(true);
		vmCustErpUploadProgress.submit();
		vmCustErpUploadProgress.progress(0);
		vmCustErpUploadProgress.workErr(0);
		vmCustErpUploadProgress.prop.error([]);
		//vmCustErpUploadProgress.prop.errorText( new Date().toString() );
	}, 500);
});

vmCustErpUpload.onsubmit(function(res, viewModel){
	var vm = viewModel
	;

	if (res.data.result.valid){
		vmCustErpUploadProgress.progress(100);

		setTimeout(function(){
			alert("업로드 작업이 완료 되었습니다.");
			vmCustErpUploadProgress.working(false);

			if ($.isArray(res.data.info.error) && (res.data.info.error.length > 0)){
				vmCustErpUploadProgress.prop.error( res.data.info.error );
				vmCustErpUploadProgress.workErr( res.data.info.error.length );
			}
			
		}, 200);
	}
	else{
		clearTimeout(iTimerFlag);
		vmCustErpUploadProgress.working(false);
	}
});

vmCustErpUploadProgress.onsubmit(function(res, vm){
	var mInfo = res.data.info
	,	iCurr = parseInt( mInfo.curr )
	,	iTotal = parseInt( mInfo.total )
	,	aError = mInfo.error
	;

	//console.log("vmCustErpUploadProgress errors", aError);

	if (mInfo && res.data.result.valid){
		//console.log("valid.");
		//console.log(vm);
		vm.workCurr( iCurr );
		vm.workMax( iTotal );
		if (iCurr !== 0 && iTotal !== 0){
			vm.progress( (iCurr / iTotal) * 100 );
		}
		
		//console.log("vmCustErpUploadProgress error check.");

		if ($.isArray(aError) && aError.length > 0){
			//console.log("vmCustErpUploadProgress yes errors");
			vm.prop.error( aError );
			vm.workErr( aError.length );
		}

		//console.log("valid end.");
	}
	else{
		vm.workErr( vm.workErr() + 1 );

		return;
	}

	setTimeout(function(){
		console.log(iCurr, iTotal);
		//console.log("vmCustErpUpload.loading", vmCustErpUpload.loading());
		if ((vmCustErpUpload.loading() === false)){
			vmCustErpUploadProgress.working(false);

			return;
		}

		vmCustErpUploadProgress.submit();
	}, 500);
});

vmCustErpUpload.onbuttonclick(function(){
	stopCustErpUpload();
});

// 거래처 변경 요청 파일 생성
vmCustDiffCheck.onsubmit(function(res, vm){
	var result = res.data.result;

	vmCustDiffList.load();

	alert(result.msg);
});

// 신규 거래처 추출

function stopNewCustExtracting(){
	vmCustEmailSync.userStop = false;
	vmCustNewToTmp.loading(false);
	vmCustEmailSync.working(false);
}

vmCustNewToTmp.onbeforesubmit(function(){
	vmCustEmailSync.userStop = false;
	vmCustEmailSync.progress(0);
	vmCustEmailSync.workCurr("신규거래처 추출");
	vmCustEmailSync.workMax(0);
	vmCustEmailSync.working(true);
});
vmCustNewToTmp.onsubmit(function(res, vm){
	if (vmCustEmailSync.userStop){
		stopNewCustExtracting();

		return;
	}
	if (!res.data.result.valid){
		alert(res.data.result.msg);
		vmCustNewToTmp.loading(false);
		vmCustEmailSync.working(false);

		return;
	}

	vmCustNewToTmp.loading(true);
	vmCustNewNoEmailList.load();
	vmCustEmailSync.progress(5);
	vmCustEmailSync.workCurr("이메일 없는 가맹점 요청");
});
vmCustNewToTmp.onbuttonclick(function(){
	if (confirm("정말 중지 하시겠습니까?")){
		vmCustEmailSync.userStop = true;
		vmCustNewToTmp.loading(false);
	}
});

// 이메일 동기화
function getPartOfArray(arr, idx, bdl){
	var bundle = bdl || 10,
		index = idx || 0,
		arrRet = arr.slice(index * bundle, bundle + (index * bundle) );
	;

	//console.log(bundle, index);
	//console.log(arrRet);

	return arrRet;
}

function joinField(arr, name){
	var saRet = []
	,	i = -1, iLen = arr.length
	;

	while( ++i < iLen ){
		saRet.push( arr[i][name] );
	}

	return saRet.join(",");
}

vmCustNewNoEmailList.onsubmit(function(res, vm){
	if (vmCustEmailSync.userStop){
		stopNewCustExtracting();

		return;
	}
	if (!res.data.data || res.data.data.length === 0){
		alert("동기화 할 내용이 발견되지 않았습니다.");

		vmCustNewToTmp.loading(false);
		vmCustEmailSync.working(false);

		return;
	}

	vmCustEmailSync.targets = res.data.data;
	vmCustEmailSync.syncIndex = 0;
	vmCustEmailSync.maxIndex = Math.floor(res.data.data.length / 10);

	vmCustNewToTmp.loading(true);
	vmCustEmailSync.progress(10);
	vmCustEmailSync.workCurr("이메일 없는 가맹점 요청");
	vmCustEmailSync.workMax(vmCustEmailSync.maxIndex + 1);
	vmCustEmailSync.workCurr(0);
	//vmCustEmailSync.prop.id_custs(  );
	vmCustEmailSync.submit({
		id_custs: joinField( getPartOfArray( vmCustEmailSync.targets, vmCustEmailSync.syncIndex, 10 ), "id_cust" )
	});
});

vmCustEmailSync.onsubmit(function(res, vm){
	if (vmCustEmailSync.userStop){
		stopNewCustExtracting();

		return;
	}

	if (!res.data.result.valid){
		alert(res.data.result.msg);

		vm.working(false);

		return;
	}



	if (vm.syncIndex >= vm.maxIndex){
		vm.progress(100);
		vm.workCurr( vm.workMax() );

		setTimeout(function(){
			alert("작업이 완료 되었습니다.");
			vmCustNewToTmp.loading(false);
			vm.working(false);
		}, 250);

		return;
	}

	vm.syncIndex++;

	vm.progress( 10 + ((vm.syncIndex / vm.maxIndex) * 90) );
	vm.workCurr( vm.workCurr() + 1 );

	vm.submit({
		id_custs: joinField( getPartOfArray( vm.targets, vm.syncIndex, 10 ), "id_cust" )
	});
});

// 신규 거래처 엑셀 파일 생성
vmCustNewCreate.onbeforesubmit(function(){
	iTimerFlag = setTimeout(function(){
		if (vmCustNewCreate.loading() === false){
			return;
		}
		vmCustNewCreateProgress.working(true);
		vmCustNewCreateProgress.submit();
		vmCustNewCreateProgress.progress(0);
	}, 500);
	
});
vmCustNewCreate.onsubmit(function(res, vm){
	if (!res.data.result.valid){
		alert(res.data.result.msg);
	}
	else{
		vmCustNewCreateProgress.progress(100);

		setTimeout(function(){
			alert("파일 생성에 성공 하였습니다.");
			vmCustNewList.load();
		}, 200);
		
	}
});
vmCustNewCreateProgress.onsubmit(function(res, vm){
	var mInfo = res.data.info
	,	iCurr = parseInt( mInfo.curr )
	,	itotal = parseInt( mInfo.total )
	;

	if (mInfo && res.data.result.valid){
		vm.workCurr( iCurr );
		vm.workMax( itotal );
		vm.progress( (mInfo.curr / mInfo.total) * 100 );
	}
	else{
		vm.workErr( vm.workErr() + 1 );

		return;
	}

	setTimeout(function(){
		if (vmCustNewCreate.loading() === false){
			vmCustNewCreateProgress.working(false);

			return;
		}

		vmCustNewCreateProgress.submit();
	}, 500);
});

});