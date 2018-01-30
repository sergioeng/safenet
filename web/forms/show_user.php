<?php
require_once("../daos/UserRestHandler.php");

	$userRestHandler = new UserRestHandler();
	$userJson = $userRestHandler->getAllUsers();

	Logger::setFilename ("../logs/user.log");
	$log = Logger::getInstance();
    $log->info ("show_user.php: show () ==> json=['$userJson']");
    $userArr = json_decode ($userJson, true);
    $log->info ("show_user.php: show () ==> arr =['$userArr']");    

    echo "<table id='tfhover' class='tftable' border='1'>";
        
    echo "<tr>";
    echo "<th>Name</th>";
    echo "<th>Phone number</th>";
    echo "<th>IMEI</th>";
    echo "<th>Patient</th>";
    echo "</tr>";

    foreach ($userArr as $user) {
	    foreach ($user as $field) {
	        $log->info ("show_user.php: show (user) ==> field=['$field']");    
	                
	        $user_id	= $field['ID'];	   
	        $name       = $field['NAME'];
	        $phone_num  = $field['PHONE_NUM'];
	        $imei       = $field['IMEI'];
	        $pat_or_gua = $field['PAT_OR_GUA'];
                    
            //creating new table row per record
            echo "<tr>";
            echo "<td>{$name}</td>";
            echo "<td>{$phone_num}</td>";
            echo "<td>{$imei}</td>";
            echo "<td>{$pat_or_gua}</td>";
            echo "<td style='text-align:center;'>";
                
			// add the record id here
			echo "<div class='dataId' style='display:none'>{$user_id}</div>";
			
            //we will use this links on next part of this post
            echo "<div class='editBtn btn btn-mini btn-warning'>EDT</div>";
					
            //we will use this links on next part of this post
            echo "<div class='deleteBtn btn btn-mini btn-danger'>RMV</div>";
            echo "</td>";
            echo "</tr>";
        }
    }
            
    echo "</table>";//end table

?>