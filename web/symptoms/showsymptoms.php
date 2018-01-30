<?php
include ("../SqlHelper.php");

$file_log = fopen("../logs/read.txt", "w");

$numRows = 0;
$sqlHelper = new SqlHelper;

$user_id =  $_REQUEST['user_id'];

//select all data
$query = "SELECT * FROM SYMPTOMS WHERE USER_ID=$user_id";
$sqlHelper->select($query);
$numRowsFound = $sqlHelper->numRows;

fwrite ($file_log, "SYMPTOMS: USER_ID=$user_id --- num rows found=$numRowsFound\n");

//this is how to get number of rows returned
echo "<div id='addSymptom'> <button type='button' class='btn btn-small btn-info'>+New symptom</button> </div>";

//if ($numRowsFound > 0) {

    echo "<table id='tfhover' class='tftable' border='1'>";//start table
    
    //creating our table heading
    echo "<tr>";
        echo "<th>Name</th>";
        echo "<th>Data Entry</th>";
        echo "<th>Unit</th>";
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
	    $add_entry  = $row['ADD_ENTRY'];
	    $unit       = $row['UNIT'];
            
            //creating new table row per record
            echo "<tr>";
                echo "<td>{$name}</td>";
                echo "<td>{$add_entry}</td>";
                echo "<td>{$unit}</td>";
                echo "<td style='text-align:center;'>";
		// add the record id here
		echo "<div class='dataId' style='display:none'>{$id}</div>";
					
                    //we will use this links on next part of this post
                echo "<div class='deleteBtn btn btn-mini btn-danger'>DEL</div>";
                echo "</td>";
            echo "</tr>";
        }
        
    echo "</table>";//end table
    
//}
?>

<?php
echo "
<script type='text/javascript'>
	var entity = 'symptoms';
	var user_id = '$user_id';

// clicking the '+ NEW DADO' button
$('#addSymptom').click(function(){
    showCreateSymptomForm();
});

function showCreateSymptomForm(){
    // show a loader image
    $('#loaderImage').show();
    
    // read and show the records after 1 second
    // we use setTimeout just to show the image loading effect when you have a very fast server
    // otherwise, you can just do: $('#pageContent').load('read.php');
    setTimeout(\"$('#pageContent').load('\"+ entity +\"/symptom_create_form.php', function(){ $('#loaderImage').hide(); });\",1000);
}

$(document).on('submit', '#addSymptomForm', function() {

    // show a loader img
    $('#loaderImage').show();
        
    // post the data from the form
    $.post(entity +\"/symptom_create.php?user_id=\"+$user_id, $(this).serialize())
            .done(function(data) {
       showSymptoms();
    });

    return true;
});

    // when clicking the DELETE button
    $(document).on('click', '.deleteBtn', function(){ 
            // get the id
            var data_id = $(this).closest('td').find('.dataId').text();
            console.log(data_id);
            
            // trigger the delete file
            $.post(entity+\"/delete.php?data_id=\" + data_id).done(function(data) {
                    // you can see your console to verify if record was deleted
                    console.log(data);
                    
                    $('#loaderImage').show();
                    
                    // reload the list
                    showSymptoms();
                    
                });
        return true;
    });

    
function showSymptoms(){

    // read and show the records after at least a second
    // we use setTimeout just to show the image loading effect when you have a very fast server
    // otherwise, you can just do: $('#pageContent').load('read.php', function(){ $('#loaderImage').hide(); });
    // THIS also hides the loader image
    var link = entity + \"/showsymptoms.php?user_id=\" + $user_id ;
    $('#pageContent').load(link, function(){ $('#loaderImage').hide(); });
}


	
</script>
";

?>