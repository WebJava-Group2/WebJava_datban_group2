$(document).ready(function(){
  $('.btn-delete').click(function(){
      var id = $(this).attr("id-foods");
      var row = $(this).closest('tr');
      $.ajax({
        method: "POST",
        url: "http://localhost:8080/crm/api/foods",
        data: { idFood: id}
      })
      .done(function( data ) {
              row.remove();
        });
       });
       
      $('.btn-add').click(function(e){
      e.preventDefault();
      var nameFood = $("#nameFood").val();
      var price = $("#price").val();
      var descriptionFood = $("#descriptionFood").val();
      var mealType = $("#mealType").val();
      var imageUrl = $("#imageUrl").val();
      $.ajax({
        method: "POST",
        url: "http://localhost:8080/crm/api/foods/add",
        data: {nameFood:nameFood, price:price, descriptionFood:descriptionFood, mealType:mealType, imageUrl:imageUrl}
      })
      .done(function(data) {
              alert("add Success!");
               location.reload();

        });
       });
});


