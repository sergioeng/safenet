/*!
 * dodsoneng functions
 * version: 1.0.1 (Tue, 15 Oct 2013)
 * @requires jQuery v1.6 or later
 *
 * Copyright 2013 Dodsoneng Co
 *
 */
var GAME_TYPE_CLASSIC	        = 1;
var GAME_TYPE_BEAT_THE_CLOCK	= 2;

window.questionCounter = 0;
window.buttonClicked = 0;

/* TOBEDELETED
function displayDebug (bttnDivId,question,correctAnswer,selectedAnswer,triviaId)
{
document.getElementById("sergiotest").innerHTML="DEBUG: buttonClick=" + window.buttonClicked +
                                                " 1> " + bttnDivId + " 2> " + question + " 3> " + correctAnswer + " 4> " + selectedAnswer + " 5> " + triviaId;
}
*/
function showModal ()
{ 
	//alert ("test: " + modalId);
	//$('.modal').show();
	$('.modal').modal('show');
}

function closeModal ()
{ 
	//alert ("test: " + modalId);
	$('.modal').hide();
}
/*
=====================================================================================
FUNCTION: displayFinalScore ()
ARGUMENTS: none
DESCRIPTION: It calls the partialScore.php indicating in the URL this is FINAL score
======================================================================================
*/
function displayFinalScore (numOfQuestions)
{
	/* CALCULATE BONUS (from android app)
		give 150 bonus points for 10/10 
		give 200 bonus points for 15/15 
		give 300 bonus points for 20/20 
		give 400 bonus points for 25/25 
		give 7500 bonus points for 30/30 
		give 1000 bonus points for 40/40 
		give 1500 bonus points for 50/50 
	*/
	bonus = 0;
	if (window.numOfCorrectAnswers == numOfQuestions) {
		if (     numOfQuestions == 10) bonus = 150;
		else if (numOfQuestions == 15) bonus = 200;
		else if (numOfQuestions == 20) bonus = 300;
		else if (numOfQuestions == 25) bonus = 400;
		else if (numOfQuestions == 30) bonus = 500;
		else if (numOfQuestions == 40) bonus = 1000;
		else if (numOfQuestions == 50) bonus = 1500;
		
		//alert ("Congratulations you've got a bonus !");
	}

	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		var xmlhttp=new XMLHttpRequest();
	}
	else  {// code for IE6, IE5
		var xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	xmlhttp.onreadystatechange=function() {
		document.getElementById("score").innerHTML = "Computing ...";
		if (xmlhttp.readyState==4 && xmlhttp.status==200) {
			document.getElementById("score").innerHTML=xmlhttp.responseText;
			showModal ();
		}
	}

	xmlhttp.open("GET","score.php?partialScore="+window.numOfCorrectAnswers*100+"&final=YES"+"&bonus="+bonus,true);
	xmlhttp.send();

}
/// ENDOF-displayFinalScore()


/*
=====================================================================================
FUNCTION: displayPartialScore ()
ARGUMENTS: none
DESCRIPTION: It calls the partialScore.php indicating in the URL this is PARTIAL score
======================================================================================
*/
function displayPartialScore ()
{
	
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		var xmlhttp=new XMLHttpRequest();
	}
	else  {// code for IE6, IE5
		var xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}

	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200) {
			document.getElementById("score").innerHTML=xmlhttp.responseText;
		}
	}

	xmlhttp.open("GET","score.php?partialScore="+window.numOfCorrectAnswers*100+"&final=NO&bonus=0",true);
	xmlhttp.send();

}
/// ENDOF-displayPartialScore ()

/*
=====================================================================================
FUNCTION: playTheGame ()
ARGUMENTS: numOfQuestions
DESCRIPTION: This function is called when the user chooses the number of question to
			  play.
======================================================================================
*/
function playTheGame (numOfQuestions)
{
//	alert ("test");
	
	/// Global variables initialization
	window.questionCounter = 1;
	window.buttonClicked = 1;
	window.numOfCorrectAnswers =0;
	window.triviaIdList = new Array ();
	window.isCorrect = new Array ();
	window.questionList = new Array ();
	window.selectedAnswerList = new Array ();
	window.correctAnswerList = new Array ();
	window.referenceList = new Array ();
	window.mycounter = 0;
	
	if (numOfQuestions == 0)
		window.gameType = GAME_TYPE_BEAT_THE_CLOCK;
	else
		window.gameType = GAME_TYPE_CLASSIC;
	
	// Makes the "Continue" button visible again.
	$("#nextbttn").css('visibility','visible');
	
	/// Prepare the request for the processGame.php to generate the first trivia page
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		var xmlhttp=new XMLHttpRequest();
	}
	else  {// code for IE6, IE5
		var xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}

	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200) {
		
			/// Make the div game visible
			$("#GAME").css('visibility','visible');
			document.getElementById("triviaArea").innerHTML=xmlhttp.responseText;
		}
	}

  	xmlhttp.open("GET","processGame.php?numOfQuestions="+numOfQuestions+"&questionId="+window.questionCounter,true);
  	xmlhttp.send();
  	window.questionCounter = window.questionCounter + 1
  	window.buttonClicked = 0;

  	// Hide the DIV used to start the game 
	$("#BEGINGAME").css('visibility','hidden');
}
///ENDOF-playTheGame()



