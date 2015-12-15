package uk.co.terragaming.bifrostServer;

import java.util.function.Consumer;

import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.packetlib.Session;


public class LoginHandler implements Consumer<Session>{

	@Override
	public void accept(Session session) {
		GameProfile profile = session.getFlag("profile");
    	String hostname = session.getFlag("hostname");

    	
    	
	}
	
}
