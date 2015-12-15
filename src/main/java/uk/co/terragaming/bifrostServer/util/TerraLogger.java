package uk.co.terragaming.bifrostServer.util;


public class TerraLogger {
	
	public static void debug(Object msg) {
		debug("" + msg, "");
	}
	
	public static void info(Object msg) {
		info("" + msg, "");
	}
	
	public static void warn(String string) {
		warn(string, "");
	}
	
	public static void error(String string) {
		error(string, "");
	}
	
	public static void info(String string) {
		info(string, "");
	}
	
	public static void debug(String string) {
		debug(string, "");
	}
	
	public static void info(String msg, Object... args) {
		System.out.println(Text.of(true, "[<l>BIFROST<r>][<l>INFO<r>] " + Text.of(Text.of(true, msg), args) + "<r>"));
	}
	
	public static void debug(String msg, Object... args) {
		System.out.println(Text.of(true, "[<l>BIFROST<r>][<l>DEBUG<r>] " + Text.of(Text.of(true, msg), args) + "<r>"));
	}
	
	public static void warn(String msg, Object... args) {
		System.out.println(Text.of(true, "[<l>BIFROST<r>][<l>WARN<r>] " + String.format(Text.of(true, msg), args) + "<r>"));
	}
	
	public static void error(String msg, Object... args) {
		System.out.println(Text.of(true, "[<l>BIFROST<r>][<l>ERROR<r>] " + String.format(Text.of(true, msg), args) + "<r>"));
	}
	
	public static void blank() {
		System.out.println(" ");
	}
	
	public static class tools {
		
		public static String repeat(String str, int times) {
			StringBuilder ret = new StringBuilder();
			for (int i = 0; i < times; i++) {
				ret.append(str);
			}
			return ret.toString();
		}
	}
	
}