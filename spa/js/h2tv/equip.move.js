$(function(){
    "use strict";

    var vmEquipCustList = new ViewModel( "#panel_h2tvEquipCustList", {id_cust: ""} );
    var vmEquipCorpList = new ViewModel( "#panel_h2tvEquipCorpList", {id_corp: ""} );

    vmEquipCustList.load();
    vmEquipCorpList.load();
});