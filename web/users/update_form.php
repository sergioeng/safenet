<?php
include ("../SqlHelper.php");
$numRows = 0;
$sqlHelper = new SqlHelper;


try {
	
	$user_id =  $_REQUEST['data_id'];
	
	//prepare query
	$query = "SELECT * FROM USERS WHERE id=$user_id";
	$sqlHelper->select($query);
	$numRowsFound = $sqlHelper->numRows;

	//execute our query
	if ($numRowsFound) {
	
		//store retrieved row to a variable
		$row = $sqlHelper->nextRow();

		//values to fill up our form
		$id         = $row['ID'];
		$name       = $row['NAME'];
		$phone_num  = $row['PHONE_NUM'];
		$imei       = $row['IMEI'];
		$pat_or_gua = $row['PAT_OR_GUA'];
		
		  
		
	}else{
		echo "User not found";
	}
}

//to handle error
catch(PDOException $exception){
	echo "Error: " . $exception->getMessage();
}
?>
<!--we have our html form here where new user information will be entered-->
<form id='updateDataForm' action='#' method='post' border='0'>
    <table>
        <tr>
            <td>Name</td>
            <td><input type='text' name='name'  value='<?php echo $name;  ?>' required /></td>
        </tr>
        <tr>
            <td>Phone number</td>
            <td><input type='number' name='phone_num' value='<?php echo $phone_num;  ?>' required/></td>
        <tr>
        <tr>
            <td>IMEI</td>
            <td><input type='number' name='imei' value='<?php echo $imei; ?>' required /></td>
        </tr>
        <tr>
	        <td>Type</td>
	        <td >
	        <?php
	        if ($pat_or_gua == '1') {
	        ?>
				<input type='radio' name='type' value='1' checked="checked" required /> Patient <br>
				<input type='radio' name='type' value='0' required /> Guardian <br>
	        <?php
			}
			else {
	        ?>
				<input type='radio' name='type' value='1' required /> Patient <br>
				<input type='radio' name='type' value='0' checked="checked" required /> Guardian <br>
	        <?php
			}
	        ?>
	        </td>
        </tr>
	<tr>
            <td></td>
            <td>
                <!-- so that we could identify what record is to be updated -->
                <input type='hidden' name='id' value='<?php echo $id ?>' />
                
                <input type='submit' value='Update' class='btn btn-small btn-inverse' />
				
            </td>
        </tr>
    </table>
</form>
