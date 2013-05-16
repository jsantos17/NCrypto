var button;
var destination;
var message;
var origin;
var price;

$(document).ready(function(){
	button=$('#btn_submit');
	destination=$('#destination');
	message=$('#message');
	origin=$('#origin');
	price=$('#price');
	button.click(create);
	destination.focus();
	$('#form_container').bind("keypress", filterKeys);
});

function filterKeys(e) {
    if(e.which == 13) {
      create();
    }
 }

function create() {
	$.post("receiveMessage.php",{origin: origin.val(), destination: destination.val(), key: "algo", message: "mensaje"}, onSuccess);
		
	
	}

function onSuccess(data) {
      if (data=="duplicate"){
		$("#error_text").html("Un Rubro Con Código: "+destination.val()+" Ya Existe");
		$("#error_text").css("visibility","visible");
	  }
	  else if(data=="too_long"){
		$("#error_text").html("El Código No Puede Tener Más de 4 Dígitos");
		$("#error_text").css("visibility","visible");
	  }
	  else{
		$("#error_text").html("Rubro Creado");
		$("#error_text").css("visibility","visible");
		setTimeout("reset()",1000);
	  }
}

function reset(){
	destination.val("");
	message.val("");
	origin.val("");
	price.val("");
	$("#error_text").css("visibility","hidden");
}