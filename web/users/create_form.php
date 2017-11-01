<!--
	-we have our html form here where user information will be entered
	-we used the 'required' html5 property to prevent empty fields
-->
<form id='addUserForm' action='#' method='post' border='0'>
    <table>
        <tr>
            <td>Name</td>
            <td><input type='text' name='name' required /></td>
        </tr>
        <tr>
            <td>Phone number</td>
            <td><input type='number' name='phone_num' required /></td>
        </tr>
        <tr>
            <td>IMEI</td>
            <td><input type='number' name='imei' required /></td>
        </tr>
        <tr>
            <td>Type</td>
            <td>
	      <input type='radio' name='type' value='p' required />  Patient<br>
	      <input type='radio' name='type' value='g' required />  Guardian<br>
            
            </td>
        <tr>
            <td></td>
            <td>                
                <input type='submit' value='Save' class='customBtn' />
            </td>
        </tr>
    </table>
</form>