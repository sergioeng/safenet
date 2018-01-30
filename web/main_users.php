<?php
	$filename = basename(__FILE__, '.php');
	
	include_once("inc/auth.inc.php");
	include('header.php');
	$sqlHelper = new SqlHelper;
	
?>

<body>
    <!-- Wrapper Start -->
    <div id="wrapper" class="container-fluid">
	<!-- Header -->
	<div class="ie-dropdown-fix" >
	    <!-- Header -->
            <div class="row-fluid" id="header">
		<!-- Logo -->
		  <div class="span5"> <a href="#"><img src="images/logo.png" alt="logo" /></a> </div>
                    <!-- Social / Contact -->
                  <div class="span4 pull-right">
		      <!-- Social Icons -->
                      <ul class="social-icons"> </ul>
		      <!-- Contact Details -->
						-->
		  </div>
            </div>
            <!-- Header / End -->
	    <!-- Navigation -->
            <?php include("navbar.php"); ?>
            <div class="clear"></div>
        </div>
        <!-- Navigation / End -->
	<!-- Content -->
	<div align="left" class="row-fluid">
	    <br><br>
<!-- SERGIO BEGIN-->			
	    <div style='margin:0 0 .5em 0;'>
		<!-- when clicked, it will show the user's list -->
		<div id='viewData'> <button type="button" class="btn btn-small btn-info">Users</button> </div>
		<!-- when clicked, it will load the add user form -->
		<div id='addUser'> <button type="button" class="btn btn-small btn-info">+ New user</button> </div>
		<!-- this is the loader image, hidden at first -->
		<div id='loaderImage'><img src='images/ajax-loader.gif' /></div>
		<div style='clear:both;'></div>
	    </div>
	    <!-- this is wher the contents will be shown. -->
	    <div id='pageContent'></div>
<!-- SERGIO END -->			
            <!-- Content continues -
	    <form align="left" class="form-signin" action="colaboradores.php" method="post"> </form>
	    -->
	</div>
	<br>
        <!-- Blog  End -->
    </div>
    <!-- Wrapper / End -->
    <!-- Footer -->
    <?php include("footer.php"); ?>
  </body>
</html>


<!--
//----------------------------------------------------------------------------------------
// JAVA SCRIPTS SECTION
// 
//----------------------------------------------------------------------------------------
-->

<script type='text/javascript'>

$(document).ready(function() {
    
    // Force to show users on load of the page
    $('#loaderImage').show();
    showUsers();
    
    // Show users when clicking on 'USERS' button
    $('#viewData').click(function(){
        // show a loader img
        $('#loaderImage').show();
        
        showUsers();
    });
    
    // Display ADD FORM when clicking on '+ NEW xxxx' button
    $('#addUser').click(function(){
        showAddUserForm();
    });
    

    // ADD FORM IS SUBMITTED 
    $(document).on('submit', '#addUserForm', function() {
        $('#loaderImage').show();

        var name = document.forms["addUserForm"]["name"].value;
        var user = {"NAME":document.forms["addUserForm"]["name"].value, 
                    "PHONE_NUM":document.forms["addUserForm"]["phone_num"].value,  
                    "IMEI":document.forms["addUserForm"]["imei"].value, 
                    "TYPE":document.forms["addUserForm"]["type"].value};
//        console.log (user);

		$.ajax({
            type: "POST",
            data :JSON.stringify(user),
            url: "/user/add/",
            contentType: "application/json"
        }).done(function() {
            showUsers();
        }); 
        
//        console.log ("End of AJAX");
        
        return false;
    });

    // UPDATE FORM IS SUBMITTED
    $(document).on('submit', '#updateUserForm', function() {

        $('#loaderImage').show();

        var name = document.forms["updateUserForm"]["name"].value;
        var user = {"NAME":document.forms["updateUserForm"]["name"].value, 
                    "PHONE_NUM":document.forms["updateUserForm"]["phone_num"].value,  
                    "IMEI":document.forms["updateUserForm"]["imei"].value, 
                    "TYPE":document.forms["updateUserForm"]["type"].value};
//        console.log (user);

        var data_id = document.forms["updateUserForm"]["data_id"].value;
        
        $.ajax({
            type: "POST",
            data :JSON.stringify(user),
            url: "/user/u/"+data_id+"/",
            contentType: "application/json"
        }).done(function() {
            showUsers();
        }); 
        
        return false;
    });

    
    // When clicking the DELETE button
    $(document).on('click', '.deleteBtn', function(){ 
        if(confirm('Are you sure?')) {
            var data_id = $(this).closest('td').find('.dataId').text();
//            console.log("Deleting row: [" + data_id + "]");
            $.post("/user/d/" + data_id + "/")
             .done(function(data) {
//                  console.log(data);
                  $('#loaderImage').show();
                  showUsers();
             });
        }
    });
    
    // Wen clicking the EDIT button
    $(document).on('click', '.editBtn', function(){ 
    
        var data_id = $(this).closest('td').find('.dataId').text();
//        console.log(data_id);
        
        // show a loader image
        $('#loaderImage').show();

        setTimeout("$('#pageContent').load('/user/updform/" + data_id + "/', function(){ $('#loaderImage').hide(); });",1000);
        
    }); 
    
});

// READ USERS
function showUsers(){
    setTimeout("$('#pageContent').load('/user/shwform/', function(){ $('#loaderImage').hide(); });", 1000);
}

// ADD USER FORM
function showAddUserForm(){
    $('#loaderImage').show();
    setTimeout("$('#pageContent').load('/user/addform/', function(){ $('#loaderImage').hide(); });",1000);
}
</script>

