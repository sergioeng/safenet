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
                    <div class="span5">
                        <a href="#"><img src="images/logo.png" alt="logo" /></a>
                    </div>
                    <!-- Social / Contact -->
                    <div class="span4 pull-right">
                        <!-- Social Icons -->
                        <ul class="social-icons">
                        </ul>
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
			    <!-- when clicked, it will show the user's list --
			    <div id='viewData'> 
				<button type="button" class="btn btn-small btn-info">User List</button>
  			    </div>

  			    <!-- when clicked, it will load the add user form 
			    <div id='addSettings'>
				<button type="button" class="btn btn-small btn-info">+ Settings</button>
			    </div>
			    -->
			    
  			    <!-- when clicked, it will load the add user form 
			    <div id='addSymptom'>
				<button type="button" class="btn btn-small btn-info">+ Symptom</button>
			    </div>
			    -->
			    <!-- when clicked, it will load the add user form 
			    <div id='addSchedule'>
				 <button type="button" class="btn btn-small btn-info">+ Schedule</button>
			    </div>
			    -->
			    <!-- this is the loader image, hidden at first -->
			    <div id='loaderImage'><img src='images/ajax-loader.gif' /></div>
    			    <div style='clear:both;'></div>
			</div>

			<!-- this is wher the contents will be shown. -->
			<div id='pageContent'></div>
</body>





<script type='text/javascript'>
	var entity = "symptoms";
  $(document).ready(function(){
    
    // VIEW USERS on load of the page
    $('#loaderImage').show();
    showUsers();
    
    // clicking the 'VIEW USERS' button
    $('#viewData').click(function(){
        // show a loader img
        $('#loaderImage').show();
        
        showUsers();
    });
    
});

// READ USERS
function showUsers(){
    // read and show the records after at least a second
    // we use setTimeout just to show the image loading effect when you have a very fast server
    // otherwise, you can just do: $('#pageContent').load('read.php', function(){ $('#loaderImage').hide(); });
    // THIS also hides the loader image
    setTimeout("$('#pageContent').load('"+ entity + "/showusers.php', function(){ $('#loaderImage').hide(); });", 1000);
}

</script>
