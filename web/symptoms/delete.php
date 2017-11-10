<?php
//include database connection
include ("../SqlHelper.php");
$sqlHelper = new SqlHelper;

$file_log = fopen("../logs/delete.txt", "w");


try {
	$user_id =  $_REQUEST['data_id'];
$log = "LOG> id=[$user_id]\n";
fwrite ($file_log, $log);

	$query = "DELETE FROM SYMPTOMS WHERE id = $user_id";
	$ret = $sqlHelper->delete($query);

$log = "LOG> ret=[$ret]\n";
fwrite ($file_log, $log);

	
}

//to handle error
catch(PDOException $exception){
	echo "Error: " . $exception->getMessage();
}
?>