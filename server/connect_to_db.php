<?php
  function get_connection() {
    $user = "usecuremessage";
    $password = "securemessage";
    $dbname = "securemessage";
    $host = "localhost";
    $connection_string = "mysql:host=$host;dbname=$dbname";
    try {
      $dbh = new PDO($connection_string,$user,$password);
	  $dbh->query('SET NAMES utf8'); 
    }
    catch(Exception $exception) {
      die("failed to connect with exception: " . $exception->getMessage());
    }
    return $dbh;
  }

?>
