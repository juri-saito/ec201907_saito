/**
 * 住所検索
 */
$(function(){
	//「住所検索」ボタンクリックで検索開始
	$('#search').click(function(){
		//検索
		$.ajax({
			url: "http://zipcoda.net/api",
			dataType: "jsonp",
			data: {zipcode: $('#zipcode').val()},
		})
		
		.done(function(data) { //成功の場合.done関数の中身が呼ばれる。dataの中にJSON形式で帰ってきた値が入る
	      // コンソールに取得データを表示
	      console.log(data);
	      console.dir(JSON.stringify(data));
	      $("#address").val(data.items[0].address);//dataの中のitem[]配列の0番目のaddress値を#addressの場所に返す
		})
    
		 // 検索失敗時には、その旨をダイアログ表示
	    .fail(function() {
	      window.alert('正しい結果を得られませんでした。');
	    });
	});
});