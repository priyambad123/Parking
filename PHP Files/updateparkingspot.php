<?php

$response = array();
if (isset($_POST['individualparkinglotid']) && isset($_POST['fromtime']) && isset($_POST['endtime']) && $_POST['parkingspotid'])
 {
 
file_put_contents("outputfilespot.txt", file_get_contents("php://input"));
 
    $individualparkinglotid = $_POST['individualparkinglotid'];
    $fromTime = $_POST['fromtime'] + 0 ;
    $toTime = $_POST['endtime'] + 0 ;
    $parkingspotid = $_POST['parkingspotid'];
    
    
    $tableName = "parkingspots";
 	
 	
$fp = fopen("testupdateaddress.txt", "w") or die("Unable to open file!");

fwrite($fp, $individualparkinglotid);
fwrite($fp, $fromTime);
fwrite($fp, $toTime);
fwrite($fp, $parkingspotid);

   // Write $somecontent to our opened file. 
   fclose($fp); 


 	
 	
 	
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 	
    // connecting to db
    $db = new DB_CONNECT();
 
  
          
          
          $query = "UPDATE $tableName SET fromtime = FROM_UNIXTIME($fromTime/1000), totime = FROM_UNIXTIME($toTime/1000) WHERE parkingspotsid = $parkingspotid ;";
          
	$result = mysql_query($query) or die(mysql_error());

  
           if($result)
           {
           		$addressquery = "SELECT address, latitude, longitude FROM parkinglots WHERE parkinglotsid = $individualparkinglotid LIMIT 1;";
           			$resultaddress = mysql_query($addressquery) or die(mysql_error());
           			
           			if (!empty($resultaddress))
           			{
           			if (mysql_num_rows($resultaddress) > 0) {
                            $response["success"] = 1;
 		$response["parkingaddressarray"] = array();
		while ($row = mysql_fetch_array($resultaddress)) 
		{
		
        // temp user array 
            $parkingaddress = array();
            $parkingaddress["parkingaddress"] = $row["address"];
            $parkingaddress["latitude"] = $row["latitude"];
            $parkingaddress["longitude"] = $row["longitude"];            
            // push single product into final response array
        array_push($response["parkingaddressarray"], $parkingaddress);
            
              } //while
              echo (json_encode($response));
           			} // >0
           } //empty
           }// $result
           else {
    // required field is missing
                        $response["success"] = 1;
    					$response["message"] = "Error in update";// field missing
 
    // echoing JSON response
    echo json_encode($response);
}
     
            
       
} // checking post condition
else {
    // required field is missing
    $response["success"] = 1;
    $response["message"] = "Required field(s) is missing";// field missing
 
    // echoing JSON response
    echo json_encode($response);
}
?>