function setTimer (value)
{

	window.countdown = value ;
	window.timer=setInterval(timer, 1000); //1000 will  run it every 1 second

}

function timer()
{
	//	alert (window.countdown);
  	window.countdown = window.countdown -1;
  	if (window.countdown < 0)
  	{
    	clearInterval(window.timer);
   	  	alert ("Timer expired");
     	window.buttonClicked = 1;
     	window.questionCounter -= 1;
     	showNext (window.questionCounter - 1);
     	return;
  	}

	xxx = window.countdown / 60.0 - 0.5;
	mins = xxx.toFixed(0);
	if (mins <= 0) mins = 0;
	secs = window.countdown % 60;
	
  	//Do code for showing the number of seconds here
  	if (secs < 10)
		document.getElementById("TIMER").innerHTML=mins + ":0" + secs;
	else
		document.getElementById("TIMER").innerHTML=mins + ":" + secs;
}


/*
=====================================================================================
FUNCTION: showNext ()
ARGUMENTS: numOfQuestions
DESCRIPTION: Callback for the CONTINUE button.
======================================================================================
*/
function showNext(numOfQuestions)
{
	/// If none of the answer's button was clicked, return
	if (! window.buttonClicked) {
		alert ("Please choose an answer to proceed");
		return;
	}

	if (numOfQuestions=="") {
		document.getElementById("triviaArea").innerHTML="";
		return;
	} 
    
    /// If #questions displayed greater than the game size
	if (numOfQuestions != 0 && window.questionCounter > numOfQuestions) {

		/// Display final score and results
		displayFinalScore (numOfQuestions);

		var results="";
		for (var i=1; i <= numOfQuestions; i ++)  {
			if (window.isCorrect [i] == 1) {
				results = results + "<div class=\"hero-results-correct\">";
				results = results + "<h4><strong><a href=\"#\">Question" + i + ":</a></strong></h4>";
				results = results + "<p>" + window.questionList[i] + "</p>";
				results = results + "<p><b>Your Answer:</b> " + window.selectedAnswerList[i] + "</p>";
				results = results + "<p><b>You're Correct!</b></p>";
				results = results + "<p></p>";
				results = results + "<p><b>Bible Reference: </b>" + window.referenceList[i] + "</p>";
				results = results + "</div><br>";
			}
			else {
				results = results + "<div class=\"hero-results-wrong\">";
				results = results + "<h4><strong><a href=\"#\">Question" + i + ":</a></strong></h4>";
				results = results + "<p>" + window.questionList[i] + "</p>";
				results = results + "<p><b>Your Answer:</b> " + window.selectedAnswerList[i] + "</p>";
				results = results + "<p><b>Correct Answer:</b> " + window.correctAnswerList[i] + "</p>";
				results = results + "<p></p>";
				results = results + "<p><b>Bible Reference: </b>" + window.referenceList[i] + "</p>";
				results = results + "</div><br>";
			}
		}
		document.getElementById("triviaArea").innerHTML=results;
		
		/// Display PLAY AGAIN button
		var playAButton = "";
		if (window.gameType == GAME_TYPE_BEAT_THE_CLOCK)
  		   playAButton = "<p><a class=\"btn btn-info btn-block\" href='beattheclock.php'>Play Again</a></p>";
  		else
  		   playAButton = "<p><a class=\"btn btn-info btn-block\" href='classictrivia.php'>Play Again</a></p>";
  		   
		document.getElementById("playAgain").innerHTML=playAButton;

		/// Hides the "Continue button"
		$("#nextbttn").css('visibility','hidden');
	}
	else {
		
		/// Display next question
		
		$("#nextbttn").css('background-image','none').css('background-color','Red') ;
		$("#nextbttn").text("Question loading, please wait ...")

		displayPartialScore ();

		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			var xmlhttp=new XMLHttpRequest();
		}
		else  {// code for IE6, IE5
			var xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}

		xmlhttp.onreadystatechange=function() {
			if (xmlhttp.readyState==4 && xmlhttp.status==200) {
			
				if (window.countdown < 0)
					return;
				document.getElementById("triviaArea").innerHTML=xmlhttp.responseText;
				$("#nextbttn").css('background-image','none').css('background-color','#49afcd') ;
				$("#nextbttn").text("Continue>")

			}
		}
	
		xmlhttp.open("GET","processGame.php?numOfQuestions="+numOfQuestions+"&questionId="+window.questionCounter,true);
		xmlhttp.send();
		window.questionCounter = window.questionCounter + 1
		window.buttonClicked = 0;
	}
}
/// ENDOF-showNext()


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

