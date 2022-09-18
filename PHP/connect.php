<?php
$username = "id15972940_vivek";
$password = "Dv55569998d@";
$dbname = "id15972940_member_123456";
$url = "localhost";

$connect = mysqli_connect($url,$username,$password,$dbname);

if(!$connect){
    echo "db not Connect";
}
else{
    echo "db connect success";
}

?>