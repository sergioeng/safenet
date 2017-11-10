<!--
	-we have our html form here where user information will be entered
	-we used the 'required' html5 property to prevent empty fields
-->
<form id='addSymptomForm' action='#' method='post' border='0'>
    <table>
        <tr>
            <td>Name</td>
            <td><input type='text' name='name' required /></td>
        </tr>
        <tr>
            <td>Data entry      </td> 
             <td><input type='radio' name='add_entry' value=1 required />  YES </td>
	     <td><input type='radio' name='add_entry' value=0 required />  NO </td>
        </tr>
        <tr>
            <td>Unit</td>
            <td><input type='text' name='unit' required /></td>
        </tr>
        <tr>
            <td></td>
            <td>                
                <input type='submit' value='SAVE' onclick='showSymptoms()' />
            </td>
        </tr>
    </table>
</form>