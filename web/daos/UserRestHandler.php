<?php
require_once("SimpleRest.php");
require_once("User.php");
require_once("../tools/Logger.php");

class UserRestHandler extends SimpleRest {
	
	
	function __construct() {
		Logger::setFilename ("../logs/user.log");
	}

	
	public function delete ($id) {

		$log = Logger::getInstance();
        $log->info ("UserRestHandler.php: delete ($id)");

		$user = new User();
		$user->delete ($id);
		
	}
	
	function write ($userJson, $rowId) {
        $type       = $userJson['TYPE'];
        $phone_num  = $userJson['PHONE_NUM'];
        $name       = $userJson['NAME'];
        $imei       = $userJson['IMEI'];
        
		$log = Logger::getInstance();
        $log->info ("UserRestHandler.php: add - NAME =[$name]");
        $log->info ("UserRestHandler.php: add - PHONE=[$phone_num]");
        $log->info ("UserRestHandler.php: add - IMEI =[$imei]");
        $log->info ("UserRestHandler.php: add - TYPE =[$type]");

		$user = new User();
        $user->setName ($name);
        $user->setPhone ($phone_num);
        $user->setImei ($imei);
        $user->setType ($type);
        
        if ($rowId == 0) {
        	$user->add();
        }
        else {
        	$user->update($rowId);
        }
	}
	 
	function getAllUsers() {	

		$user = new User();
		$rawData = $user->getAllUsers();
		
		$log = Logger::getInstance();
        $log->info ("UserRestHandler.php: getAllUsers () rawData=[$rawData]");

		if(empty($rawData)) {
			$statusCode = 404;
			$rawData = array('error' => 'No data found.');		
		} else {
			$statusCode = 200;
		}

		$requestContentType = 'application/json';
		$this->setHttpHeaders($requestContentType, $statusCode);
				
		$result["output"] = $rawData;
				
		if(strpos($requestContentType,'application/json') !== false){
			$response = $this->encodeJson($result);
			$log->info  ("UserRestHandler.php: response=[$response]");
	
			return $response;
		} 
	}
	
	
	public function encodeJson($responseData) {
		$jsonResponse = json_encode($responseData);
		return $jsonResponse;		
	}
	
	
	public function getUser($id) {

		$user = new User();
		$rawData = $user->getUser($id);

		$log = Logger::getInstance();
        $log->info ("UserRestHandler.php: getUser($id) rawData=[$rawData]");
		
		if(empty($rawData)) {
			$statusCode = 404;
			$rawData = array('error' => 'No record found.');		
		} else {
			$statusCode = 200;
		}

		$requestContentType = 'application/json';
		$this->setHttpHeaders($requestContentType, $statusCode);
				
		$result["output"] = $rawData;
		
		if(strpos($requestContentType,'application/json') !== false){
			$response = $this->encodeJson($result);
			return $response;
		}
	}
	
}
?>
