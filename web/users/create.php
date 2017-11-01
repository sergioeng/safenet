<?php
//include database connection
include ("../SqlHelper.php");

$sqlHelper = new SqlHelper;


try{

$file_log = fopen("../logs/create.txt", "w");
	
	$name      = $_POST['name'];
	$phone_num = $_POST['phone_num'];
	$imei      = $_POST['imei'];
	$type      = $_POST['type'];

$log = "LOG> name=[$name]\n";
fwrite ($file_log, $log);
$log = "LOG> phone_num=[$phone_num]\n";
fwrite ($file_log, $log);
$log = "LOG> imei=[$imei]\n";
fwrite ($file_log, $log);
$log = "LOG> type=[$type]\n";
fwrite ($file_log, $log);

	//write query

	if ($type == "g") {
	   $pat_or_gua = 0;
	}
	else {
	   $pat_or_gua = 1;
	}
	
	$query = "INSERT INTO USERS 
	  (name, phone_num, imei, pat_or_gua) VALUES
	  ('$name','$phone_num', '$imei', '$pat_or_gua') ";

	$ret = $sqlHelper->insert ($query);
	
$log = "LOG> query=[$query]\n";
fwrite ($file_log, $log);
$log = "LOG> retorno=[$ret]\n";
fwrite ($file_log, $log);
	
	// Execute the query
	if($ret == 1){
	  echo "User was created. ret=$ret";
	  $log = "User was created. ret=$ret";
	  
	}else{
	  echo "Unable to create user. ret=$ret";
	  $log = "Unable to create user. ret=$ret";
	}

fwrite ($file_log, $log);

	
}

//to handle error
catch(PDOException $exception){
	echo "Error: " . $exception->getMessage();
}
?>