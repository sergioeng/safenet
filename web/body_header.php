<!-- Header -->
<div class="ie-dropdown-fix" >

    <!-- Header -->
    <div class="row-fluid" id="header">

	<!-- Logo -->
	<div class="span5"> <a href="index.php"><img src="images/logo.png" alt="logo" /></a> </div>
	<!-- Social / Contact -->
	<div class="span4 pull-right">
	    <!-- Social Icons -->
	    <ul class="social-icons">
		<?php 
		    if ($filename == "about") {
			echo "<li class='facebook'><a href='#'>Facebook</a></li>";
			echo "<li class='twitter'><a href='#'>Twitter</a></li>";
			echo "<li class='linkedin'><a href='#'>LinkedIn</a></li>";
			echo "<li class='googleplus'><a href='#'>Google+</a></li>";
		    }
		?>
	    </ul>

	    <!-- Contact Details -->
	    <div id="contact-top">
		<ul>
		    <?php if ($filename == "about") {	?>
			      <li><i class="icon-envelope"></i><a href="mailto:sro.eng@gmail.com">sro.eng@gmail.com</a></li>
		    <?php } else if ($user == NULL) { ?>
			      <li><i class="icon-user"></i><a href="login.php">Login</a></li>
			      <li><i class="icon-wrench"></i><a href="register.php">Sign Up!</a></li>
		    <?php } else { ?>
			      <!--<li><i class="icon-wrench"></i><a href="#">Account Setting</a></li>-->
			      <li><i class="icon-user"></i><?php echo "$name"; ?></li>
			      <li><i class="icon-minus"></i><a href="logout.php">Logout</a></li>
		    <?php } ?>
					
		</ul>
	    </div>
	</div>
    </div>
    <!-- Header / End -->

    <!-- Navigation -->
    <?php include("navbar.php"); ?>
    <div class="clear"></div>

</div>
<!-- Navigation / End -->
