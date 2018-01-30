<?php
function login_page()
{
include('config.inc.php');
include('header.php');
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
                        <div id="contact-top">
                            <ul>
								<li><i class="icon-user"></i><a href="#">Sign Up!</a></li>
								
                            </ul>
                        </div>

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
			<br><br>
			
			<div align="center" class="row no-space">
			<div align="center" class="span3">
			</div>
			  <div align="center" class="span4 ">
			  <form align="left" class="form-signin" action="login.php" method="post">
					<h2 class="form-signin-heading">Sign in</h2>
					<input name="user" type="text" class="input-block-level" placeholder="Email address / username">
					<input name="pass" type="password" class="input-block-level" placeholder="Password">
					<!-- <label class="checkbox">
					  <input type="checkbox" value="remember-me"> Remember me
					</label>-->
					<br><br>
					<button class="btn  btn-inverse" name="submit" type="submit">Sign in</button>
				   
					<button class="btn  btn-mini pull-right" type="submit">Forget Password</button>
				</form>
			  </div>
			  <div align="center" class="span4">
			  <form align="center" class="form-signin" action="login.php" method="post">
					<hr>
					<h3 class="form-signin-heading">Register an Account</h3>
					<button class="btn  btn-danger" name="register" type="register">Sign Up!</button>
				</form>
			  </div>
			  <div align="center" class="span2">
			</div>
			</div>
			<br><br>
				
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
<?php
}

function forgot_password_page($mode)
{
include('config.inc.php');
?>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title><?php echo "$Site_Name" ?></title>
    <meta name="description" content="">
    <meta name="author" content="">
	<link rel="shortcut icon" href="icon.png">
    <!-- Le styles -->
    <link href="assets/css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
      /* Override some defaults */
      html, body {
		background: url('assets/img/backgrounds/5.jpg');
		background-repeat:no-repeat;
		background-attachment:fixed;
		background-position:center; 
      }
      body {
        padding-top: 40px; 
      }
      .container {
        width: 500px;
      }

      /* The white background content wrapper */
      .container > .content {

		background-color:rgba(255,255,255,0.9);
        padding: 20px;
        margin: 0 -20px; 
        -webkit-border-radius: 10px 10px 10px 10px;
           -moz-border-radius: 10px 10px 10px 10px;
                border-radius: 10px 10px 10px 10px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.15);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.15);
                box-shadow: 0 1px 2px rgba(0,0,0,.15);
      }

	  .login-form {
		margin-left: 165px;
	  }
	
	  legend {
		margin-right: -50px;
		font-weight: bold;
	  	color: #404040;
	  }

    </style>

</head>
<body>
  <div class="container">
  <br><br><br><br><br>
    <div class="content">
      <div class="row">
	  <h1 align="center"><img src="assets/img/nfl/AFC.gif" width="50px"> <?php echo "$Site_Name" ?> <img src="assets/img/nfl/NFC.gif" width="50px"></h1>
        <div class="login-form">
          <h3>Forgot Password</h3>
          <form action="forgot_password.php" method="post">
		  <?php
				if ($mode != "first") {
					if ($mode == "reset") {
						$type = "alert-success";
						$message = "An email was sent with a new password";
					} else {
						$type = "alert-error";
						$message = "Email address is not valid";
					}
				?>
				<div class="alert <?php echo "$type"; ?>">
					<a class="close" data-dismiss="alert" href="#">x</a><?php echo "$message"; ?>
				</div> 
				<?php 
				}
			?>
            <fieldset>
			<?php 
			  if ($mode == "reset") {
					echo "<button class='btn btn-success' name='login' type='submit'>Continue To Login</button>";
			  } else {
			  ?>
					<div class="clearfix">
					<input type="text" name="email" placeholder="Email Address">
					</div>
					<button class='btn btn-danger' name='submit' type='submit'>Reset Password</button>
			  <?php
			  }
			  ?>
            </fieldset>
          </form>
        </div>
      </div>
    </div>
  </div> <!-- /container -->
</body>
</html>
<?php
}

function randString($length, $charset='ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789')
{
    $str = '';
    $count = strlen($charset);
    while ($length--) {
        $str .= $charset[mt_rand(0, $count-1)];
    }
    return $str;
}
?>
