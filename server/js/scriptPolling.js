var button;
var destination;
var origin;

$(document).ready(function(){
	button=$('#btn_submit');
	destination=$('#destination');
	origin=$('#origin');
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
	$.post("checkQueue.php",{origin: origin.val(), destination: destination.val()}, onSuccess);
		
	
	}

function onSuccess(data) {
	  alert(data    );
}

function reset(){
    origin.focus();
	$("#error_text").css("visibility","hidden");
}