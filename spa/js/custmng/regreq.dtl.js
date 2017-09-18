$(function(){
    "use strict";

    var idRegSeq = Util.getParam().id_reg_seq;

    var vmCustRegReqDtl = new ViewModel( "#panel_custRegReqDtl", {id_reg_seq: idRegSeq} );

    vmCustRegReqDtl.load();
});