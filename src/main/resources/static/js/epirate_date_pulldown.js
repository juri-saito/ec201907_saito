/**
 * クレジットカード有効年のプルダウンを生成する
 */
$(function(){
	//innerHTMLで毎回要素を空にして<select>内を空にする（どんどんappendされるのをふせぐため）
	$("#expirationMonth").html("");
	//子プルダウンの<select>内にfor文でappend
	for (var i = 2019; i <= 2039; i++) {
		$("#expirationMonth").append("<option value=" + i + ">" + i + "</option>");
	}
});