$(function(){
"use strict";

var sDate = Util.dateFormat( Util.getPrevMonthFirstDate() );
var vmSalesXls = new ViewModel("#panel_salesXls", {yyyymm_use: sDate});
var vmTaxbillCloseUpload = new ViewModel("#panel_taxbillCloseUpload", {yyyymm_use: sDate} );
var vmTaxbillCloseUploadProgress = new ViewModel("#panel_taxbillCloseUploadProgress", {errorText: "", showError: false}, ["error"]);
var vmTaxbillCreate = new ViewModel("#panel_taxbillCreate", {yyyymm_use: sDate} );
var vmTaxbillCreateProgress = new ViewModel("#panel_taxbillCreateProgress", {errorText: "", showError: false}, ["error"]);
var vmTaxbillList = new ViewModel("#panel_taxbillList");

// 신규 폐업 파일 업로드
function stopCustErpUpload(callback){
	$.getJSON("/ctrl/lnerp/cust.erp.upload.stop.jsp", function(){
		if (callback){
			callback();
		}
	}, function(){
		alert("작업 중단에 실패 했습니다.");
	});
}

stopCustErpUpload();

vmTaxbillCloseUpload.onbeforesubmit(function(){
	iTimerFlag = setTimeout(function(){
		if (vmTaxbillCloseUpload.loading() === false){
			return;
		}
		vmTaxbillCloseUpload.loading(true);
		vmTaxbillCloseUploadProgress.working(true);
		vmTaxbillCloseUploadProgress.submit();
		vmTaxbillCloseUploadProgress.progress(0);
		vmTaxbillCloseUploadProgress.workErr(0);
		vmTaxbillCloseUploadProgress.prop.error([]);
	}, 500);
});

vmTaxbillCloseUpload.onsubmit(function(res, viewModel){
	var vm = viewModel
	;

	if (res.data.result.valid){
		vmTaxbillCloseUploadProgress.progress(100);

		setTimeout(function(){
			alert("업로드 작업이 완료 되었습니다.");
			vmTaxbillCloseUploadProgress.working(false);

			if ($.isArray(res.data.info.error) && (res.data.info.error.length > 0)){
				vmTaxbillCloseUploadProgress.prop.error( res.data.info.error );
				vmTaxbillCloseUploadProgress.workErr( res.data.info.error.length );
			}
			
		}, 200);
	}
	else{
		clearTimeout(iTimerFlag);
		vmTaxbillCloseUploadProgress.working(false);
	}
});

vmTaxbillCloseUploadProgress.onsubmit(function(res, vm){
	var mInfo = res.data.info
	,	iCurr = parseInt( mInfo.curr )
	,	iTotal = parseInt( mInfo.total )
	,	aError = mInfo.error
	;

	//console.log("vmTaxbillCloseUploadProgress errors", aError);

	if (mInfo && res.data.result.valid){
		//console.log("valid.");
		//console.log(vm);
		vm.workCurr( iCurr );
		vm.workMax( iTotal );
		if (iCurr !== 0 && iTotal !== 0){
			vm.progress( (iCurr / iTotal) * 100 );
		}
		
		//console.log("vmTaxbillCloseUploadProgress error check.");

		if ($.isArray(aError) && aError.length > 0){
			//console.log("vmTaxbillCloseUploadProgress yes errors");
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
		//console.log("vmTaxbillCloseUpload.loading", vmTaxbillCloseUpload.loading());
		if ((vmTaxbillCloseUpload.loading() === false)){
			vmTaxbillCloseUploadProgress.working(false);

			return;
		}

		vmTaxbillCloseUploadProgress.submit();
	}, 500);
});

vmTaxbillCloseUpload.onbuttonclick(function(){
	vmTaxbillCloseUpload.loading(false);
	stopCustErpUpload();
});


// 복붙해서 ㅈㅅ -_-;;

vmTaxbillCreate.onbeforesubmit(function(){
	iTimerFlag = setTimeout(function(){
		if (vmTaxbillCreate.loading() === false){
			return;
		}
		vmTaxbillCreate.loading(true);
		vmTaxbillCreateProgress.working(true);
		vmTaxbillCreateProgress.submit();
		vmTaxbillCreateProgress.progress(0);
		vmTaxbillCreateProgress.workErr(0);
		vmTaxbillCreateProgress.prop.error([]);
	}, 500);
});

vmTaxbillCreate.onsubmit(function(res, viewModel){
	var vm = viewModel
	;

	if (res.data.result.valid){
		vmTaxbillCreateProgress.progress(100);

		setTimeout(function(){
			alert(res.data.result.msg);
			vmTaxbillCreateProgress.working(false);
			
		}, 200);
	}
	else{
		clearTimeout(iTimerFlag);
		vmTaxbillCreateProgress.working(false);
	}
});

vmTaxbillCreateProgress.onsubmit(function(res, vm){
	var mInfo = res.data.info
	,	iCurr = parseInt( mInfo.curr )
	,	iTotal = parseInt( mInfo.total )
	,	aError = mInfo.error
	;

	//console.log("vmTaxbillCreateProgress errors", aError);

	if (mInfo && res.data.result.valid){
		//console.log("valid.");
		//console.log(vm);
		vm.workCurr( iCurr );
		vm.workMax( iTotal );
		if (iCurr !== 0 && iTotal !== 0){
			vm.progress( (iCurr / iTotal) * 100 );
		}
		
		//console.log("vmTaxbillCreateProgress error check.");

		if ($.isArray(aError) && aError.length > 0){
			//console.log("vmTaxbillCreateProgress yes errors");
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
		//console.log("vmTaxbillCreate.loading", vmTaxbillCreate.loading());
		if ((vmTaxbillCreate.loading() === false)){
			vmTaxbillCreateProgress.working(false);
			vmTaxbillList.load();

			return;
		}

		vmTaxbillCreateProgress.submit();
	}, 500);
});

// vmTaxbillCreate.onbuttonclick(function(){
// 	stopCustErpUpload();
// });



});