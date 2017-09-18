$(function(){
    "use strict";

    var vmClientInfo = new ViewModel( "#panel_h2tvClientInfo", ["id_cust", "nm_cust", "id_cust_svc"] )
    var vmClientList = new ViewModel( "#panel_h2tvClientList", ["id_cust", "nm_cust", "id_cust_svc"] );
    var vmClientEnable = new ViewModel( "#panel_h2tvClientEnable", ["id_client"] );
    var vmClientAdd = new ViewModel( "#panel_h2tvClientAdd", ["id_cust", "client_mac", "confirmed"] );

    // observable 로 변환할 필요가 있는 필드를 전환 시킴.
    vmClientList.ondatafield(function(item, viewModel){
        item.yn_use = ko.observable( item.yn_use );
    });

    vmClientList.onitemclick(function(e, item){
        var mParam = {
            id_client: item.id_client,
            yn_use: ""
        };

        if (item.yn_use() === "Y"){
            mParam.yn_use = "N";
        }
        else{
            mParam.yn_use = "Y";
        }

        vmClientEnable.submit( mParam, item );
    });

    vmClientEnable.onsubmit(function(res, viewModel){
        res.ref.yn_use( (res.ref.yn_use() === "Y")? "N" : "Y" );
    });

    vmClientAdd.onbeforesubmit(function(url, param){
        if (!param.client_mac){
            alert( "MAC 주소를 입력 해 주세요." );

            return false;
        }

        param.client_mac = param.client_mac.trim().replace( /\-/ig, "" );

        if (param.client_mac.length !== 12){
            alert(param.client_mac + " - MAC 주소는 12자리 여야 합니다.");

            return false;
        }

        if ((!param.id_cust)){
            alert("고객 정보가 부족합니다.");

            return false;
        }
    });
    vmClientAdd.onsubmit(function(res, viewModel){
        if (res.data.info){
            if (confirm(res.data.result.msg)){
                viewModel.prop.confirmed("yes");
                setTimeout(function(){
                    viewModel.submit();
                }, 250);
            }

            return;
        }

        viewModel.prop.client_mac("");
        viewModel.prop.confirmed("");

        vmClientList.load();
    });

    vmClientList.load();
    /*vmFileList.onrowselected(function(e){
        vmFileDataList.load({
            id_eb11: e.data.id_eb11
        });
    });*/

    // vmFileList.load();
    vmClientInfo.onbeforesubmit(function(url, param){
        if (confirm("계약 내용을 동기화 하시겠습니까?") === false){
            return false;
        }
    });
    vmClientInfo.onsubmit(function(res, viewModel){
        var msg = "";

        if (res.data.data){
            msg = "계약 내용이 동기화 되었습니다.";
        }
        else{
            msg = "계약 내용 동기화에 실패 했습니다.";
        }

        alert(msg);
    });

});