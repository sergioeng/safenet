<?php
require_once("../daos/UserRestHandler.php");
require_once("../tools/Logger.php");

$view = "";
if(isset($_GET["view"]))
	$view = $_GET["view"];

Logger::setFilename ("../logs/user.log");
$log = Logger::getInstance();
$log->info ("UsersController.php: VIEW=['$view']");

switch($view){
	case "all":
		all ();
		break;

	case "add":
		add ();
		break;

	case "del":
		delete ($_GET["id"]);
		break;
		
	case "update":
		update ($_GET["id"]);
		break;
		
	case "single":
		// to handle REST Url /mobile/show/<id>/
		$userRestHandler = new UserRestHandler();
		$userRestHandler->getUser($_GET["id"]);
		break;

	case "" :
		//404 - not found;
		break;
}


function add () {

    $log = Logger::getInstance();

    $userJson = json_decode(file_get_contents('php://input'), true);
    $log->info ("UsersController.php: add () - JSON =['$userJson']");
	        
  	$userRestHandler = new UserRestHandler();
    $userRestHandler->write ($userJson, 0);
	
}

function delete ($rowId) {

    $log = Logger::getInstance();
    $log->info ("UsersController.php: del () - row_id=['$rowId']");

  	$userRestHandler = new UserRestHandler();
    $userRestHandler->delete ($rowId);
    
}

function update ($rowId) {

    $log = Logger::getInstance();
    $log->info ("UsersController.php: update () - row_id=['$rowId']");
    
    $userJson = json_decode(file_get_contents('php://input'), true);
    $log->info ("UsersController.php:  () - JSON =['$userJson']");

    $userRestHandler = new UserRestHandler();
    $userRestHandler->write ($userJson, $rowId);
    
}

function all () {

    $log = Logger::getInstance();

	$userRestHandler = new UserRestHandler();
	$userJson = $userRestHandler->getAllUsers();
    
    echo $userJson;

}

?>
