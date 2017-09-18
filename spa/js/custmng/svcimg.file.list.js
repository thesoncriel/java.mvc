$(function(){
    "use strict";

    var vmCustSvcImgList = new ViewModel( "#panel_custSvcImgList" );
    var vmCustSvcImgFileList = new ViewModel( "#panel_custSvcImgFileList" );
    var vmCustSvcImgFileSizeUpdate = new ViewModel( "#panel_custSvcImgFileSizeUpdate" );

    /*vmFileList.onrowselected(function(e){
        vmFileDataList.load({
            id_eb11: e.data.id_eb11
        });
    });*/
    //ko.applyBindings( vmCustSvcImgList, vmCustSvcImgList.getContext() );
    //ko.applyBindings( vmCustSvcImgFileList, vmCustSvcImgFileList.getContext() );
    //ko.applyBindings( vmCustSvcImgFileSizeUpdate, vmCustSvcImgFileSizeUpdate.getContext() );

    // vmFileList.load();


    function updateFileSize(arr, nowIndex, countAtOnce){
        var vm = vmCustSvcImgFileSizeUpdate,
            aSlice,
            sJson,
            iEndIndex = nowIndex + countAtOnce - 1,
            iLen = arr.length,
            mReq = {};

        if (iLen < iEndIndex){
            iEndIndex = iLen;
        }

        aSlice = arr.slice(nowIndex, iEndIndex);
        mReq.reqList = aSlice;

        if (window.JSON){
            sJson = JSON.stringify(mReq);
        }
        else{
            sJson = $.stringify(mReq);
        }

        if (nowIndex === 0){
            vm.progress(0);
            vm.workCurr( 0 );
            vm.workMax( iLen );
        }
        
        vm.tmp.arr = arr;
        vm.tmp.nowIndex = iEndIndex + 1;
        vm.tmp.countAtOnce = countAtOnce;

        vm.working(true);
        vm.submit({json: sJson});
    }

    vmCustSvcImgFileList.onsubmit(function(res){
        var iCountAtOnce = parseInt( res.param.countAtOnce || 5 );

        try{
            if (!res.data.data){
                throw "전송 된 파일 자료에 data 항목이 존재하지 않습니다.";
            }

            vmCustSvcImgFileList.working(true);
            updateFileSize(res.data.data, 0, iCountAtOnce);
        }
        catch(ex){
            alert("전송 된 파일 자료에 오류가 있습니다.\n" + ex);
            console.log(ex);
        }
    });

    vmCustSvcImgFileList.onbuttonclick(function(e){
        var jqElem = $(e.currentTarget),
            msgConfirm = jqElem.data("confirm");

        if (confirm(msgConfirm)){
            vmCustSvcImgFileSizeUpdate.working(false);
        }
    });

    vmCustSvcImgFileSizeUpdate.onsubmit(function(res, viewModel){
        var vm = viewModel,
            arr = vm.tmp.arr,
            url = res.url,
            param = res.param,
            iLen = arr.length,
            nowIndex = vm.tmp.nowIndex,
            countAtOnce = vm.tmp.countAtOnce,
            iCurr = nowIndex + 1,
            dProgress = (iCurr / iLen) * 100,
            iProgress = Math.floor(dProgress);

        if ((nowIndex > iLen) || (iProgress >= 100)){
            vm.workCurr( iLen );
            vm.progress(100);

            setTimeout(function(){
                alert("작업이 완료 되었습니다.");
                vmCustSvcImgFileList.working(false);
                vm.working(false);
            }, 500);

            return;
        }

        if (vm.working() === false){
            vmCustSvcImgFileList.working(false);
            alert("작업이 임의 종료 되었습니다.");

            return;
        }

        vm.workCurr( nowIndex );
        vm.progress( dProgress );
        updateFileSize(arr, nowIndex, countAtOnce);
    });
});