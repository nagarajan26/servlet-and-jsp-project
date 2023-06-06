<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Create Page</title>
<style>
    #button{
    top:0;
    right:0;
    position:absolute
    }
</style>
<script src="https://alcdn.msauth.net/browser/2.26.0/js/msal-browser.js"
    integrity="sha384-fitpJWrpyl840mvd9nBFLGulqR4BJzvim0fzrXQKdsVh2AQzE4rTTJ0o5o+x+dRK"
    crossorigin="anonymous"></script>
    <!-- authconfig file -->
    
    <script type="text/javascript">

    const msalConfig = {
        auth: {
            clientId: "f8fe2014-c107-445d-b9ab-897d5a498d82",
            authority: "https://login.microsoftonline.com/6cf65d7f-653f-44b9-9b22-a70597a24e41",
            redirectUri: "http://localhost:8080/jazz/index.html",
        },
       cache: {
            cacheLocation: "sessionStorage", 
            storeAuthStateInCookie: false,
        }
    };
    const loginRequest = {
        scopes: ["User.Read"]
    };

    const tokenRequest = {
        scopes: ["User.Read"]
    };
    </script>
    <!-- authPopup file -->
    <script type="text/javascript">

    const myMSALObj = new msal.PublicClientApplication(msalConfig);

    let username = "";


    function selectAccount() {

        const currentAccounts = myMSALObj.getAllAccounts();
        if (currentAccounts.length === 0) {
            return;
        } else if (currentAccounts.length > 1) {
            // Add choose account code here
            console.warn("Multiple accounts detected.");
        } else if (currentAccounts.length === 1) {
            username = currentAccounts[0].username;
           // showWelcomeMessage(username);
        }
    }
    function handleResponse(response) {

        if (response !== null) {
            username = response.account.username;
       	}
    else{
    	selectAccount();
    	}
    }

    function signIn() {

        myMSALObj.loginPopup(loginRequest)
            .then(handleResponse)
            .catch(error => {
                console.error(error);
            });
    }

    function signOut() {

        const logoutRequest = {
            account: myMSALObj.getAccountByUsername(username),
            postLogoutRedirectUri: msalConfig.auth.redirectUri,
            mainWindowRedirectUri: msalConfig.auth.redirectUri
        };
        var xhr = new XMLHttpRequest();
    	xhr.open('POST', 'logout', true);
    	xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    	xhr.send('username='+username);
    	xhr.onload = function () 
	{
       		if(this.responseText==1)
			{
                 var xhr1 = new XMLHttpRequest();
                xhr1.open('POST', 'http://localhost:8080/application/#/logout', true);
                xhr1.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
                xhr1.send('username=' + username);
                xhr1.onload = function () {
                    myMSALObj.logoutPopup(logoutRequest);
                    console.log(this.response);
                   };
        			//myMSALObj.logoutPopup(logoutRequest);
            
			}
		else
			{
				console.log(this.response);
			}
	}
    }
    function getTokenPopup(request) {
        request.account = myMSALObj.getAccountByUsername(username);
        
        return myMSALObj.acquireTokenSilent(request)
            .catch(error => {
                console.warn("silent token acquisition fails. acquiring token using popup");
                if (error instanceof msal.InteractionRequiredAuthError) {
                    // fallback to interaction when silent call fails
                    return myMSALObj.acquireTokenPopup(request)
                        .then(tokenResponse => {
                            console.log(tokenResponse);
                            return tokenResponse;
                        }).catch(error => {
                            console.error(error);
                        });
                } else {
                    console.warn(error);   
                }
        });
    }

    selectAccount();
</script>


