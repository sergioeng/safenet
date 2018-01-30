<?php
session_start();
include_once("inc/auth.inc.php");
_already_logged($_COOKIE);

if(isset($_POST['forgot']))
{
	header("location: forgot_password.php");
}

if(isset($_POST['submit']))
{
//echo "inside of submit with ". $_POST['user'];
	$user_data = _check_database(fm($_POST['user']),fm($_POST['pass']));
	if($user_data == 0) {
	//echo "date = nothing";
		login_page();
	} else {
		_set_cookie($user_data,fm($_POST['rem']),session_id(),fm($_POST['user']));
	}
} 
else if (isset($_POST['guest'])) {
	$user_data = _insert_guest();
	//echo "data = $user_data";

	if($user_data != 0) {
		_set_cookie_guest($user_data,fm($_POST['rem']),session_id());
	}
} 
else if (isset($_POST['register'])) {
	header("location: register.php");
} 
else {
//echo "outside of submit";
	login_page();
}

?>
