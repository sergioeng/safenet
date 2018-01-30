<?php

/* ===============================
== Design by DodsonEng
== Bible Trivia
== Email: DodsonEng@gmail.com
================================*/

include_once("config.inc.php");
include_once("layout.inc.php");
include_once("global.inc.php");
include ("SqlHelper.php");

// Create a SqlPestCloudHelper object
$sqlHelper = new SqlHelper;
 
// check if the username and password are correct
function _check_database($user, $pass)
{
	$user = mysql_real_escape_string($user);
	$pass = mysql_real_escape_string($pass);
	//$hash = crypt($pass, '44A44B');
	$sqlHelper = new SqlHelper;
	$sqlHelper->select("SELECT colaborador_id FROM colaboradores WHERE username = '$user' AND password = '$pass'");
	
	if ($sqlHelper->numRows == 0) {
		return 0;
	} else {
		$row = $sqlHelper->nextRow();
		return $row;
	}
}

/*
// insert a new guest into the databse
function _insert_guest()
{
	$zero = 0;
	$date = new DateTime();
	$sqlHelper = new SqlHelper;
	$success = $sqlHelper->insert("INSERT INTO guests (gstTotalScore, gstGamesPlayed, gstInsertDate) VALUES ($zero, $zero, CURDATE())");
	
	if ($success != 0) {
		$guestID = $sqlHelper->insertID;
	}

	
	return $guestID;
}
*/
// check if email address exists
function _check_user($email)
{
  $email = mysql_real_escape_string($email);
  if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    $email = "0";
  }
  $Q = mysql_query("SELECT colaborador_id FROM colaboradores WHERE btUser = '$email'");
  if (mysql_num_rows($Q) == 0) 
  	return 0;
  
  return mysql_fetch_array($Q);
}

// password reset feature
function _reset_password($email)
{
  $email = mysql_real_escape_string($email);
  if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    $email = "0";
  }
  $newPass = randString(10);
  //$hash = crypt($newPass, '44A44B');
  $Q = mysql_query(" UPDATE colaboradores SET user_pass = '$pass' WHERE user_email = '$email' ");
  
  $from = "groversurvivorpool@gmail.com";
  $headers = "From:" . $from;
  $subject = "Grover Survivor Pool Password Reset";
  $message = "Your new password for the Grover Survivor Pool is $newPass.";
  // The mail sending has been commented out.  it needs to be put back in when the code is ready for it.
  mail($email,$subject,$message,$headers);
}
 
function _check_auth($cookie, $loginRequired=true)
{
	$user = array();
	$session = $cookie['SESSION'];
	
	$query = "SELECT s.user_id, u.btUser as userName FROM sessions s, users u WHERE s.user_id = u.btIndex and session = '".$session."' ";
	$query = "SELECT s.colaborador_id, c.username FROM sessions s, colaboradores c WHERE s.colaborador_id = c.colaborador_id and session = '".$session."' ";

	$sqlHelper = new SqlHelper;
	$sqlHelper->select($query);

	if ($sqlHelper->numRows == 0 && $loginRequired) {
    	header("Location: login.php");
	} 
	else if ($sqlHelper->numRows == 0 && !$loginRequired) {		
		$user = NULL;
	} 
	else {
		$row = $sqlHelper->nextRow();
		$user['colaborador_id'] = $row['colaborador_id'];
		$user['username'] = $row['username'];
	}

	return $user;
}

function _set_cookie($user_data, $rem, $session, $username)
{
	if ($rem == 1) 
		setcookie('SESSION', $session, time() + 186400);
	else 
		setcookie('SESSION', $session);
	
	$colaborador_id = $user_data['colaborador_id'];
	$sqlHelper = new SqlHelper;

	$query = "SELECT * FROM sessions WHERE colaborador_id = '$colaborador_id' AND username = '$username' ";
	$sqlHelper->select($query);

	if ($sqlHelper->numRows == 0) {
		$sqlHelper->insert("INSERT INTO sessions (session, colaborador_id, username) VALUES ('$session','$colaborador_id','$username') ");
	} else {
		$sqlHelper->update("UPDATE sessions SET session = '$session' WHERE colaborador_id = '$colaborador_id' AND user_email = '$username'");
	}
	header("location: index.php");
}

/*
function _set_cookie_guest($user_data, $rem, $session)
{
	if ($rem == 1) 
		setcookie('SESSION', $session, time() + 186400);
	else 
		setcookie('SESSION', $session);
	
	$colaborador_id = $user_data;
	$username = "Guest" . $colaborador_id;
	$sqlHelper = new SqlHelper;

	$query = "SELECT * FROM sessions WHERE user_id = '$colaborador_id' AND user_email = '$username' ";
	$sqlHelper->select($query);

	if ($sqlHelper->numRows == 0) {
		$sqlHelper->insert("INSERT INTO sessions (session, user_id, user_email) VALUES ('$session','$colaborador_id','$username') ");
	} else {
		$sqlHelper->update("UPDATE sessions SET session = '$session' WHERE user_id = '$colaborador_id' AND user_email = '$username'");
	}
	header("location: index.php");
}
*/
function _logout_user($cookie)
{
	$session = $cookie['SESSION'];
 
	$sqlHelper = new SqlHelper;
	$sqlHelper->delete("DELETE FROM sessions WHERE session = '$session' ");
	setcookie('SESSION', '', 0);
	header("location: login.php");
}

function _logout_user_expires($cookie)
{
	$session = $cookie['SESSION'];
	$sqlHelper = new SqlHelper;
	$sqlHelper->delete("DELETE FROM sessions WHERE session = '$session' ");
	setcookie('SESSION', '', 0);
	header("location: loginexpires.php");
}

function _already_logged($cookie)
{
	if (isset($cookie['SESSION']))
		$session = $cookie['SESSION'];

	//$session = mysql_real_escape_string($session);
//	$q = mysql_query(" SELECT s.user_id, s.user_email, u.btUser FROM sessions s, colaboradores c WHERE s.user_id = u.btIndex and session = '".$session."' ")
	$q = mysql_query(" SELECT s.colaborador_id, s.username, c.username FROM sessions s, colaboradores c WHERE s.colaborador_id = c.colaborador_id and session = '".$session."' ")
		or die(mysql_error());

	if (mysql_num_rows($q) != 0) {
		header("location: index.php");
	}
}

function fm($String)
{
	return addslashes(strip_tags($String));
}
?>
