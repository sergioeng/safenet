<?php
include ("../SqlHelper.php");
$numRows = 0;
$sqlHelper = new SqlHelper;

//select all data
$query = "SELECT * FROM colaboradores ORDER BY username";
$sqlHelper->select($query);
$numRowsFound = $sqlHelper->numRows;

echo "Num rows foud=$numRowsFound";

//this is how to get number of rows returned
if ($numRowsFound > 0) {

    echo "<table id='tfhover' class='tftable' border='1'>";//start table
    
    //creating our table heading
    echo "<tr>";
        echo "<th>Usuario</th>";
        echo "<th>Senha</th>";
        echo "<th>Nome</th>";
        echo "<th>Tipo</th>";
        echo "<th>Medico</th>";
        echo "<th>Data Ultima Alteracao</th>";
        echo "<th style='text-align:center;'>Ação</th>";
    echo "</tr>";

        //retrieve our table contents
        //fetch() is faster than fetchAll()
        while ($numRows < $numRowsFound){
            $numRows = $numRows + 1;
            //extract row
            //this will make $row['firstname'] to
            //just $firstname only
	        $row =  $sqlHelper->nextRow();
			$id        = $row['colaborador_id'];
			$username  = $row['username'];
			$password  = $row['password'];
			$nome      = $row['nome'];
			$tipo      = $row['tipo'];
			$eh_medico = $row['eh_medico'];
			$data_alt  = $row['data_alt'];
            
            //creating new table row per record
            echo "<tr>";
                echo "<td>{$username}</td>";
                echo "<td>{$password}</td>";
                echo "<td>{$nome}</td>";
                echo "<td>{$tipo}</td>";
                echo "<td>{$eh_medico}</td>";
                echo "<td>{$data_alt}</td>";
                echo "<td style='text-align:center;'>";
					// add the record id here
					echo "<div class='dataId' style='display:none'>{$id}</div>";
					
                    //we will use this links on next part of this post
                    echo "<div class='editBtn btn btn-mini btn-warning'>EDT</div>";
					
                    //we will use this links on next part of this post
                    echo "<div class='deleteBtn btn btn-mini btn-danger'>RMV</div>";
                echo "</td>";
            echo "</tr>";
        }
        
    echo "</table>";//end table
    
}
else {
    echo "<div class='noneFound'>Não há colaboradores</div>";
}

?>
