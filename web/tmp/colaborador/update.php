<?php
include ("../SqlDavance.php");
$useLog = 1;
$numRows = 0;
$sqlHelper = new SqlDavanceHelper;



try{
	$file_log = fopen("../logs/update.txt", "w");
    fwrite ($file_log, "--- LOG BEGUN -----------------------\n");

	//write query
	//in this case, it seemed like we have so many fields to pass and 
	//its kinda better if we'll label them and not use question marks
	//like what we used here
	$colaborador_id =  $_POST['id'];
	$username  = $_POST['username'];
	$password  = $_POST['password'];
	$nome      = $_POST['nome'];
	$tipo      = $_POST['tipo'];
	$eh_medico = $_POST['eh_medico'];
	$endereco  = $_POST['endereco'];
	$cidade    = $_POST['cidade'];
	$estado    = $_POST['estado'];
	$cep       = $_POST['cep'];
	$tel_fix   = $_POST['tel_fix'];
	$tel_cel   = $_POST['tel_cel'];
	$email     = $_POST['email'];
	$data_nas  = $_POST['data_nas'];
	$data_alt  = $_POST['data_alt'];

	$log = "LOG> username=[$username]\n";
   fwrite ($file_log, $log);
	$log = "LOG> password=[$password]\n";
   fwrite ($file_log, $log);
	$log = "LOG> nome=[$nome]\n";
   fwrite ($file_log, $log);
	$log = "LOG> colaborador_id=[$colaborador_id]\n";
   fwrite ($file_log, $log);

	$query = "UPDATE colaboradores SET 
username='$username', 
password='$password', 
nome='$nome', 
tipo='$tipo',
eh_medico='$eh_medico',
endereco='$endereco',
cidade='$cidade',
estado='$estado',
cep='$cep',
tel_fix='$tel_fix',
tel_cel='$tel_cel',
email='$email',
data_nas='$data_nas',
data_alt='$data_alt' 
WHERE colaborador_id=$colaborador_id";


	$ret = $sqlHelper->update ($query);

    $log = "LOG> query=[$query]\n";
    fwrite ($file_log, $log);
    $log = "LOG> retorno=$ret\n";
	fwrite ($file_log, $log);
	
	// Execute the query
	if($ret == 1){
		echo "User was updated. ret=$ret";
	}else{
		echo "Unable to update user. ret=$ret";
	}

}

//to handle error
catch(Exception $exception){
	echo "Error: " . $exception->getMessage();
}
?>
