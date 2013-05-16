<?php
    include_once("connect_to_db.php");

    $origin_hash = $_POST['origin'];
    $destination_hash = $_POST['destination'];
    $key = $_POST['key'];
    $vector = $_POST['vector'];
    $message = $_POST['message'];
    error_log("Mensaje: ".$message."\norigen: ".$origin_hash."\ndestino: ".$destination_hash."\nVector: ".$vector."\nKEY: ".$key);

    $conn = get_connection();
    
    try{
        $conn->beginTransaction();
        
        $sql="SELECT id FROM user WHERE user_hash = :origin;";
        $q = $conn->prepare($sql);
        $q -> execute(array(':origin'=>$origin_hash));
        if ($q->rowCount() == 0) {
            $sql="INSERT INTO user VALUES(null, :userHash);";
            $q = $conn->prepare($sql);
            $q -> execute(array(':userHash'=>$origin_hash));
            $origin_id=$conn->lastInsertId();
        }
        else{
            $row = $q->fetch();
            $origin_id=$row['id'];
        }
        
        $sql="SELECT id FROM user WHERE user_hash = :destination;";
        $q = $conn->prepare($sql);
        $q -> execute(array(':destination'=>$destination_hash));
        if ($q->rowCount() == 0) {
            $sql="INSERT INTO user VALUES(null, :userHash);";
            $q = $conn->prepare($sql);
            $q -> execute(array(':userHash'=>$destination_hash));
            $destination_id=$conn->lastInsertId();
        }
        else {
            $row = $q->fetch();
            $destination_id=$row['id'];
        }
        
        $sql="INSERT INTO message values(null, :originID, :destinationID, :message, :key, :vector, now(), FALSE);";
        $q = $conn->prepare($sql);
        $q -> execute(array(':originID'=>$origin_id, ':destinationID'=>$destination_id, ':message'=>$message, ':key'=>$key, ':vector'=>$vector));
        if ($q->rowCount() == 0) {
            $conn->rollBack();
            $conn = null;
            die("error");
        }
        
        $conn->commit();
        $conn = null;
        echo "success";
        
    }catch(PDOException $e) {
        $conn->rollBack();
        $conn = null;
        die("error");
    }
?>