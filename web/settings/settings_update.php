<?php
include ("../SqlHelper.php");
$useLog = 1;
$numRows = 0;
$sqlHelper = new SqlHelper;

$NUM_PARAMS=9;
$name  = array ("DTASYNFREQ","GPSLOCON","EMGYCALLON","SOSON","CHKION","FALLDETCON","NOMOTIONON","GUARDIANNUM","CHECKINTYPE");
$type  = array ('I'         ,'B'       ,'B'         ,'B'    ,'B'     ,'B'         ,'B'         ,'I'          ,'T');

try{
	$file_log = fopen("../logs/update.txt", "w");

	//write query
	//in this case, it seemed like we have so many fields to pass and 
	//its kinda better if we'll label them and not use question marks
	//like what we used here
	$user_id = $_POST['id'];
	$log = "LOG> user_id=[$user_id]\n";
	fwrite ($file_log, $log);
	
	for ($i=0; $i<$NUM_PARAMS; $i++) {
	   $value = $_POST[$name[$i]];
  	   $log = "LOG> [$name[$i] = $value\n";
	   fwrite ($file_log, $log);

	  $query = "UPDATE SETTINGS SET value='$value' WHERE USER_ID=$user_id AND NAME='$name[$i]'" ;
	  $ret = $sqlHelper->update ($query);

	  $log = "LOG> query=[$query]\n";
	  fwrite ($file_log, $log);
	  $log = "LOG> retorno=$ret\n";
	  fwrite ($file_log, $log);
	}
	
	// Execute the query
	if($ret == 1){
		echo "Data was updated. ret=$ret \n";
	}else{
		echo "Unable to update data. ret=$ret \n";
	}

}

//to handle error
catch(Exception $exception){
	echo "Error: " . $exception->getMessage();
}
?>
