<?php
class DBController {
	private $conn = "";
	private $host = "safenet.mysql.dbaas.com.br";
	private $user = "safenet";
	private $password = "pinico18";
	private $database = "safenet";
		
	function __construct() {
		$conn = $this->connectDB();
		if(!empty($conn)) {
			$this->conn = $conn;			
		}
	}

	function connectDB() {
		$conn = mysqli_connect($this->host,$this->user,$this->password,$this->database);
		return $conn;
	}

	function executeSelectQuery($query) {
		$result = mysqli_query($this->conn, $query);
		while($row=mysqli_fetch_assoc($result)) {
			$resultset[] = $row;
		}
		if(!empty($resultset))
			return $resultset;
	}
	
	function executeQuery ($query) {
		
		// For other type of SQL statements, INSERT, UPDATE, DELETE, DROP, etc, 
		// mysql_query() returns TRUE on success or FALSE on error.
		$result = mysqli_query($this->conn, $query);

		if (! $result)
		{
			echo mysql_error();
		}

		return $result;
	}
}
?>
