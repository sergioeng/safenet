<?php

//------------------------------------------------------------------------
// DATABASE INFO ABSTRACT CLASS
//------------------------------------------------------------------------

abstract class SqlAbsDbInfo {
	public $HOST_NAME;
	public $DATABASE_NAME;
	public $DB_USERNAME;
	public $DB_PASSWORD;
}

//------------------------------------------------------------------------
// SQL HELPER ABSTRACT CLASS
//------------------------------------------------------------------------

abstract class SqlAbsHelper {

	public $result;
	public $numRows;
	public $row;
	public $connected = false;
	public $insertID;
	private $dbInfo;


	public function __construct($dbInfo)
	{
		$this->connect($dbInfo);
	}

	//------------------------------------------------------------------------
	// DATABASE CONNECTION
	//------------------------------------------------------------------------

	public function connect($dbInfo)
	{
		$this->dbInfo = $dbInfo;
		if (!mysql_connect($dbInfo->HOST_NAME, $dbInfo->DB_USERNAME, $dbInfo->DB_PASSWORD))
		{
			die('Not connected : ' . mysql_error());
		}
		if (!mysql_select_db($dbInfo->DATABASE_NAME))
		{
			die('Can\'t select $dbInfo->DATABASE_NAME : ' . mysql_error());
		}
		$this->connected = true;
	}

	public function disconnect()
	{
		if ($this->isConnected())
		{
			mysql_close();
			$this->connected = false;
		}
	}

	public function isConnected()
	{
		return $this->connected;
	}

	//------------------------------------------------------------------------
	// SELECT, SHOW, DESCRIBE, EXPLAIN
	//------------------------------------------------------------------------

	public function select($query)
	{
		$this->get($query);
	}
	
	public function nextRow()
	{
		//if (!empty($this->result))
		{
			// The returned result resource should be passed to mysql_fetch_array(), and other
			// functions for dealing with result tables, to access the returned data.
			$this->row = mysql_fetch_array($this->result);
		}

		return $this->row;
	}

	//------------------------------------------------------------------------
	// INSERT, UPDATE, DELETE, DROP
	//------------------------------------------------------------------------	

	public function insert($query)
	{
		return $this->put($query);
	}

	public function update($query)
	{
		return $this->put($query);
	}

	public function delete($query)
	{
		return $this->put($query);
	}

	public function drop($query)
	{
		return $this->put($query);
	}	


	//------------------------------------------------------------------------
	// PRIVATE FUNCTIONS
	//------------------------------------------------------------------------

	private function get($query)
	{
		// Initialize vars
		$this->result  = 0;
		$this->numRows = 0;
		$this->row     = 0;

		// If not connected, connect
		if (!$this->isConnected())
		{
			$this->connect($this->dbInfo);
		}

		// For SELECT, SHOW, DESCRIBE, EXPLAIN and other statements returning resultset, 
		// mysql_query() returns a resource on success, or FALSE on error.
		$this->result  = mysql_query($query);

		if (!empty($this->result))
		{
			// Use mysql_num_rows() to find out how many rows were returned for a 
			// SELECT statement.
			$this->numRows = mysql_numrows($this->result);
		}
		else
		{
			echo mysql_error();
		}

		$this->disconnect();
	}

	private function put($query)
	{
		// Initialize vars
		$this->result  = 0;
		$this->numRows = 0;
		$this->row     = 0;
		$this->insertID = 0;

		// If not connected, connect
		if (!$this->isConnected())
		{
			$this->connect($this->dbInfo);
		}	

		// For other type of SQL statements, INSERT, UPDATE, DELETE, DROP, etc, 
		// mysql_query() returns TRUE on success or FALSE on error.
		$this->result = mysql_query($query);

		if (!empty($this->result))
		{
			// Use mysql_affected_rows() to find out how many rows were affected by a 
			// DELETE, INSERT, REPLACE, or UPDATE statement.
			$this->numRows = mysql_affected_rows();
			$this->insertID = mysql_insert_id();
		}

		if (!$this->result)
		{
			echo mysql_error();
		}

		$this->disconnect();

		return $this->result;
	}
}
?>