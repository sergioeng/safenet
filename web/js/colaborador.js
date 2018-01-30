/*
===========================================================
FUNCTION: selectedAnswer
ARGUMENTS:	(1) answerId		- Answer ID in DB
				0 - correct answer
				1 - wrong answer #1
				2 - wrong answer #2
				3 - wrong answer #3
			(2) corrDivId		- DIV id for the correct button
			(3) bttnDivId		- DIV id for the 4 buttons
			(4) question		- question text.
			(5) correctAnswer	- correct answer text
			(6) selectedAnswer	- selected answer text
			(7) reference		- trivia reference in Bible
			(8) triviaId		- database trivia id
DESCRIPTION: Callback for the answer's buttons
===========================================================
*/
$(document).ready(function(){
     
    // VIEW USERS on load of the page
    $('#loaderImage').show();
    showUsers();
     
    // clicking the 'VIEW USERS' button
    $('#viewUsers').click(function(){
        // show a loader img
        $('#loaderImage').show();
         
        showUsers();
    });
     
    // clicking the '+ NEW USER' button
    $('#addUser').click(function(){
        showCreateUserForm();
    });
 
    // clicking the EDIT button
    $(document).on('click', '.editBtn', function(){ 
     
        var user_id = $(this).closest('td').find('.userId').text();
        console.log(user_id);
         
        // show a loader image
        $('#loaderImage').show();
 
        // read and show the records after 1 second
        // we use setTimeout just to show the image loading effect when you have a very fast server
        // otherwise, you can just do: $('#pageContent').load('update_form.php?user_id=" + user_id + "', function(){ $('#loaderImage').hide(); });
        setTimeout("$('#pageContent').load('update_form.php?user_id=" + user_id + "', function(){ $('#loaderImage').hide(); });",1000);
         
    }); 
     
     
    // when clicking the DELETE button
    $(document).on('click', '.deleteBtn', function(){ 
        if(confirm('Are you sure?')){
         
            // get the id
            var user_id = $(this).closest('td').find('.userId').text();
             
            // trigger the delete file
            $.post("delete.php", { id: user_id })
                .done(function(data) {
                    // you can see your console to verify if record was deleted
                    console.log(data);
                     
                    $('#loaderImage').show();
                     
                    // reload the list
                    showUsers();
                     
                });
 
        }
    });
     
     
    // CREATE FORM IS SUBMITTED
     $(document).on('submit', '#addUserForm', function() {
 
        // show a loader img
        $('#loaderImage').show();
         
        // post the data from the form
        $.post("create.php", $(this).serialize())
            .done(function(data) {
                // 'data' is the text returned, you can do any conditions based on that
                showUsers();
            });
                 
        return false;
    });
     
    // UPDATE FORM IS SUBMITTED
     $(document).on('submit', '#updateUserForm', function() {
 
        // show a loader img
        $('#loaderImage').show();
         
        // post the data from the form
        $.post("update.php", $(this).serialize())
            .done(function(data) {
                // 'data' is the text returned, you can do any conditions based on that
                showUsers();
            });
                 
        return false;
    });
     
});
 
// READ USERS
function showUsers(){
    // read and show the records after at least a second
    // we use setTimeout just to show the image loading effect when you have a very fast server
    // otherwise, you can just do: $('#pageContent').load('read.php', function(){ $('#loaderImage').hide(); });
    // THIS also hides the loader image
    setTimeout("$('#pageContent').load('read.php', function(){ $('#loaderImage').hide(); });", 1000);
}
 
// CREATE USER FORM
function showCreateUserForm(){
    // show a loader image
    $('#loaderImage').show();
     
    // read and show the records after 1 second
    // we use setTimeout just to show the image loading effect when you have a very fast server
    // otherwise, you can just do: $('#pageContent').load('read.php');
    setTimeout("$('#pageContent').load('create_form.php', function(){ $('#loaderImage').hide(); });",1000);
}

/*
===========================================================
FUNCTION: selectedAnswer
ARGUMENTS:	(1) answerId		- Answer ID in DB
				0 - correct answer
				1 - wrong answer #1
				2 - wrong answer #2
				3 - wrong answer #3
			(2) corrDivId		- DIV id for the correct button
			(3) bttnDivId		- DIV id for the 4 buttons
			(4) question		- question text.
			(5) correctAnswer	- correct answer text
			(6) selectedAnswer	- selected answer text
			(7) reference		- trivia reference in Bible
			(8) triviaId		- database trivia id
DESCRIPTION: Callback for the answer's buttons
===========================================================
*/
function selectedAnswer(answerId,corrDivId,bttnDivId,question,correctAnswer,selectedAnswer,reference,triviaId)
{
	
	if (window.buttonClicked) 
		return;

	window.buttonClicked = 1;
		
	window.questionList [window.questionCounter-1] = question;
	window.selectedAnswerList [window.questionCounter-1] = selectedAnswer;
	window.correctAnswerList [window.questionCounter-1] = correctAnswer;
	window.referenceList [window.questionCounter-1] = reference;
	
	if (selectedAnswer == correctAnswer) {
		$("#"+bttnDivId).css('background-image','none').css('background-color','#51a351') ;
		window.numOfCorrectAnswers ++;
		window.isCorrect  [window.questionCounter-1] = 1;
	}
	else {
		$("#"+corrDivId).css('background-image','none').css('background-color','#51a351') ;
		$("#"+bttnDivId).css('background-image','none').css('background-color','#bd362f') ;
		window.isCorrect  [window.questionCounter-1] = 0;
	}
	
	window.triviaIdList    [window.questionCounter-1] = triviaId;
	//displayPartialScore ();
	
	// Updating the number of times the answer was chosen
/******/
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		var xmlhttp=new XMLHttpRequest();
	}
	else  {// code for IE6, IE5
		var xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}

	xmlhttp.open("GET","chosenAnswerUpdate.php?triviaId="+triviaId+"&answerId="+answerId,true);
	xmlhttp.send();
/******/

}
/// ENDOF-selectedAnswer()

