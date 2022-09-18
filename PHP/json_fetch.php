<?php

require "connect.php";

$query = "select * from temp";

$raw = mysqli_query($connect,$query);

while($res=mysqli_fetch_array($raw)){

        $data[] = $res;

}

print(json_encode($data));

$connect->close();

?>