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

	    $id		= $row['ID'];	   
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
		  echo "<div class='dataId' style='display:none'>{$id}</div>";
					
		  //we will use this links on next part of this post
		  echo "  ";
		  echo "<div class='symptomsBtn  btn btn-mini btn-primary'> SYMPTOMS </div>";
		  echo "  ";
					
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

  var entity = \"symptoms\" ;

  $(document).ready(function(){
 
    // clicking the SYMPTOMS button
    $(document).on('click', '.symptomsBtn', function(){ 
    
        var data_id = $(this).closest('td').find('.dataId').text();
        
        console.log(data_id);
        
        // show a loader image
        $('#loaderImage').show();

        setTimeout(\"$('#pageContent').load('\" + entity +\"/showsymptoms.php?user_id=\" + data_id + \"', function() { $('#loaderImage').hide(); });\",1000);
    }); 

        // UPDATE FORM IS SUBMITTED
     $(document).on('submit', '#symptomsUpdateDataForm', function() {

        // show a loader img
        $('#loaderImage').show();
        
        // post the data from the form
        $.post(entity +\"/symptoms_update.php\", $(this).serialize()).done(function(data) {
                // 'data' is the text returned, you can do any conditions based on that
                showSymptoms();
        });
               
        return true;
    });

 
});

function showSymptoms(){

    // read and show the records after at least a second
    // we use setTimeout just to show the image loading effect when you have a very fast server
    // otherwise, you can just do: $('#pageContent').load('read.php', function(){ $('#loaderImage').hide(); });
    // THIS also hides the loader image
    var link = entity + \"/showsymptoms.php?user_id=\" + user_id ;
    $('#pageContent').load(link, function(){ $('#loaderImage').hide(); });
}


</script>
";
?>
