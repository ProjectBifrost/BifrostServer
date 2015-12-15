package uk.co.terragaming.bifrostServer;

import java.net.Proxy;
import java.util.function.Consumer;

import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.mc.protocol.MinecraftConstants;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.ServerLoginHandler;
import org.spacehq.mc.protocol.data.SubProtocol;
import org.spacehq.mc.protocol.data.message.TextMessage;
import org.spacehq.mc.protocol.data.status.PlayerInfo;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol.data.status.VersionInfo;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoBuilder;
import org.spacehq.mc.protocol.packet.handshake.client.HandshakePacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.server.ServerAdapter;
import org.spacehq.packetlib.event.server.SessionAddedEvent;
import org.spacehq.packetlib.event.server.SessionRemovedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import uk.co.terragaming.bifrostServer.util.TerraLogger;
import uk.co.terragaming.bifrostServer.util.Text;


public class BifrostServer {
	
	private final String NAME;
    private final boolean VERIFY_USERS;
    private final String HOST;
    private final int PORT;
    private final Proxy PROXY;
    private final Proxy AUTH_PROXY;
   
    private Server server;
    
    private Consumer<Session> loginHandler;
   
	public BifrostServer(String host, int port, String name, boolean verify_users, Proxy proxy, Proxy auth_proxy){
		this.HOST = host;
		this.PORT = port;
		this.NAME = name;
		this.VERIFY_USERS = verify_users;
		this.PROXY = proxy;
		this.AUTH_PROXY = auth_proxy;

		
		String spacer = Text.repeat("-", (" Launching Bifrost Authentication Server ").length());
		String msg = "<l> Launching Bifrost Authentication Server ";
		
		TerraLogger.blank();
		TerraLogger.info(spacer);
		TerraLogger.info(msg);
		TerraLogger.info(spacer);
		TerraLogger.blank();
		
		
		server = new Server(HOST, PORT, MinecraftProtocol.class, new TcpSessionFactory(PROXY));
		server.setGlobalFlag(MinecraftConstants.AUTH_PROXY_KEY, AUTH_PROXY);
		server.setGlobalFlag(MinecraftConstants.VERIFY_USERS_KEY, VERIFY_USERS);
		
		server.setGlobalFlag(MinecraftConstants.SERVER_INFO_BUILDER_KEY, new ServerInfoBuilder(){
			public ServerStatusInfo buildInfo(Session arg0) {
				return new ServerStatusInfo(
					new VersionInfo(MinecraftConstants.GAME_VERSION, MinecraftConstants.PROTOCOL_VERSION),
					new PlayerInfo(25, 0, new GameProfile[0]),
					new TextMessage(NAME),
					null);
			}
		});
		
		server.setGlobalFlag(MinecraftConstants.SERVER_LOGIN_HANDLER_KEY, new ServerLoginHandler(){
			@Override
			public void loggedIn(Session session) {
				GameProfile profile = session.getFlag("profile");
		    	String hostname = session.getFlag("hostname");
		    	TerraLogger.info("<h>%s<r> has connected to <h>%s<r> from <h>%s<r>:<h>%s<r> with UUID <h>%s<r>.", profile.getName(), hostname, session.getHost(), Integer.toString(session.getPort()), profile.getIdAsString());
				loginHandler.accept(session);
				if (session.isConnected())
					session.disconnect(Text.of("<b>An internal error has occurred."));
			}}
		);
		
		server.addListener(new ServerAdapter() {
            @Override
            public void sessionAdded(SessionAddedEvent event) {
                event.getSession().addListener(new SessionAdapter() {
                    @Override
                    public void packetReceived(PacketReceivedEvent event) {
                    	if (event.getPacket() instanceof HandshakePacket)
                    		event.getSession().setFlag("hostname", event.<HandshakePacket>getPacket().getHostName());
                    }
                });
            }

            @Override
            public void sessionRemoved(SessionRemovedEvent event) {
                MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
                if(protocol.getSubProtocol() == SubProtocol.GAME) {
                    event.getServer().close();
                }
            }
        });
		
		server.setGlobalFlag(MinecraftConstants.SERVER_COMPRESSION_THRESHOLD, 100);
		
		TerraLogger.info("Starting Server on <h>%s<r>:<h>%s<r>.", HOST, Integer.toString(port));
	}
	
	public void start(){
		server.bind();
		while (server.isListening()){
			try {
                Thread.sleep(5);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
		}
		start();
	}

	public void setLoginHandler(Consumer<Session> loginHandler) {
		this.loginHandler = loginHandler;
	}
	
}
