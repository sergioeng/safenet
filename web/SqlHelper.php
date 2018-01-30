<?php

include ("Common/SQL/SqlClasses.php");

//------------------------------------------------------------------------
// PEST CLOUD DATABASE INFO
//------------------------------------------------------------------------

class SqlDbInfo extends SqlAbsDbInfo {
	
	public function __construct()
	{
		$this->HOST_NAME     = 'safenet.mysql.dbaas.com.br';
		$this->DATABASE_NAME = 'safenet';
		$this->DB_USERNAME   = 'safenet';
		$this->DB_PASSWORD   = 'pinico18';
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
