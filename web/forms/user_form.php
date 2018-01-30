<?php
require_once("../daos/UserRestHandler.php");
{
    Logger::setFilename ("../logs/user.log");

    $view = "";
    if(isset($_GET["view"]))
	    $view = $_GET["view"];

    $log = Logger::getInstance();
    $log->info ("UsersController.php: VIEW=['$view']");

    switch($view){
	case "show":
		show ();
		break;

	case "add":
		add ();
		break;

	case "del":
		del ($_GET["id"]);
		break;
		
	case "update":
		update ($_GET["id"]);
		break;

	case "" :
		//404 - not found;
		break;
    }

}

//========================================================================================

function add () {

echo "<form id='addUserForm' action='#' method='post' border='0'>";
echo "    <table>";
echo "        <tr>";
echo "            <td>Name</td>";
echo "            <td><input type='text' name='name' required /></td>";
echo "        </tr>";
echo "        <tr>";
echo "            <td>Phone number</td>";
echo "            <td><input type='number' name='phone_num' required /></td>";
echo "        </tr>";
echo "        <tr>";
echo "            <td>IMEI</td>";
echo "            <td><input type='number' name='imei' required /></td>";
echo "        </tr>";
echo "        <tr>";
echo "            <td>Type</td>";
echo "            <td>";
echo "	            <input type='radio' name='type' value='p' required />  Patient<br>";
echo "                <input type='radio' name='type' value='g' required />  Guardian<br>";
echo "            </td>";
echo "        <tr>";
echo "            <td></td>";
echo "            <td>";
echo "                <input type='submit' value='Save' class='customBtn' />";
echo "            </td>";
echo "        </tr>";
echo "    </table>";
echo "</form>";

}



function show () {

	$userRestHandler = new UserRestHandler();
	$userJson = $userRestHandler->getAllUsers();

	$log = Logger::getInstance();
    $log->info ("user_form.php: show () ==> json=['$userJson']");
    $userArr = json_decode ($userJson, true);
//    $log->info ("user_form.php: show () ==> arr =['$userArr']");    

    echo "<table id='tfhover' class='tftable' border='1'>";
        
    echo "<tr>";
    echo "<th>Name</th>";
    echo "<th>Phone number</th>";
    echo "<th>IMEI</th>";
    echo "<th>Patient</th>";
    echo "</tr>";

    foreach ($userArr as $user) {
	    foreach ($user as $field) {
//	        $log->info ("user_form.php: show (user) ==> field=['$field']");    
	                
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

}


function update ($id) {

	$userRestHandler = new UserRestHandler();
	$userJson = $userRestHandler->getUser($id);

    $log = Logger::getInstance();
    $log->info ("user_form.php: update () ==> json=['$userJson']");
    $userArr = json_decode ($userJson, true);
    $log->info ("user_form.php: update () ==> single=['$row']");    

    foreach ($userArr as $user) {
	    foreach ($user as $field) {
	        $log->info ("show_user.php: show (user) ==> field=['$field']");    
	                
	        $id	= $field['ID'];	   
	        $name       = $field['NAME'];
	        $phone_num  = $field['PHONE_NUM'];
	        $imei       = $field['IMEI'];
	        $pat_or_gua = $field['PAT_OR_GUA'];
        }
        break;
    }                            

    echo "<form id='updateUserForm' action='#' method='post' border='0'>";
    echo "<table>";
        echo "<tr>";
            echo "<td>Name</td>";
            echo "<td><input type='text' name='name'  value='$name' required /></td>";
        echo "</tr>";
        echo "<tr>";
            echo "<td>Phone number</td>";
            echo "<td><input type='number' name='phone_num' value='$phone_num' required/></td>";
        echo "<tr>";
        echo "<tr>";
            echo "<td>IMEI</td>";
            echo "<td><input type='number' name='imei' value='$imei' required /></td>";
        echo "</tr>";
        echo "<tr>";
	        echo "<td>Type</td>";
	        echo "<td >";
	        if ($pat_or_gua == '1') {
				echo "<input type='radio' name='type' value='1' checked='checked' required /> Patient <br>";
				echo "<input type='radio' name='type' value='0' required /> Guardian <br>";
			}
			else {
				echo "<input type='radio' name='type' value='1' required /> Patient <br>";
				echo "<input type='radio' name='type' value='0' checked='checked' required /> Guardian <br>";
			}
	        echo "</td>";
        echo "</tr>";
	    echo "<tr>";
            echo "<td></td>";
            echo "<td>";
                echo "<input type='hidden' name='data_id' value='$id' />";
                echo "<input type='submit' value='Save' class='btn btn-small btn-inverse' />";
            echo "</td>";
        echo "</tr>";
    echo "</table>";
    echo "</form>";

}

?>

