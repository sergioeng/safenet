<?php
require_once("../tools/DBController.php");

/* 
A domain Class to demonstrate RESTful web services
http://phppot.com/php/php-restful-web-service/
http://phppot.com/php/rest-api-search-implementation-in-php/
*/
Class User {
	
	private $users = array();
    private $name = "AA";
    private $type = "X";
    private $phone = "123";
    private $imei = "456";		
	
    function __construct() {
		Logger::setFilename ("../logs/user.log");
	}
    

	public function add () {
	
	    $query = "INSERT INTO USERS (name, phone_num, imei, pat_or_gua) VALUES
	          ('$this->name','$this->phone', '$this->imei', '$this->type') ";

		$dbcontroller = new DBController();
		$ret = $dbcontroller->executeQuery($query);

		$log = Logger::getInstance();
		$log->info ("User.php: add - ret=[$ret] query=[$query]");
		
	}

	public function delete ($id) {
	
	    $query = "DELETE FROM USERS WHERE id=$id";
	    
		$dbcontroller = new DBController();
		$ret = $dbcontroller->executeQuery($query);

		$log = Logger::getInstance();
		$log->info ("User.php: delete - ret=[$ret] query=[$query]");
	
	}
	
	public function getAllUsers() {
		$query = "SELECT * FROM USERS ORDER BY name";

		$dbcontroller = new DBController();
		$this->users = $dbcontroller->executeSelectQuery($query);
		
		return $this->users;
	}
	
	public function getUser($id){
		
		//$user = array($id => ($this->users[$id]) ? $this->users[$id] : $this->users[1]);
		//return $user;
		
		$query = "SELECT * FROM USERS WHERE ID=$id";

		$dbcontroller = new DBController();
		$user = $dbcontroller->executeSelectQuery($query);
		
		return $user;
		
	}
	
	public function setIMEI ($imei) {
	    $this->imei = $imei;
	}	
	
	public function setName ($name) {
	    $this->name = $name;
	}

	public function setPhone ($phone) {
	    $this->phone = $phone;
	}
	
	public function setType ($type) {
	    $this->type = $type;
	    if ($type == "g") {
	       $this->type = 0;
	    }
    	else {
	       $this->type = 1;
    	}
	}

	public function update ($id) {
	
	    $query = "UPDATE USERS SET name='$this->name', phone_num='$this->phone', imei='$this->imei', pat_or_gua='$this->type' WHERE id=$id";

		$dbcontroller = new DBController();
		$ret = $dbcontroller->executeQuery($query);

		$log = Logger::getInstance();
		$log->info ("User.php: update - ret=[$ret] query=[$query]");
		
	}

}
?>
