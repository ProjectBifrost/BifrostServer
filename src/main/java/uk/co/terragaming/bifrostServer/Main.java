package uk.co.terragaming.bifrostServer;

import java.net.InetAddress;
import java.net.Proxy;
import java.net.UnknownHostException;

public class Main 
{
	
	private static final String NAME = "\u00A73Project Bifrost \u00A7f[\u00A7eAuthentication Server\u00A7f]\u00A78";
    private static final boolean VERIFY_USERS = true;
    private static final int PORT = 25565;
    private static final Proxy PROXY = Proxy.NO_PROXY;
    private static final Proxy AUTH_PROXY = Proxy.NO_PROXY;
    
    public static void main( String[] args )
    {
		System.setProperty("jansi.passthrough", "true");
		
		String host = "127.0.0.1";
		
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		BifrostServer server = new BifrostServer(host, PORT, NAME, VERIFY_USERS, PROXY, AUTH_PROXY);
		server.setLoginHandler(new LoginHandler());
		server.start();
    }
}
