$(function(){
	"use strict";

	var vmCorpList = new ViewModel( "#panel_corpList", {nm_corp: "", nm_charger: ""} );
	var vmCorpIpList = new ViewModel( "#panel_corpIpList", {id_corp: ""} );
	var vmCorpIpDel = new ViewModel( "#panel_corpIpDel", {ip: "", id_corp: ""} );
	var vmCorpIpAdd = new ViewModel( "#panel_corpIpAdd", {ip: "", id_corp: ""} );
	var mCorp = undefined;
	var iWithAllow = 0;
	var mParam = {};

	vmCorpList.bind("rowselected", function(item, e, vm){
		// console.log(item);
		// console.log(vm);

		vmCorpIpList.submit({
			id_corp: item.id_corp
		});

		vmCorpIpList.prop.id_corp( item.id_corp );

		mCorp = item;
	});

	vmCorpList.bind("datafield", function(item, vm){
		item.editing = ko.observable( false );
		item.tel = ko.observable( item.tel );
	});


	vmCorpIpList.bind("itemdelete", function(item, e, vm){
		var sIp = Util.getVal( item.ip )
		,	sMac = Util.getVal( item.mac )
		,	sIdCorp = Util.getVal( item.id_corp )
		;

		if ( (!sIp && !sMac) || (!sIdCorp) ){
			return true;
		}

		if (confirm("해당 주소 (" + sIp + ", " + sMac + ")를 허용 목록에서 제외 하시겠습니까?") === false){
			return false;
		}
		vmCorpIpList.working(true);
		vmCorpIpDel.submit(item);
	});

	vmCorpIpList.bind("itemallow", function(item, e, vm){
		var sIp = Util.getVal( item.ip )
		,	sMac = Util.getVal( item.mac )
		,	sIdCorp = Util.getVal( item.id_corp )
		,	sIdLogin = mCorp.id_login
		,	jqInput = $(e.currentTarget)
						.parent() // td
						.prev() // 왼쪽td
						.find("input") // 내부 입력칸
		;

		iWithAllow = 0;

		if ( (!sIp && !sMac) || (!sIdCorp) ){
			return true;
		}

		if (confirm("해당 주소 (" + sIp + ", " + sMac + ")와 관련 지사의 SMS 인증을 강제 추가 하시겠습니까? ") === false){
			return false;
		}

		if (jqInput.length > 0){
			var event = jQuery.Event("keyup");

			event.keyCode = 13;
			
			iWithAllow = 500;
			mParam = {
				ip: sIp,
				mac: sMac,
				idCorp: sIdCorp,
				idLogin: sIdLogin
			};
			jqInput.trigger(event);
		}

		if (iWithAllow === 0){
			authAllow(sIp, sMac, sIdCorp, sIdLogin);
		}
	});

	function authAllow(sIp, sMac, sIdCorp, sIdLogin){
		$.getJSON("/ctrl/corpmng/corp.sms.auth.allow.jsp", {ip: sIp, mac: sMac, id_corp: sIdCorp, id_login: sIdLogin}, function(res){
			var msg = res.result.msg;

			if (msg.indexOf("추가 되었습니다") === -1){
				alert(msg);

				return;
			}

			$.getJSON("/ctrl/corpmng/mem.cache.clear.jsp", function(res){
				alert(msg + "\nSMS 인증이 추가 되었습니다.");
			});
		}, function(){
			alert("SMS 인증 추가에 실패 했습니다.");
		});
	}

	vmCorpIpDel.bind("submit", function(){
		vmCorpIpList.working(false);

		$.getJSON("/ctrl/corpmng/mem.cache.clear.jsp", function(res){
			alert("삭제 되었습니다.");
		});
	});



	vmCorpIpList.bind("itemadd", function(item, e, vm){
		item.id_corp = ko.observable( vmCorpIpList.prop.id_corp() );
		item.ip = ko.observable("");
		item.mac = ko.observable("");
	});

	vmCorpIpList.bind("itemedited", function(item, e, vm){
		//alert("성공:" + item.ip());
		vmCorpIpAdd.submit({
			id_corp: item.id_corp(),
			ip: item.ip(),
			mac: item.mac()
		});
	});

	vmCorpIpAdd.bind("submit", function(){
		vmCorpIpList.working(false);
		vmCorpIpList.load();

		if (iWithAllow){
			iWithAllow = 0;

			authAllow(
				mParam.ip,
				mParam.mac,
				mParam.idCorp,
				mParam.idLogin
			);

			return;
		}

		$.getJSON("/ctrl/corpmng/mem.cache.clear.jsp", function(res){
			alert("추가 되었습니다.");
		});
	});
});