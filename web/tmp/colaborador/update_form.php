<?php
include ("../SqlDavance.php");
$numRows = 0;
$sqlHelper = new SqlDavanceHelper;

/*
'colaborador_id' 
'username'
'password' 
'nome'
'tipo'
'eh_medico'
'endereco' 
'cidade' 
'estado'
'cep'
'tel_fix' 
'tel_cel' 
'email' 
'data_nas'
'data_alt' 
*/

try {
	
	$colaborador_id =  $_REQUEST['data_id'];
	
	//prepare query
	$query = "SELECT * FROM colaboradores WHERE colaborador_id=$colaborador_id";
	$sqlHelper->select($query);
	$numRowsFound = $sqlHelper->numRows;

	//execute our query
	if ($numRowsFound) {
	
		//store retrieved row to a variable
		$row = $sqlHelper->nextRow();

		//values to fill up our form
		$id        = $row['colaborador_id'];
		$username  = $row['username'];
		$password  = $row['password'];
		$nome      = $row['nome'];
		$tipo      = $row['tipo'];
		$eh_medico = $row['eh_medico'];
		$endereco  = $row['endereco'];
		$cidade    = $row['cidade'];
		$estado    = $row['estado'];
		$cep       = $row['cep'];
		$tel_fix   = $row['tel_fix'];
		$tel_cel   = $row['tel_cel'];
		$email     = $row['email'];
		$data_nas  = $row['data_nas'];
		$data_alt  = $row['data_alt'];
			
	}else{
		echo "Colaborador nao encontrado";
	}
}

//to handle error
catch(PDOException $exception){
	echo "Error: " . $exception->getMessage();
}
?>
<!--we have our html form here where new user information will be entered-->
<form id='updateDataForm' action='#' method='post' border='0'>
    <table>
        <tr>
            <td>Username</td>
            <td><input type='text' name='username'  value='<?php echo $username;  ?>' required /></td>
        </tr>
        <tr>
            <td>Password</td>
            <td><input type='password' name='password' value='<?php echo $password;  ?>' required/></td>
        <tr>
        <tr>
            <td>Nome</td>
            <td><input type='text' name='nome' value='<?php echo $nome; ?>' required /></td>
        </tr>
        <tr>
	        <td>Tipo</td>
	        <td >
	        <?php
	        if ($tipo == 'S') {
	        ?>
				<input type='radio' name='tipo' value='S' checked="checked" required />Socio
				<input type='radio' name='tipo' value='F' required />Funcionario
	        <?php
			}
			else {
	        ?>
				<input type='radio' name='tipo' value='S' required />Socio
				<input type='radio' name='tipo' value='F' checked="checked" required />Funcionario
	        <?php
			}
	        ?>
	        </td>
        </tr>
        <tr>
	        <td>Médico</td>
	        <td style="padding: 5px;">
	        <?php
	        if ($eh_medico == 'S') {
	        ?>
				<input type='radio' name='eh_medico' value='S' checked="checked" required />Sim
				<input type='radio' name='eh_medico' value='N' required />Nao
	        <?php
			}
			else {
	        ?>
				<input type='radio' name='eh_medico' value='S' required />Sim
				<input type='radio' name='eh_medico' value='N' checked="checked" required />Nao
	        <?php
			}
	        ?>
	        </td>
		</tr>
		<tr>
	        <td>Endereco</td>
	        <td><input type='text' name='endereco' value='<?php echo $endereco; ?>' required /></td>
		</tr>
		<tr>
	        <td>Cidade</td>
	        <td><input type='text' name='cidade' value='<?php echo $cidade; ?>' required /></td>
	        <td>Estado</td>
	        <td><input type='text' name='estado' value='<?php echo $estado; ?>' required /></td>
	        <td>CEP</td>
	        <td><input type='text' name='cep' value='<?php echo $cep; ?>' required /></td>
		</tr>
		<tr>
	        <td>Telefone Fixo</td>
	        <td><input type='text' name='tel_fix' value='<?php echo $tel_fix; ?>' required /></td>
	        <td>Celular</td>
	        <td><input type='text' name='tel_cel' value='<?php echo $tel_cel; ?>' required /></td>
	        <td>email</td>
	        <td><input type='text' name='email' value='<?php echo $email; ?>' required /></td>
		</tr>
		<tr>
	        <td>Nascimento</td>
	        <td><input type='date' name='data_nas' value='<?php echo $data_nas; ?>' required /></td>
	        <td>Ultima alteracao</td>
	        <td><input type='date' name='data_alt' value='<?php echo $data_alt; ?>' required /></td>
		</tr>
		<tr>
            <td></td>
            <td>
                <!-- so that we could identify what record is to be updated -->
                <input type='hidden' name='id' value='<?php echo $id ?>' />
                
                <input type='submit' value='Update' class='btn btn-small btn-inverse' />
				
            </td>
        </tr>
    </table>
</form>
