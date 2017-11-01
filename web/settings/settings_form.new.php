<?php
include ("../SqlHelper.php");
$file_log = fopen("../logs/read.txt", "w");

$numRows = 0;
$sqlHelper = new SqlHelper;
/*
$cars = array("Volvo", "BMW", "Toyota");
echo "I like " . $cars[0] . ", " . $cars[1] . " and " . $cars[2] . ".";
CAMPOS		DOMINIO		UNIT		DESCRIPTION
"DTASYNFREQ" 	numeric	  	minutes    	Data Sync Frequency
"GPSLOCON"    	Y/N			--	GPS Location is active
"EMGYCALLON"	Y/N			--	Emergency call is active
"SOSON"    	Y/N			--	SOS is active
"CHKION" 	Y/N			--	Check In is actve
"FALLDETCON"    Y/N			--	Fall Detetction is active
"NOMOTIONON"    Y/N			-- 	No motion fucntion is active
"PHSTATUSON"    Y/N			--	Phone stattus is active
"GUARDIANNUM"   Numeric only    --		Guardian phone number
"CHECKINTYPE"   A/B/C		--		Check in type, where:
									A: Button only
									B: Added text message 
									C: Added text message and check list
"MINNMFDTIME"	numeric		minutes	Minimum no motion time after fall
"NMSTARTTIME"	numeric		hour 		No motion start time
"NMSTOPTIME"	numeric		hour 		No motion stop time

TYPE (BOL,INT,TEX)

{"id":1,"name":"DTASYNFREQ","value":0,"description":"Data Sync Frequency"},
{"id":2,"name":"GPSLOCON","value":0,"description":"GPS Location is active"},
{"id":3,"name":"EMGYCALLON","value":0,"description":"Emergency call is active"},
{"id":4,"name":"SOSON","value":0,"description":"SOS is active"},
{"id":5,"name":"CHKION","value":0,"description":"Check In is active"},
{"id":6,"name":"FALLDETCON","value":0,"description":"Fall Detetction is active"},
{"id":7,"name":"NOMOTIONON","value":0,"description":"No motion fucntion is active"},
{"id":8,"name":"PHSTATUSON","value":0,"description":"Phone stattus is active"},
{"id":9,"name":"GUARDIANNUM","value":0,"description":"Guardian phone number"},
{"id":10,"name":"CHECKINTYPE","value":0,"description":"Check in type"}]
*/
$NUM_PARAMS=9;
$id    = array (1           ,2         ,3           ,4      ,5       ,6           ,7           ,8            ,9);
$name  = array ("DTASYNFREQ","GPSLOCON","EMGYCALLON","SOSON","CHKION","FALLDETCON","NOMOTIONON","GUARDIANNUM","CHECKINTYPE");
$type  = array ('I'         ,'B'       ,'B'         ,'B'    ,'B'     ,'B'         ,'B'         ,'I'          ,'T');
$value = array ('0'         ,'T'       ,'T'         ,'T'    ,'T'     ,'T'         ,'T'         ,'000'        ,'A');

try {
	
	$user_id =  $_REQUEST['data_id'];
	
	//prepare query
	$query = "SELECT * FROM SETTINGS WHERE USER_ID=$user_id";
	$sqlHelper->select($query);
	$numRowsFound = $sqlHelper->numRows;
	
	fwrite ($file_log, "settings_form: numOfRows=$numRowsFound\n");
	
	//execute our query
	if ($numRowsFound > 0) {
	
	    for ($x = 0; $x < $numRowsFound; $x++) {
	
		//store retrieved row to a variable
		$row = $sqlHelper->nextRow();

		//values to fill up our form
		$name    [$x] = $row['NAME'];
		$type    [$x] = $row['TYPE'];
		$value   [$x] = $row['VALUE'];
	    }
		
	} else {
	    for ($i = 0; $i < $NUM_PARAMS; $i++) {
	        /// Insert default settings in databese
	        $query = "INSERT INTO SETTINGS 
		      (user_id, name, type, value) VALUES
		      ('$user_id', '$name[$i]','$type[$i]', '$value[$i]') ";
	        $ret = $sqlHelper->insert ($query);
	    }
	}
}
//to handle error
catch(PDOException $exception){
	echo "Error: " . $exception->getMessage();
}
?>



<!--we have our html form here where new user information will be entered-->
<form id='settingsUpdateDataForm' action='#' method='post' border='0'>
    <table cellpadding=5px>
        <?php for ($i=0; $i < $NUM_PARAMS; $i++) { ?> 
        <tr>
            <td><?php echo $name[$i]; ?></td>
            <td><input type='text' name='<?php echo $name[$i];  ?>'  value='<?php echo $value[$i];  ?>' required /></td>
	    <td >
	        <?php $temp = $type[$i]; ?>
	        <?php if ($temp == 'I') { ?>
		   <td> INTEGER </td>
		<?php } else if ($temp == 'B') { ?>
		   <td> BOOLEAN </td> 
		<?php } else { ?>
		   <td> TEXT </td>
		<?php } ?>
	    </td>
        </tr>
    	<?php } ?>
        <tr>
        </tr>
	<tr>
            <td></td>
            <td>
                <!-- so that we could identify what record is to be updated -->
                <input type='hidden' name='id' value='<?php echo $user_id ?>' />
                <input type='submit' value='Update' class='btn btn-small btn-inverse' />
				
            </td>
        </tr>
    </table>
</form>
