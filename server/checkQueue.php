<?php
    include_once("connect_to_db.php")

    $sender_id = $_POST['sender'];
    $receiver_id = $_POST['receiver'];

    $conn = get_connection();
	
?>