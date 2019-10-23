/**
 * クッキーを設定する場合はこちらを使う
 */
$(function(){
	<!-- 閲覧履歴 -->
	var allCookie = $.cookie('recent_view');
	var now_item = $('#add_cart [name=itemId]').val();
	
	if(allCookie == undefined){
		<!--クッキーrecent_viewが存在しなかったとき-->
		var recent_view = $('#add_cart [name=itemId]').val();
		$.cookie('recent_view', recent_view, { expires: 1, path:'/'});
	}else{
		<!--クッキーrecent_viewが存在するとき-->
		var recent_view = allCookie.split(',');
		
		if(recent_view.indexOf(now_item) == -1){
			//現在の商品と最近見た商品が重複しないときはなにもしない
		}else{
			//現在の商品と重複する最近見た商品がある場合は	削除する
			recent_view.splice(recent_view.indexOf(now_item), 1);
		}
		
		if(recent_view.length < 5){
			//最近見た商品が5個未満のとき
			recent_view.unshift(now_item);
			var recent_view_str = recent_view.join(',');
			$.cookie('recent_view', recent_view_str, { expires: 1, path:'/'});
		}else{
			//最近見た商品が5個以上のとき
			recent_view.pop();
			recent_view.unshift(now_item);
			var recent_view_str = recent_view.join(',');
			$.cookie('recent_view', recent_view_str, { expires: 1, path:'/'});
		}
	}
});