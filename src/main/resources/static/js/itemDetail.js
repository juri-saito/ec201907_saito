$(function() {
	calc_price();
	$(".size").on("change", function() {
		calc_price();
	});
	$(".checkbox").on("change", function() {
		calc_price();
	});
	$("#total").on("change", function() {
		calc_price();
	});

	function calc_price() {
		var size = $(".size:checked").val();
		console.log(size);
		var topping_number = $("#check input.checkbox:checked").length;
		var tmp = $("#total").val();
		var total = parseInt(tmp);

		console.log("size1111111");
		if (size == "M") {
			var curry_price = $("#M").data('value');
			var topping_price = 200 * topping_number;

		} else {
			var curry_price = $("#L").data('value');
			var topping_price = 300 * topping_number;
		}
		var price = (curry_price + topping_price) * total;
		$("#total-price").text(price.toLocaleString());
	}
	;

});
