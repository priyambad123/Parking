<?php
$username="searchke_priyam";
$host="localhost";
$dbname="searchke_parkingonthego";
$password="incredable1";

$connect=mysqli_connect($host,$username,$password,$dbname);

if(!$connect)
{echo "database not connected".mysqli_connect_error();
}
else{
echo"db connect success";
}
?>