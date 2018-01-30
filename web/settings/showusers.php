<?php
include ("../SqlHelper.php");

//$file_log = fopen("../logs/read.txt", "w");

$numRows = 0;
$sqlHelper = new SqlHelper;

//select all data
$query = "SELECT * FROM USERS ORDER BY name";
$sqlHelper->select($query);
$numRowsFound = $sqlHelper->numRows;

//echo "Num rows foud=$numRowsFound";

//this is how to get number of rows returned
if ($numRowsFound > 0) {

    echo "<table id='tfhover' class='tftable' border='1'>";//start table
    
    //creating our table heading
    echo "<tr>";
        echo "<th>Name</th>";
        echo "<th>Phone number</th>";
        echo "<th>IMEI</th>";
        echo "<th>Patient</th>";
    echo "</tr>";

        //retrieve our table contents
        //fetch() is faster than fetchAll()
        while ($numRows < $numRowsFound){
            $numRows = $numRows + 1;
            //extract row
            //this will make $row['firstname'] to
            //just $firstname only
	    $row =  $sqlHelper->nextRow();
	    
	    //fwrite ($file_log, $row);

	    $user_id		= $row['USER_ID'];	   
	    $name       = $row['NAME'];
	    $phone_num  = $row['PHONE_NUM'];
	    $imei       = $row['IMEI'];
	    $pat_or_gua = $row['PAT_OR_GUA'];
            
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
//		  echo "<div class='container'>";  
		  echo "<div class='settingsBtn btn btn-mini btn-primary'> SETTINGS </div>";
//		  echo "  ";
//		  echo "<div class='symptomsBtn  btn btn-mini btn-primary'> SYMPTOMS </div>";
//		  echo "  ";
//		  echo "<div class='scheduleBtn btn btn-mini btn-primary'> SCHEDULE </div>";
//                  echo "</div>";
					
		  //we will use this links on next part of this post
		  //echo "<div class='deleteBtn btn btn-mini btn-danger'>RMV</div>";
                echo "</td>";
            echo "</tr>";
        }
        
    echo "</table>";//end table
    
}
else {
    echo "<div class='noneFound'>Não há users</div>";
}

?>
<?php
echo "
<script type='text/javascript'>

  var entity = \"settings\" ;

  $(document).ready(function(){
 
    // clicking the SETTINGS button
    $(document).on('click', '.settingsBtn', function(){ 
    
        var data_id = $(this).closest('td').find('.dataId').text();
        
        console.log(data_id);
        
        // show a loader image
        $('#loaderImage').show();

        setTimeout(\"$('#pageContent').load('\" + entity +\"/settings_form.php?data_id=\" + data_id + \"', function(){ $('#loaderImage').hide(); });\",1000);
        ///$.post('\" + entity +\"/settings_form.php?data_id=\" + data_id);
        
    }); 
    
        
    // UPDATE FORM IS SUBMITTED
     $(document).on('submit', '#settingsUpdateDataForm', function() {

        // show a loader img
        $('#loaderImage').show();
        
        // post the data from the form
        $.post(entity +\"/settings_update.php\", $(this).serialize()).done(function(data) {
                // 'data' is the text returned, you can do any conditions based on that
                showUsers();
        });
                 
        return true;
    });
  });
</script>
";
?>
