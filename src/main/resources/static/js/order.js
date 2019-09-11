/**
 * 
 */
$(function(){

	var today = new Date();
	//昨日の日付を取り出す
	today.setDate(today.getDate() - 1);
	
	var year = today.getFullYear();
	var  month = ("0"+(today.getMonth() + 1)).slice(-2);
	var  day = ("0"+(today.getDate() + 1)).slice(-2);
	
	var minimumDate = year + "-" + month + "-" + day;
	
	
	$("#deliveryDate").attr('min', minimumDate);
	
	$("#zipcode-button").on("click",function(){
		alert($(destinationZipcode).val());
		AjaxZip3.zip2addr('destinationZipcode','','destinationAddress','destinationAddress');
		alert($(destinationZipcode).val());
	});
});