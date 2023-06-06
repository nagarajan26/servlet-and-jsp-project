<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.sql.*,java.io.*"%>
<!DOCTYPE html>
<html>
<head>
    
<meta charset="UTF-8">
<title>Show Page</title>
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
    
    <script>

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
                   // myMSALObj.logoutPopup(logoutRequest);
                    
            }
        else
            {
                console.log(this.responseText);
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

</head>
<body bgcolor="BlanchedAlmond">
<button id="button" onclick="location.replace('http://localhost:8080/application/')">Switch</button>
<br>
<br>
<button onclick="signOut()">SignOut</button>
<br>
<br>
<%
Class.forName("com.mysql.cj.jdbc.Driver");
Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/Reminder","root","Naga26@123");
String name,tt,desc,dd;
String s2="Select rname,dd,tt,rdesc from table1";
Statement stm=con.createStatement();
ResultSet set=stm.executeQuery(s2);
int c=1;
while(set.next())
{
	if(c==1)
	{
		out.println("\n<br>-----------------Reminders----------------<br>");
		c++;
	}
		name=set.getString(1);
		dd=set.getString(2);
		tt=set.getString(3);
		desc=set.getString(4);
		out.println("\n Name          : "+name+"<br>");
		out.println("\n Date          : "+dd+"<br>");
		out.println("\n Description   : "+desc+"<br>");
		out.println("\n Time          : "+tt+"<br>");
		out.println("\n------------------------------------------<br>");
}
%>
<br><b>Redirect to main page 'click me'</b><br>
<input type='button' onclick="window.location='index.html'" value='Click Me!'>
</form>
</body>
</html>