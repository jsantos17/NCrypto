var button;
var destination;
var message;
var origin;
var key;
var vector;

$(document).ready(function(){
	button=$('#btn_submit');
	destination=$('#destination');
	message=$('#message');
	origin=$('#origin');
	key=$('#key');
    vector = $('#vector');
	button.click(create);
	origin.focus();
	$('#form_container').bind("keypress", filterKeys);
});

function filterKeys(e) {
    if(e.which == 13) {
      create();
    }
 }

function create() {
	$.post("receiveMessage.php",{origin: origin.val(), destination: destination.val(), key: key.val(), vector: vector.val(), message: message.val()}, onSuccess);
		
	
	}

function onSuccess(data) {
	  if (data=="success"){
		$("#error_text").html("Mensaje Insertado");
		$("#error_text").css("visibility","visible");
		setTimeout("reset()",1000);
	  }
}

function reset(){
	message.val("");
	key.val("");
    vector.val("");
    key.focus();
	$("#error_text").css("visibility","hidden");
}