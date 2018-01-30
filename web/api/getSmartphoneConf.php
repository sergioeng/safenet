<?php
include ("../SqlHelper.php");
  
try {  
  
  $file_log = fopen("../logs/api.txt", "w");

  $imei =  $_REQUEST['imei'];
  $numRows = 0;
  $setsArray=array();
  $sqlHelper = new SqlHelper;
 
  $query = "SELECT USER_ID FROM USERS WHERE imei=$imei";
  $sqlHelper->select($query);
  $numRowsFound = $sqlHelper->numRows;
  fwrite ($file_log, "Num users found=$numRowsFound for IMEI=$imei\n");
  if ($numRowsFound > 0) {
    $row =  $sqlHelper->nextRow();
    $user_id = $row['USER_ID'];	   

    $query = "SELECT * FROM SETTINGS WHERE user_id=$user_id";
    $sqlHelper->select($query);
    $numRowsFound = $sqlHelper->numRows;
    fwrite ($file_log, "Num settings found=$numRowsFound for USER_ID=$user_id\n");

    for ($i=0; $i<$numRowsFound; $i++) {
      $row =  $sqlHelper->nextRow();
      $setsArray [$i] = new \stdClass();
      $setsArray [$i]->id = $row['ID'];
      $setsArray [$i]->name=$row['NAME'];
      $type=$row['TYPE'];
      if ($type == 'I') {
	$setsArray [$i]->value=$row['VALUE'];
      }
      else if ($type == 'B') {
	$value = $row['VALUE'];
	if ($value == "T") 
	  $setsArray [$i]->value=true;
	else 
	  $setsArray [$i]->value=false;
      }
      else {
	  $setsArray [$i]->value=$row['VALUE'];
      }
    }
  }

  $myJSON = json_encode($setsArray);
  echo $myJSON;

}
catch(Exception $exception){
  echo "Error: " . $exception->getMessage();
}



/*
$myObj1=new \stdClass();
$myObj2=new \stdClass();
$myObj3=new \stdClass();
$myObj4=new \stdClass();
$myObj5=new \stdClass();
$myObj6=new \stdClass();
$myObj7=new \stdClass();
$myObj8=new \stdClass();
$myObj9=new \stdClass();
$myObj10=new \stdClass();

$sympt1=new \stdClass();
$sympt2=new \stdClass();
$sympt3=new \stdClass();
$sympt4=new \stdClass();
$sympt5=new \stdClass();

$schedule1=new \stdClass();
$schedule2=new \stdClass();
$schedule3=new \stdClass();

// Sintomas
$sympt1->name      = "Temperatura";
$sympt1->has_entry = true;
$sympt1->unit      = "C";

$sympt2->name      = "Dor";
$sympt2->has_entry = true;
$sympt2->unit      = "1-10";

$sympt3->name      = "Fraqueza";
$sympt3->has_entry = true;
$sympt3->unit      = "1-10";

$sympt4->name      = "Raiva";
$sympt4->has_entry = false;
$sympt4->unit      = "";

$sympt5->name      = "Tontura";
$sympt5->has_entry = false;
$sympt5->unit      = "";

$simptArr = array ($sympt1,$sympt2,$sympt3,$sympt4,$sympt5);

// Schedule
$schedule1->time = "08:00";
$schedule2->time = "17:41";
$schedule3->time = "16:00";

$scheduleArr = array ($schedule1, $schedule2, $schedule3);

$myObj1->id = "1";
$myObj1->name="DTASYNFREQ";
$myObj1->value="0";
$myObj1->description="Data Sync Frequency";

$myObj2->id = "2";
$myObj2->name="GPSLOCON";
$myObj2->value="0";
$myObj2->description="GPS Location is active";

$myObj9->id = "9";
$myObj9->name="GUARDIANNUM";
$myObj9->value="+5521996030969";
$myObj9->description="Guardian phone number";

$myObj10->id = "10";
$myObj10->name="CHECKINTYPE";
$myObj10->value="1";
$myObj10->description="Check in type";
$myObj10->symptom  = $simptArr;
$myObj10->schedule = $scheduleArr;


$myArr = array ($myObj1,$myObj2,$myObj9, $myObj10);
$myJSON = json_encode($myArr);
echo $myJSON;
*/

?>
