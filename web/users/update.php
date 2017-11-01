<?php
include ("../SqlHelper.php");
$useLog = 1;
$numRows = 0;
$sqlHelper = new SqlHelper;



try{
	$file_log = fopen("../logs/update.txt", "w");
    fwrite ($file_log, "--- LOG BEGUN -----------------------\n");

	//write query
	//in this case, it seemed like we have so many fields to pass and 
	//its kinda better if we'll label them and not use question marks
	//like what we used here
	$user_id =  $_POST['id'];
	$name       = $_POST['name'];
	$phone_num  = $_POST['phone_num'];
	$imei       = $_POST['imei'];
	$pat_or_gua = $_POST['type'];

	$log = "LOG> user_id=[$user_id]\n";
   fwrite ($file_log, $log);
	$log = "LOG> name=[$name]\n";
   fwrite ($file_log, $log);
	$log = "LOG> phone_num=[$phone_num]\n";
   fwrite ($file_log, $log);
	$log = "LOG> imei=[$imei]\n";
   fwrite ($file_log, $log);
	$log = "LOG> pat_or_gua=[$pat_or_gua]\n";
   fwrite ($file_log, $log);

	$query = "UPDATE USERS SET 
	  name='$name', 
	  phone_num='$phone_num', 
	  imei='$imei', 
	  pat_or_gua='$pat_or_gua'
	WHERE id=$user_id";


	$ret = $sqlHelper->update ($query);

    $log = "LOG> query=[$query]\n";
    fwrite ($file_log, $log);
    $log = "LOG> retorno=$ret\n";
	fwrite ($file_log, $log);
	
	// Execute the query
	if($ret == 1){
		echo "User was updated. ret=$ret \n";
	}else{
		echo "Unable to update user. ret=$ret \n";
	}

}

//to handle error
catch(Exception $exception){
	echo "Error: " . $exception->getMessage();
}
?>
