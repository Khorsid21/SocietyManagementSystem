<?php

require "connect.php";

$name = $_POST["namekey"];
$email = $_POST["emailkey"]; 
$mobile = $_POST["mobilekey"];

$insert = "insert into temp values(null,'$name','$email','$mobile')";


if($connect->query($insert)===TRUE){
     echo "Registration Done";
    
}
else{
   echo "Not Done";
}

$connect->close();

?>