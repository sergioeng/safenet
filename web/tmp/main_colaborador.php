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

                        <!-- Contact Details -->
 						<!-- TO BE DELETED by sergioeng 
                        <div id="contact-top">
                            <ul>
								<li><i class="icon-user"></i><a href="#">Sign Up!</a></li>
								
                            </ul>
                        </div>
						-->
                    </div>

                </div>
                <!-- Header / End -->

                <!-- Navigation -->
                <?php
					include("navbar.php");
				?>
                <div class="clear"></div>

            </div>
            <!-- Navigation / End -->
			
			
			<!-- Content -->
			<div align="left" class="row-fluid">
			<br><br>
<!-- SERGIO BEGIN-->			
			<div style='margin:0 0 .5em 0;'>
			    <!-- when clicked, it will show the user's list -->
			    <div id='viewData'> 
					<button type="button" class="btn btn-small btn-info">Colaboradores</button>
				</div>
			    <!-- when clicked, it will load the add user form -->
			    <div id='addUser'>
					<button type="button" class="btn btn-small btn-info">+ Novo Colaborador</button>
				</div>
    
			    <!-- this is the loader image, hidden at first -->
			    <div id='loaderImage'><img src='images/ajax-loader.gif' /></div>
    
			    <div style='clear:both;'></div>
			</div>

			<!-- this is wher the contents will be shown. -->
			<div id='pageContent'></div>

<script type='text/javascript'>
	var entity = "colaborador";

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
    
    // clicking the '+ NEW DADO' button
    $('#addUser').click(function(){
        showCreateUserForm();
    });

    // clicking the EDIT button
    $(document).on('click', '.editBtn', function(){ 
    
        var data_id = $(this).closest('td').find('.dataId').text();
        console.log(data_id);
        
        // show a loader image
        $('#loaderImage').show();

        // read and show the records after 1 second
        // we use setTimeout just to show the image loading effect when you have a very fast server
        // otherwise, you can just do: $('#pageContent').load('update_form.php?data_id=" + data_id + "', function(){ $('#loaderImage').hide(); });
        setTimeout("$('#pageContent').load('" + entity +"/update_form.php?data_id=" + data_id + "', function(){ $('#loaderImage').hide(); });",1000);
        
    }); 
    
    
    // when clicking the DELETE button
    $(document).on('click', '.deleteBtn', function(){ 
        if(confirm('Voce tem certeza?')){
        
            // get the id
            var data_id = $(this).closest('td').find('.dataId').text();
            
            // trigger the delete file
            $.post(entity+"/delete.php", { id: data_id })
                .done(function(data) {
                    // you can see your console to verify if record was deleted
                    console.log(data);
                    
                    $('#loaderImage').show();
                    
                    // reload the list
                    showUsers();
                    
                });

        }
    });
    
    
    // CREATE FORM IS SUBMITTED
     $(document).on('submit', '#addUserForm', function() {

        // show a loader img
        $('#loaderImage').show();
        
        // post the data from the form
        $.post(entity +"/create.php", $(this).serialize())
            .done(function(data) {
                // 'data' is the text returned, you can do any conditions based on that
                showUsers();
            });
                
        return false;
    });
    
    // UPDATE FORM IS SUBMITTED
     $(document).on('submit', '#updateDataForm', function() {

console.log ("update data");
        // show a loader img
        $('#loaderImage').show();
        
        // post the data from the form
        $.post(entity +"/update.php", $(this).serialize())
            .done(function(data) {
                // 'data' is the text returned, you can do any conditions based on that
                showUsers();
            });
                
        return false;
    });
    
});

// READ USERS
function showUsers(){
    // read and show the records after at least a second
    // we use setTimeout just to show the image loading effect when you have a very fast server
    // otherwise, you can just do: $('#pageContent').load('read.php', function(){ $('#loaderImage').hide(); });
    // THIS also hides the loader image
    setTimeout("$('#pageContent').load('"+ entity +"/read.php', function(){ $('#loaderImage').hide(); });", 1000);
}

// CREATE USER FORM
function showCreateUserForm(){
    // show a loader image
    $('#loaderImage').show();
    
    // read and show the records after 1 second
    // we use setTimeout just to show the image loading effect when you have a very fast server
    // otherwise, you can just do: $('#pageContent').load('read.php');
    setTimeout("$('#pageContent').load('"+ entity +"/create_form.php', function(){ $('#loaderImage').hide(); });",1000);
}
</script>
<!-- SERGIO END -->			

            <!-- Content continues -
			<form align="left" class="form-signin" action="colaboradores.php" method="post">
			</form>
			-->
		</div>
		<br>
				
            <!-- Blog  End -->
        </div>
        <!-- Wrapper / End -->

        <!-- Footer -->

        <!-- Footer Top -->
       <?php
		include("topfooter.php");
	   ?>
        <!-- Footer / Bottom -->
	<?php
		include("footer.php");
	?>

        <!-- Footer / End -->
    </body>

</html>
