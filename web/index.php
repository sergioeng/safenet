<?php
	session_start();
	include_once("inc/auth.inc.php");

	$user = _check_auth($_COOKIE, false);
	$filename = basename(__FILE__, '.php');
//echo "index.php => filename=[$filename]";

	$name = $user['username'];
	$userID = $user['colaborador_id'];
/*
$_SESSION['user_id'] = $userID;
*/
	include("header.php");
?>
    <body>

        <!-- Wrapper Start -->
        <div id="wrapper" class="container-fluid">
		
        <?php include("body_header.php"); ?>

        <!-- Content -->
	<div class="row-fluid">
	    <div class="container-narrow">
		<div class="jumbotron">
		    <h1>Settings</h1>
		    <p class="lead">bla-bla-bla</p>
		    <!--a class="btn btn-large btn-success" href="#">Sign Up</a> -->
		    <!-- <a class="btn btn-large" href="classictrivia.php">Play Now!</a> -->
		</div> <!-- end of jumbotron -->
	    </div> <!-- end of container-narrow -->
	</div> <!-- end of row-fluid -->

        <!-- Features Start -->
        <!-- Features #nd -->
			
        <!-- Blog  End -->
        </div>
        <!-- Wrapper / End -->

        <!-- Footer -->

	<?php include("footer.php"); ?>

        <!-- Footer / End -->
    </body>

</html>
