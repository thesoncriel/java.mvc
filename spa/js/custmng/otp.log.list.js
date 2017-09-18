$(function(){
    "use strict";

    var dateNow = new Date()
    ,   sDate = Util.dateFormat( dateNow )
    ,   sDtStart = sDate
    ,   sDtEnd = sDate
    var vmOtpLogList = new ViewModel( "#panel_otpLogList", {"dt_start": sDtStart, "dt_end": sDtEnd} );

    // function regreqAllow(e, item){
    //     var msgPrompt = "등록된 가맹점ID를 입력해 주십시오. (11자리 숫자)",
    //         msgInvalid = "가맹점 ID는 11자리 숫자여야 합니다.",
    //         msgConfirm = "등록된 가맹점 ID가 아래와 같습니까?\n",
    //         sIdCust = "";

    //     if (item.req_state() === "Y"){
    //         return;
    //     }

    //     sIdCust = prompt(msgPrompt, "");

    //     // 취소 누르면 바깥 try~catch로 넘어가 아무것도 안함.

    //     if (sIdCust.length !== 11 || $.isNumeric(sIdCust) === false){
    //         alert( msgInvalid );

    //         return;
    //     }

    //     if (confirm( msgConfirm + sIdCust) === false){
    //         return;
    //     }

    //     vmCustRegReqAllow.prop.id_cust( sIdCust );

    //     vmCustRegReqAllow.submit({
    //         id_reg_seq: item.id_reg_seq,
    //         id_cust: sIdCust
    //     }, item);
    // }

    // function regreqCancel(e, item){
    //     var msgConfirm = "해당 요청을 취소 하시겠습니까?";

    //     if (confirm(msgConfirm) === false){
    //         return;
    //     }

    //     vmCustRegReqCancel.submit({
    //         id_reg_seq: item.id_reg_seq
    //     }, item);
    // }

    // vmCustRegReqList.ontabclick(function(tabId, e, viewModel){
    //     var jqTab = $(e.currentTarget)
    //     ;

    //     if (tabId === "wait"){
    //         viewModel.prop.req_state("N");
    //     }
    //     else{
    //         viewModel.prop.req_state("Y");
    //     }

    //     viewModel.load();

        

    //     return false;
    // });

    // // observable 로 변환할 필요가 있는 필드를 전환 시킴.
    // vmCustRegReqList.ondatafield(function(item, viewModel){
    //     item.req_state = ko.observable( item.req_state );
    //     item.id_cust = ko.observable( item.id_cust );
    // });

    // vmCustRegReqList.onitemclick(function(e, item){
    //     if ($(e.currentTarget).data("role") === "cancel"){
    //         regreqCancel(e, item);
    //     }
    //     else{
    //         regreqAllow(e, item);
    //     }
    // });

    // vmCustRegReqAllow.onsubmit(function(res, viewModel){
    //     res.ref.req_state("Y");
    //     res.ref.id_cust( viewModel.prop.id_cust() );

    //     alert("완료 처리 되었습니다.");
    // });

    // vmCustRegReqCancel.onsubmit(function(res, viewModel){
    //     res.ref.req_state("C");

    //     alert("취소 처리 되었습니다.");
    // });

    /*vmFileList.onrowselected(function(e){
        vmFileDataList.load({
            id_eb11: e.data.id_eb11
        });
    });*/

    // vmFileList.load();

});