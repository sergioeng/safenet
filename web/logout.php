<?php
include_once("inc/auth.inc.php");
$user = _check_auth($_COOKIE);
_logout_user($_COOKIE);
?>