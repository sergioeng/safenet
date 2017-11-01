<?php

include ("Common/SQL/SqlClasses.php");

//------------------------------------------------------------------------
// PEST CLOUD DATABASE INFO
//------------------------------------------------------------------------

class SqlDbInfo extends SqlAbsDbInfo {
	
	public function __construct()
	{
		$this->HOST_NAME     = 'localhost';
		$this->DATABASE_NAME = 'safenet';
		$this->DB_USERNAME   = 'root';
		$this->DB_PASSWORD   = 'modfrag';
	}
}

//------------------------------------------------------------------------
// PEST CLOUD SQL HELPER
//------------------------------------------------------------------------

class SqlHelper extends SqlAbsHelper {

	public function __construct()
	{
		parent::__construct(new SqlDbInfo);
	}
}

?>
