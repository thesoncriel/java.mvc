$(function(){
    "use strict";

    var vmEquipList = new ViewModel( "#panel_h2tvEquipList", 
        {"no_serial": "", 
        "mac": "", 
        "have_type": "", 
        "id_nm_cust": "", 
        "id_nm_corp": ""
        },
        null,
        function(){
            this.excelDownParams = ko.pureComputed(function(){
                var aRet = []
                ,   mProp = this.prop
                ,   key
                ,   i = 0
                ;

                for(key in mProp){
                    if (i++ > 0){
                        aRet.push( "&" );
                    }

                    aRet.push( key );
                    aRet.push( "=" );
                    aRet.push( mProp[key]() );
                }

                return "?" + aRet.join("");
            }, this);
        }
    );

    var vmEquipAdd = new ViewModel( "#panel_h2tvEquipAdd",
        {
            no_serial: "",
            mac: "",
            equip_type: "ASTB"
        });

    vmEquipAdd.onsubmit(function(res, vm){
        if (res.data.info){
            this.prop.no_serial("");
            this.prop.mac("");
            //this.prop.equip_type("ASTB");

            vmEquipList.load();
        }
    });

    var vmEquipAddExcel = new ViewModel( "#panel_h2tvEquipAddExcel" );

    vmEquipAddExcel.onsubmit(function(res, vm){
        var data = res.data
        ,   info
        ,   msg
        ,   bSuccess = false
        ;

        if (data){
            info = data.info;

            if (info){
                msg = "업로드 작업이 완료 되었습니다.\n성공: " + info.succ + "\n" + "실패: " + info.fail;
                if (parseInt(info.succ) > 0){
                    bSuccess = true;
                }
            }
            else{
                msg = "실패: 작업 결과 정보가 없습니다.\n요청 경로가 잘못 되었거나 모두 실패했을 수 있습니다.";
            }
        }
        else{
            msg = "실패: 응답 데이터가 없습니다.";
        }

        alert(msg);

        if (bSuccess){
            vmEquipList.load();
        }
    });
});