<!DOCTYPE html>
<html>
<head>
<title>Reminder App</title>
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
                xhr1.open('POST', 'http://localhost:8080/application/logout', true);
                xhr1.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
                xhr1.send('username=' + username);
                xhr1.onload = function () {
                    console.log(this.response);
                    myMSALObj.logoutPopup(logoutRequest);
                    console.log(this.response);
                   };
        			//myMSALObj.logoutPopup(logoutRequest);
                    
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
<body bgcolor="LightCyan">
<br>
<br>
<button onclick="signOut()">SignOut</button><br>
<br>
<br>
<meta http-equiv="refresh" content="20">
<button id=1 onclick="window.location='create.jsp'">1.Create Reminder</button><br>
<button id=2 onclick="window.location='delete.jsp'">2.Delete Reminder</button><br>
<button id=2 onclick="window.location='show.jsp'">3.Show Reminder</button><br>
<button id="button" onclick="location.replace('http://localhost:8080/application/')">Switch</button>
</body>
</html>