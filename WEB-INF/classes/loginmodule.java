import java.util.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import javax.servlet.http.*;
import java.util.*;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.*;
import javax.security.auth.spi.LoginModule;
import javax.security.auth.login.FailedLoginException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;
import java.net.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.*;

















public class loginmodule implements LoginModule{
	private CallbackHandler handler;
  	private Subject subject;
  	private UserPrincipal userPrincipal;
 	private RolePrincipal rolePrincipal;
  	private String login;
  	private List<String> userGroups;
	private boolean authenticationflag=false;


	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
			Map<String, ?> options) {
		handler = callbackHandler;
		this.subject = subject;
			System.out.println("Initialize..");
		
	}
	@Override
	public boolean login() throws LoginException {
		
		System.out.println("login..");
		Callback[] callbacks = new Callback[2];
    	callbacks[0] = new NameCallback("login");
    	callbacks[1] = new PasswordCallback("password", true);
    		
		try {
			handler.handle(callbacks);
			String name=((NameCallback) callbacks[0]).getName();
			String accessToken = String.valueOf(((PasswordCallback) callbacks[1]).getPassword());
			System.out.println(name);



			URL url = new URL("https://graph.microsoft.com/v1.0/me");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Accept", "application/json");
            String response = HttpClientHelper.getResponseStringFromConn(conn);
			int responseCode = conn.getResponseCode();
    		if(responseCode != HttpURLConnection.HTTP_OK) {
    				throw new IOException(response);
    			}
			JSONObject responseObject = HttpClientHelper.processResponse(responseCode, response);
			JSONObject nam=responseObject.getJSONObject("responseMsg");
			String valid=responseObject.toString();
			System.out.println(valid);
			String name2=nam.getString("userPrincipalName");

			
			System.out.println(name2);
			//valid part

			/*Base64 decoder = new Base64(-1, null, true);
			String[] id_token_parts = id_token.split("\\.");
	    	String MODULUS = "wEMMJtj9yMQd8QS6Vnm538K5GN1Pr_I31_LUl9-OCYu-9_DrDvPGjViQK9kOiCjBfyqoAL-pBecn9-XXaS-C4xZTn1ZRw--GELabuo0u-U6r3TKj42xFDEP-_R5RpOGshoC95lrKiU5teuhn4fBM3XfR2GB0dVMcpzN3h4-0OMvBK__Zr9tkQCU_KzXTbNCjyA7ybtbr83NF9k3KjpTyOyY2S-qvFbY-AoqMhL9Rp8r2HBj_vrsr6RX6GeiSxxjbEzDFA2VIcSKbSHvbNBEeW2KjLXkz6QG2LjKz5XsYLp6kv_-k9lPQBy_V7Ci4ZkhAN-6j1S1Kcq58aLbp0wDNKQ";
	    	String EXPONENT = "AQAB";
			byte[] decodedheaderBytes = decoder.decode(id_token_parts[0]);
        	String header = new String(decodedheaderBytes, StandardCharsets.UTF_8);
			byte[] decodedpayloadBytes = decoder.decode(id_token_parts[1]);
        	String payload = new String(decodedpayloadBytes, StandardCharsets.UTF_8);
        	byte[] signature = decoder.decode(id_token_parts[2]);
        	byte[] nb = decoder.decode(MODULUS);
			byte[] eb = decoder.decode(EXPONENT);
			BigInteger n = new BigInteger(1, nb);
        	BigInteger e = new BigInteger(1, eb);
			RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(n, e);
			PublicKey publicKey;
        		try
        		{
            			publicKey = KeyFactory.getInstance("RSA").generatePublic(rsaPublicKeySpec);

        		}
        		catch (Exception ex)
        		{
           			throw new RuntimeException("Cant create public key", ex);
        		}
			byte[] data = (id_token_parts[0] + "." + id_token_parts[1]).getBytes(StandardCharsets.UTF_8);
			Signature sig = Signature.getInstance("SHA256withRSA");
        		sig.initVerify(publicKey);
        		sig.update(data);
			boolean valid=sig.verify(signature);*/
			//int valid=1;
			if(valid!=null)
			{
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reminder","root","Naga26@123");
				String s2="Select mailid from table2";
				Statement stm=con.createStatement();
				ResultSet set=stm.executeQuery(s2);
				int c=0;
				while(set.next())
				{
					String id=set.getString("mailid");
					//System.out.println(id);
					if(id.equals(name2))
					{
						System.out.println("true");
						login = name;
	        			userGroups = new ArrayList<String>();
	        			userGroups.add("admin");
						System.out.println("true");
	        			return true;
					}
				}
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Failed");
		throw new FailedLoginException("authentication Failure");

	}
	@Override
	public boolean commit() throws LoginException {
		System.out.println("commit...");
		 userPrincipal = new UserPrincipal(login);
    		subject.getPrincipals().add(userPrincipal);
    		System.out.println(login);
		if (userGroups != null && userGroups.size() > 0) {
      		for (String groupName : userGroups) {
        	rolePrincipal = new RolePrincipal(groupName);
        	subject.getPrincipals().add(rolePrincipal);
        	System.out.println(groupName);
      		}
   	 }
   	 	System.out.println(".....");
		return true;
	}
	@Override
	public boolean abort() throws LoginException {
		System.out.println("abort..");
		return false;
	}
	@Override
	public boolean logout() throws LoginException {
		subject.getPrincipals().remove(userPrincipal);
    		subject.getPrincipals().remove(rolePrincipal);
		System.out.println("logout..");
		return true;
	}

}

class HttpClientHelper {

    private HttpClientHelper() {
    }

    static String getResponseStringFromConn(HttpURLConnection conn) throws IOException {

        BufferedReader reader;
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder stringBuilder= new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }

    static JSONObject processResponse(int responseCode, String response) throws JSONException  {

        JSONObject responseJson = new JSONObject();
        responseJson.put("responseCode", responseCode);

        if (response.equalsIgnoreCase("")) {
            responseJson.put("responseMsg", "");
        } else {
        	 responseJson.put("responseMsg", new JSONObject(response));
        }
        return responseJson;
    }
}

























/*





class check
{
    	
    public static String base64UrlDecode(String input)
    {
        byte[] decodedBytes = base64UrlDecodeToBytes(input);
        String result = new String(decodedBytes, StandardCharsets.UTF_8);
        return result;
    }

    public static byte[] base64UrlDecodeToBytes(String input)
    {
        Base64 decoder = new Base64(-1, null, true);
        byte[] decodedBytes = decoder.decode(input);

        return decodedBytes;
    }

  

    public static int validateToken(String id_token)
    {
	String[] id_token_parts = id_token.split("\\.");

   String MODULUS = "wEMMJtj9yMQd8QS6Vnm538K5GN1Pr_I31_LUl9-OCYu-9_DrDvPGjViQK9kOiCjBfyqoAL-pBecn9-XXaS-C4xZTn1ZRw--GELabuo0u-U6r3TKj42xFDEP-_R5RpOGshoC95lrKiU5teuhn4fBM3XfR2GB0dVMcpzN3h4-0OMvBK__Zr9tkQCU_KzXTbNCjyA7ybtbr83NF9k3KjpTyOyY2S-qvFbY-AoqMhL9Rp8r2HBj_vrsr6RX6GeiSxxjbEzDFA2VIcSKbSHvbNBEeW2KjLXkz6QG2LjKz5XsYLp6kv_-k9lPQBy_V7Ci4ZkhAN-6j1S1Kcq58aLbp0wDNKQ";
    String EXPONENT = "AQAB";

    String ID_TOKEN_HEADER = base64UrlDecode(id_token_parts[0]);
    String ID_TOKEN_PAYLOAD = base64UrlDecode(id_token_parts[1]);
    byte[] ID_TOKEN_SIGNATURE = base64UrlDecodeToBytes(id_token_parts[2]);

        PublicKey publicKey = getPublicKey(MODULUS, EXPONENT);
        byte[] data = (id_token_parts[0] + "." + id_token_parts[1]).getBytes(StandardCharsets.UTF_8);

        try
        {
            boolean isSignatureValid = verifyUsingPublicKey(data, ID_TOKEN_SIGNATURE, publicKey);
            System.out.println("isSignatureValid: " + isSignatureValid);
		return 1;
        }
        catch (GeneralSecurityException e)
        {
            e.printStackTrace();
return 0;
        }

    }

    public static PublicKey getPublicKey(String MODULUS, String EXPONENT)
    {
        byte[] nb = base64UrlDecodeToBytes(MODULUS);
        byte[] eb = base64UrlDecodeToBytes(EXPONENT);
        BigInteger n = new BigInteger(1, nb);
        BigInteger e = new BigInteger(1, eb);

        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(n, e);
        try
        {
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(rsaPublicKeySpec);

            return publicKey;
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Cant create public key", ex);
        }
    }

    private static boolean verifyUsingPublicKey(byte[] data, byte[] signature, PublicKey pubKey) throws GeneralSecurityException
    {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(pubKey);
        sig.update(data);

        return sig.verify(signature);
    }
}

 class check
{
	
    public static String base64UrlDecode(String input)
    {
        byte[] decodedBytes = base64UrlDecodeToBytes(input);
        String result = new String(decodedBytes, StandardCharsets.UTF_8);
        return result;
    }

    public static byte[] base64UrlDecodeToBytes(String input)
    {
        Base64 decoder = new Base64(-1, null, true);
        byte[] decodedBytes = decoder.decode(input);

        return decodedBytes;
    }
    public static int validateToken(String id)
    {
	    String id_token =id ;
	   	String[] id_token_parts = id_token.split("\\.");
	    String MODULUS = "wEMMJtj9yMQd8QS6Vnm538K5GN1Pr_I31_LUl9-OCYu-9_DrDvPGjViQK9kOiCjBfyqoAL-pBecn9-XXaS-C4xZTn1ZRw--GELabuo0u-U6r3TKj42xFDEP-_R5RpOGshoC95lrKiU5teuhn4fBM3XfR2GB0dVMcpzN3h4-0OMvBK__Zr9tkQCU_KzXTbNCjyA7ybtbr83NF9k3KjpTyOyY2S-qvFbY-AoqMhL9Rp8r2HBj_vrsr6RX6GeiSxxjbEzDFA2VIcSKbSHvbNBEeW2KjLXkz6QG2LjKz5XsYLp6kv_-k9lPQBy_V7Ci4ZkhAN-6j1S1Kcq58aLbp0wDNKQ";
	    String EXPONENT = "AQAB";

	    String ID_TOKEN_HEADER = base64UrlDecode(id_token_parts[0]);
	    String ID_TOKEN_PAYLOAD = base64UrlDecode(id_token_parts[1]);
	    byte[] ID_TOKEN_SIGNATURE = base64UrlDecodeToBytes(id_token_parts[2]);
	        PublicKey publicKey = getPublicKey(MODULUS, EXPONENT);
        byte[] data = (id_token_parts[0] + "." + id_token_parts[1]).getBytes(StandardCharsets.UTF_8);

        try
        {
            boolean isSignatureValid = verifyUsingPublicKey(data, ID_TOKEN_SIGNATURE, publicKey);
            System.out.println("isSignatureValid: " + isSignatureValid);
            return 1;
        }
        catch (GeneralSecurityException e)
        {
            System.out.println("exception     fvf  ;"+e);
            return 0;
        }

    }

    public static PublicKey getPublicKey(String MODULUS, String EXPONENT)
    {
        byte[] nb = base64UrlDecodeToBytes(MODULUS);
        byte[] eb = base64UrlDecodeToBytes(EXPONENT);
        BigInteger n = new BigInteger(1, nb);
        BigInteger e = new BigInteger(1, eb);

        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(n, e);
        try
        {
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(rsaPublicKeySpec);

            return publicKey;
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Cant create public key", ex);
        }
    }

    private static boolean verifyUsingPublicKey(byte[] data, byte[] signature, PublicKey pubKey) throws GeneralSecurityException
    {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(pubKey);
        sig.update(data);

        return sig.verify(signature);
    }
}








*/






/*
public class loginmodule implements LoginModule{
	private CallbackHandler handler;
  	private Subject subject;
  	private UserPrincipal userPrincipal;
 	private RolePrincipal rolePrincipal;
  	private String login;
  	private List<String> userGroups;
	private boolean authenticationflag=false;


	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
			Map<String, ?> options) {
		handler = callbackHandler;
		this.subject = subject;
			System.out.println("Initialize..");
		
	}
	@Override
	public boolean login() throws LoginException {
		System.out.println("login..");
		Callback[] callbacks = new Callback[2];
    		callbacks[0] = new NameCallback("login");
    		callbacks[1]=new PasswordCallback("appid",true);
    		
		try {
			handler.handle(callbacks);
			String name=((NameCallback) callbacks[0]).getName();
			String appid=String.valueOf(((PasswordCallback) callbacks[1]).getPassword());
			System.out.println(appid);
			System.out.println(name);
			check obj=new check();
			int valid= obj.validateToken(appid);
			//int valid=1;
			if(valid==1)
			{
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reminder","root","Naga26@123");
				String s2="Select mailid from table2";
				Statement stm=con.createStatement();
				ResultSet set=stm.executeQuery(s2);
				int c=0;
				while(set.next())
				{
					String id=set.getString("mailid");
					System.out.println(id);
					if(id.equals(name))
					{
						System.out.println("true");
						login = name;
	        			userGroups = new ArrayList<String>();
	        			userGroups.add("admin");
						System.out.println("true");
	        			return true;
					}
				}
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Failed");
		throw new FailedLoginException("authentication Failure");
	}
	@Override
	public boolean commit() throws LoginException {
		System.out.println("commit...");
		 userPrincipal = new UserPrincipal(login);
    		subject.getPrincipals().add(userPrincipal);
    		System.out.println(login);
		if (userGroups != null && userGroups.size() > 0) {
      		for (String groupName : userGroups) {
        	rolePrincipal = new RolePrincipal(groupName);
        	subject.getPrincipals().add(rolePrincipal);
        	System.out.println(groupName);
      		}
   	 }
   	 	System.out.println(".....");
		return true;
	}
	@Override
	public boolean abort() throws LoginException {
		System.out.println("abort..");
		return false;
	}
	@Override
	public boolean logout() throws LoginException {
		subject.getPrincipals().remove(userPrincipal);
    		subject.getPrincipals().remove(rolePrincipal);
		System.out.println("logout..");
		return true;
	}

}




*/





































/*import java.util.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import javax.servlet.http.*;
import java.util.*;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.*;
import javax.security.auth.spi.LoginModule;
import javax.security.auth.login.FailedLoginException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.RSAPublicKeySpec;
import org.apache.commons.codec.binary.Base64;








public class check
{
	public static final String id_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IjJaUXBKM1VwYmpBWVhZR2FYRUpsOGxWMFRPSSJ9.eyJhdWQiOiJmOGZlMjAxNC1jMTA3LTQ0NWQtYjlhYi04OTdkNWE0OThkODIiLCJpc3MiOiJodHRwczovL2xvZ2luLm1pY3Jvc29mdG9ubGluZS5jb20vNmNmNjVkN2YtNjUzZi00NGI5LTliMjItYTcwNTk3YTI0ZTQxL3YyLjAiLCJpYXQiOjE2NjQ0NTAzNjQsIm5iZiI6MTY2NDQ1MDM2NCwiZXhwIjoxNjY0NDU0MjY0LCJhaW8iOiJBV1FBbS84VEFBQUFNNWkzR1FEaTRNM1N5alJkRXdyczBiUldFdmxRd3N4OEdaenJENWR4cUNWdGNNZVFRTmVVZXZHcW9Bai94SzdOL3ZyTlhLczgrZG4yYVVaejNrWTRHd0hzNEs1eFlnaFV0dlVlcDQyRklWUzZXYzR6MWdFaW9GenRtVCt5Qi9IaSIsImlkcCI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzkxODgwNDBkLTZjNjctNGM1Yi1iMTEyLTM2YTMwNGI2NmRhZC8iLCJuYW1lIjoiTmFnYXJhamFuIE0iLCJub25jZSI6ImJkODI4YjNjLTQ4ZGMtNDk4Mi1iN2RjLTI1MWE1NDE1YWNiZCIsIm9pZCI6IjExZWM2YjI1LTFjZmItNDRkYi05Y2EzLWYwMGI0Y2RkN2JhMiIsInByZWZlcnJlZF91c2VybmFtZSI6Im5hZ2FyYWphbm11dGh1c2FtaTI2QGdtYWlsLmNvbSIsInJoIjoiMC5BVW9BZjEzMmJEOWx1VVNiSXFjRmw2Sk9RUlFnX3ZnSHdWMUV1YXVKZlZwSmpZS0pBQk0uIiwic3ViIjoiZU5KdHVva2xPc3oyVjZzZVFuc3l5V2xWTkFfZ2tyeHFSS0MtSkVEcFJCbyIsInRpZCI6IjZjZjY1ZDdmLTY1M2YtNDRiOS05YjIyLWE3MDU5N2EyNGU0MSIsInV0aSI6InNRd1hSdDJsNFVpR0wwSlJNUXAxQUEiLCJ2ZXIiOiIyLjAifQ.KrVujXePpe8NxFn_ckBm7I7TsIjrkhF4mOBwZPsV7gnkZcTZGw-cRKQs4CzjBAPlktCrHEOVOb6iQ09eLhPj6nkqoxDX-9ffTmvyboCJpW-NzKfKgrLPXfuLcak2Hqal2KPIdDYFiqjokMibHTfCUEnbn4oIExwXLmA5m8SZLsx2RJDLv0vYUa22sZD5xZvAMsqpbEGmkaNmovq1o3aDOHEQcD8yrK9x9GgoM_CW7D-kqLH_anAnCAOvbe4C-WXmL5Jmve4ER59S71BrvgroCz3JJkwckbFyEeDCXY66n24lpCe_p-ocN_a-bakDmXEaazT3uxVd-7b4Vn_nQ4kvgg";
    public static final String[] id_token_parts = id_token.split("\\.");
    public static final String MODULUS = "wEMMJtj9yMQd8QS6Vnm538K5GN1Pr_I31_LUl9-OCYu-9_DrDvPGjViQK9kOiCjBfyqoAL-pBecn9-XXaS-C4xZTn1ZRw--GELabuo0u-U6r3TKj42xFDEP-_R5RpOGshoC95lrKiU5teuhn4fBM3XfR2GB0dVMcpzN3h4-0OMvBK__Zr9tkQCU_KzXTbNCjyA7ybtbr83NF9k3KjpTyOyY2S-qvFbY-AoqMhL9Rp8r2HBj_vrsr6RX6GeiSxxjbEzDFA2VIcSKbSHvbNBEeW2KjLXkz6QG2LjKz5XsYLp6kv_-k9lPQBy_V7Ci4ZkhAN-6j1S1Kcq58aLbp0wDNKQ";
    public static final String EXPONENT = "AQAB";

    public static final String ID_TOKEN_HEADER = base64UrlDecode(id_token_parts[0]);
    public static final String ID_TOKEN_PAYLOAD = base64UrlDecode(id_token_parts[1]);
    public static final byte[] ID_TOKEN_SIGNATURE = base64UrlDecodeToBytes(id_token_parts[2]);

    public static String base64UrlDecode(String input)
    {
        byte[] decodedBytes = base64UrlDecodeToBytes(input);
        String result = new String(decodedBytes, StandardCharsets.UTF_8);
        return result;
    }

    public static byte[] base64UrlDecodeToBytes(String input)
    {
        Base64 decoder = new Base64(-1, null, true);
        byte[] decodedBytes = decoder.decode(input);

        return decodedBytes;
    }

    public static void main(String args[])
    {
        dumpJwtInfo();
        validateToken();
    }

    public static void dump(String data)
    {
        System.out.println(data);
    }

    public static void dumpJwtInfo()
    {
        dump(ID_TOKEN_HEADER);
        dump(ID_TOKEN_PAYLOAD);
    }

    public static void validateToken()
    {
        PublicKey publicKey = getPublicKey(MODULUS, EXPONENT);
        byte[] data = (id_token_parts[0] + "." + id_token_parts[1]).getBytes(StandardCharsets.UTF_8);

        try
        {
            boolean isSignatureValid = verifyUsingPublicKey(data, ID_TOKEN_SIGNATURE, publicKey);
            System.out.println("isSignatureValid: " + isSignatureValid);
        }
        catch (GeneralSecurityException e)
        {
            e.printStackTrace();
        }

    }

    public static PublicKey getPublicKey(String MODULUS, String EXPONENT)
    {
        byte[] nb = base64UrlDecodeToBytes(MODULUS);
        byte[] eb = base64UrlDecodeToBytes(EXPONENT);
        BigInteger n = new BigInteger(1, nb);
        BigInteger e = new BigInteger(1, eb);

        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(n, e);
        try
        {
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(rsaPublicKeySpec);

            return publicKey;
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Cant create public key", ex);
        }
    }

    private static boolean verifyUsingPublicKey(byte[] data, byte[] signature, PublicKey pubKey) throws GeneralSecurityException
    {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(pubKey);
        sig.update(data);

        return sig.verify(signature);
    }
}
































public class loginmodule implements LoginModule{
	private CallbackHandler handler;
  	private Subject subject;
  	private UserPrincipal userPrincipal;
 	private RolePrincipal rolePrincipal;
  	private String login;
  	private List<String> userGroups;
	private boolean authenticationflag=false;
	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
			Map<String, ?> options) {
		handler = callbackHandler;
		this.subject = subject;
			System.out.println("Initialize..");
		
	}
	@Override
	public boolean login() throws LoginException {
		System.out.println("login..");
		Callback[] callbacks = new Callback[2];
    		callbacks[0] = new NameCallback("login");
    		callbacks[1]=new PasswordCallback("appid",true);
		try {
			handler.handle(callbacks);
			String name=((NameCallback) callbacks[0]).getName();
			String appid=String.valueOf(((PasswordCallback) callbacks[1]).getPassword());
			System.out.println(appid);
			System.out.println(name);
			String validate="f8fe2014-c107-445d-b9ab-897d5a498d82";
			if(appid.equals(validate))
			{
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/reminder","root","Naga26@123");
				String s2="Select mailid from table2";
				Statement stm=con.createStatement();
				ResultSet set=stm.executeQuery(s2);
				int c=0;
				while(set.next())
				{
					String id=set.getString("mailid");
					System.out.println(id);
					if(id.equals(name))
					{
						System.out.println("true");
						login = name;
	        			userGroups = new ArrayList<String>();
	        			userGroups.add("admin");
						System.out.println("true");
	        			return true;
					}
				}
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Failed");
		throw new FailedLoginException("authentication Failure");
	}
	@Override
	public boolean commit() throws LoginException {
		System.out.println("commit...");
		 userPrincipal = new UserPrincipal(login);
    		subject.getPrincipals().add(userPrincipal);
    		System.out.println(login);
		if (userGroups != null && userGroups.size() > 0) {
      		for (String groupName : userGroups) {
        	rolePrincipal = new RolePrincipal(groupName);
        	subject.getPrincipals().add(rolePrincipal);
        	System.out.println(groupName);
      		}
   	 }
   	 	System.out.println(".....");
		return true;
	}
	@Override
	public boolean abort() throws LoginException {
		System.out.println("abort..");
		return false;
	}
	@Override
	public boolean logout() throws LoginException {
		subject.getPrincipals().remove(userPrincipal);
    		subject.getPrincipals().remove(rolePrincipal);
		System.out.println("logout..");
		return true;
	}

}
*/