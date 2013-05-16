<?php
    include_once("connect_to_db.php");
    
    $origin_hash = $_POST['origin'];
    $destination_hash = $_POST['destination'];
    
    $conn = get_connection();
    
    $sql="SELECT id FROM user WHERE user_hash = :origin;";
    $q = $conn->prepare($sql);
    $q -> execute(array(':origin'=>$origin_hash));
    if ($q->rowCount() == 0) {
        $conn = null;
        die("error");
    }
    else{
        $row = $q->fetch();
        $origin_id=$row['id'];
    }
    
    $sql="SELECT id FROM user WHERE user_hash = :destination;";
    $q = $conn->prepare($sql);
    $q -> execute(array(':destination'=>$destination_hash));
    if ($q->rowCount() == 0) {
        $conn = null;
        die("error");
    }
    else{
        $row = $q->fetch();
        $destination_id=$row['id'];
    }
    
    $sql="SELECT `id`, `message`, `key`, `vector` from `message` where origin_id = :origin AND destination_id = :destination AND `read` = FALSE;";
    $q = $conn->prepare($sql);
    $q -> execute(array(':origin'=>$origin_id, 'destination'=>$destination_id));
    if ($q->rowCount() == 0) {
        $conn = null;
        die("error");
    }
    else{
        $result = array();
        $sec = 1;
        $sqlUpdate="UPDATE `message` SET `read` = TRUE WHERE id = :id;";
        $qUpdate = $conn->prepare($sqlUpdate);
        while($row = $q->fetch()){
		    $result["sec".$sec] = array('message' => $row['message'], 'key' => $row['key'], 'vector' => $row['vector']);
            $sec++;
            $qUpdate -> execute(array(':id'=>$row['id']));
		}
        $response = json_encode($result);  
        $conn=null;
        echo $response;
    }
	
?>