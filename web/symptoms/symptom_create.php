<?php
//include database connection
include ("../SqlHelper.php");

$sqlHelper = new SqlHelper;


try{

$file_log = fopen("../logs/create.txt", "w");
	
	$user_id   = $_REQUEST['user_id'];
	$name      = $_POST['name'];
	$add_entry = $_POST['add_entry'];
	$unit      = $_POST['unit'];

$log = "LOG> user_id=[$user_id]\n";
fwrite ($file_log, $log);
$log = "LOG> name=[$name]\n";
fwrite ($file_log, $log);
$log = "LOG> add_entry=[$add_entry]\n";
fwrite ($file_log, $log);
$log = "LOG> unit=[$unit]\n";
fwrite ($file_log, $log);

	//write query
	$query = "INSERT INTO SYMPTOMS 
	  (user_id, name, add_entry, unit) VALUES
	  ('$user_id', '$name','$add_entry', '$unit') ";

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