<script>
function fun()
{
	document.getElementById("specs").innerHTML="";
}
function fun1()
{
	document.getElementById("specs").innerHTML="";
}
function fun2()
	{
		document.getElementById("specs").innerHTML="<b>Select the Day of Month to remaind every month  :<br><select name=\"day\" required>"
          +"<option value=\"01\">1</option>"
          +"<option value=\"02\">2</option>"
          +"<option value=\"03\">3</option>"
          +"<option value=\"04\">4</option>"
          +"<option value=\"05\">5</option>"
          +"<option value=\"06\">6</option>"
          +"<option value=\"07\">7</option>"
          +"<option value=\"08\">8</option>"
          +"<option value=\"09\">9</option>"
          +"<option value=\"10\">10</option>"
          +"<option value=\"11\">11</option>"
          +"<option value=\"12\">12</option>"
          +"<option value=\"13\">13</option>"
          +"<option value=\"14\">14</option>"
          +"<option value=\"15\">15</option>"
          +"<option value=\"16\">16</option>"
          +"<option value=\"17\">17</option>"
          +"<option value=\"18\">18</option>"
          +"<option value=\"19\">19</option>"
          +"<option value=\"20\">20</option>"
          +"<option value=\"21\">21</option>"
          +"<option value=\"22\">22</option>"
          +"<option value=\"23\">23</option>"
          +"<option value=\"24\">24</option>"
          +"<option value=\"25\">25</option>"
          +"<option value=\"26\">26</option>"
          +"<option value=\"27\">27</option>"
          +"<option value=\"28\">28</option>"
          +"<option value=\"29\">29</option>"
          +"<option value=\"30\">30</option>"
          +"<option value=\"31\">31</option>"
         +"</select>";
	}
function fun3()
	{
		document.getElementById("specs").innerHTML="<b>Select the Week to remaind every week :<br><select name=\"week\" required>"
		+"<option value=\"1\">Sunday</option>"
		+"<option value=\"2\">Monday</option>"
		+"<option value=\"3\">Tuesday</option>"
		+"<option value=\"4\">Wednesday</option>"
		+"<option value=\"5\">Thursday</option>"
		+"<option value=\"6\">Friday</option>"
		+"<option value=\"7\">Saturday</option>"
		+"</select>";
	}
function fun4()
	{
		document.getElementById("mail").innerHTML="<b>Enter the mail id:<br><input type=\"email\" name=\"email\"><br>";
	}
function fun5()
	{
		document.getElementById("mail").innerHTML="";
	}
function fun6()
	{
                console.log("fsdvsdv");
		var name=document.getElementById("name").value;
		var desc=document.getElementById("desc").value;
		var date=document.getElementById("date").value;
		var time=document.getElementById("time").value;
		var nots=document.getElementById("nots").value;
		var rtype=document.getElementById("rtype").value;
		if(name!="" && desc!="" && date!="" && time!="" && nots!="" && rtype!="")
		{ 
		alert("Successfully registered");
		}
	}
</script>
</head>
<% 
	String name=(String)request.getAttribute("name");
	System.out.println(name);
%>
<body>
<br>
<br>
<button id="button" onclick="location.replace('http://localhost:8080/application/')">Switch</button>
<button onclick="signOut()">SignOut</button><br>
<br>
<div id="create" >
<form action="set" method="POST">
<b>
Enter the Reminder Name:<br>
<input type="text" id="name" name="rname"autofocus required><br>
Enter the Reminder Description:<br>
<input type="text" id="desc" name="rdesc"autofocus required><br>
Enter the Date:<br>
<input type="date" id="date" name="date"autofocus required><br>
Enter the time:<br>
<input type="time" id="time" name="time"autofocus required><br>
Select the Notification Specification:<br></b>
<input type="radio" id="nots" name="notspecs" value=1 onclick="fun()" required>Daily Reminder<br>
<input type="radio" id="nots" name="notspecs" value=2 onclick="fun1()" required>Specific Time Reminder<br>
<input type="radio" id="nots" name="notspecs" value=3 onclick="fun2()" required>Monthly Reminder<br>
<input type="radio" id="nots" name="notspecs" value=4 onclick="fun3()" required>Weekly Reminder<br>
<p id="specs"></p>
<b>Select the Remainder Type:<br></b>
<input type="radio" id="rtype" name="rtype" value=1 onclick="fun5()" required>Message Box<br>
<input type="radio" id="rtype" name="rtype" value=2 onclick="fun4()" required>Mail Send<br>
<p id="mail"></p>
<button style="width:10%" onclick="fun6()">submit</button>
<input type='button' onclick="window.location='index.html'" value='Back'>
</form>
</div>
</body>
</html>