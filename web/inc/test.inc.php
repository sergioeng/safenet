<?php
include_once("config.inc.php");

mysql_connect($CONFIG['HOST_NAME'], $CONFIG['DB_USERNAME'], $CONFIG['DB_PASSWORD'])
  or die(mysql_error());
mysql_select_db($CONFIG['DATABASE_NAME'])
  or die(mysql_error());

function _check_database($user, $pass)
{
  $Q = mysql_query("SELECT user_id FROM users WHERE user_email = '$user' AND user_pass = '$pass'");
  if(mysql_num_rows($Q) == 0) return 0;
  else return mysql_fetch_array($Q);
}
 
function _check_auth($cookie)
{
  $user = array();
  $session = $cookie['SESSION'];

  $q = mysql_query(" SELECT user_id, user_email FROM sessions WHERE session = '".$session."' ")
    or die(mysql_error());
  if(mysql_num_rows($q) == 0)
    header("Location: login.php");
  else {
    $r = mysql_fetch_array($q);
    $user['user_id']   = $r['user_id'];
    $user['user_email'] = $r['user_email'];
  }
  return $user;
}

function _set_cookie($user_data, $rem, $session, $username)
{
  if($rem == 1) setcookie('SESSION', $session, time() + 186400);
  else setcookie('SESSION', $session);

  $user_id = $user_data['user_id'];
  $C = mysql_query(" SELECT * FROM sessions WHERE user_id = '$user_id' AND user_email = '$username' ");
  if(mysql_num_rows($C) == 0)
    $Q = mysql_query(" INSERT INTO sessions (session, user_id, user_email) VALUES ('$session','$user_id','$username') ");
  else
    $Q = mysql_query(" UPDATE sessions SET session = '$session' WHERE user_id = '$user_id' AND user_email = '$username' ");
  header("location: index.php");
}

function _logout_user($cookie)
{
  $session = $cookie['SESSION'];
  $q = mysql_query(" DELETE FROM sessions WHERE session = '$session' ")
    or die(mysql_error());
  setcookie('SESSION', '', 0);
  header("location: login.php");
}

function _logout_user_expires($cookie)
{
  $session = $cookie['SESSION'];
  $q = mysql_query(" DELETE FROM sessions WHERE session = '$session' ")
    or die(mysql_error());
  setcookie('SESSION', '', 0);
  header("location: loginexpires.php");
}

function _already_logged($cookie)
{
  if(isset($cookie['SESSION']))
    header("location: index.php");
}

function fm($String)
{
  return addslashes(strip_tags($String));
}
?>