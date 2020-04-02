package net.wurstclient.forge.commands;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.wurstclient.forge.Command;
import net.wurstclient.forge.Command.CmdException;
import net.wurstclient.forge.Command.CmdSyntaxError;
import net.wurstclient.forge.utils.ChatUtils;
import net.wurstclient.forge.utils.user.HWIDUtils;
import net.wurstclient.forge.utils.user.WebUtils;

public class UnlockCmd extends Command{
	public static boolean LOCK=false;
	public UnlockCmd()
	{
		super("unlock", "unlock some features ", "Syntax: .unlock <name>");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length > 0)
			throw new CmdSyntaxError();
	
		
				try {
					if(WebUtils.get("https://gitee.com/beimian-hurricane/codes/9j3p4subfkaiyt8egdw2586/raw?blob_name=HWID.txt").contains(getHWID())) {
						ChatUtils.message("OK!");
						System.out.println(WebUtils.get("https://gitee.com/beimian-hurricane/codes/9j3p4subfkaiyt8egdw2586/raw?blob_name=HWID.txt"));
						LOCK=true;
					}else {
						LOCK=false;
						System.out.println("your id   "+getHWID());
						System.out.println(WebUtils.get("https://gitee.com/beimian-hurricane/codes/9j3p4subfkaiyt8egdw2586/raw?blob_name=HWID.txt"));
						ChatUtils.error("verification failed! your uuid is"+getHWID());
					}
				} catch (NoSuchAlgorithmException e) {
					ChatUtils.error("Network Error!");
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					ChatUtils.error("Error");
					e.printStackTrace();
				} catch (IOException e) {
					ChatUtils.error("Error");
					e.printStackTrace();
				}
					
			
		
		}
		
	
	public static String getHWID() throws NoSuchAlgorithmException,UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		String main = System.getenv("PROCESS_IDENTIFIER") + System.getenv("COMPUTERNAME");
		byte[] bytes = main.getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] md5 = md.digest(bytes);
		int i = 0;
		for (byte b : md5) {
			sb.append(Integer.toHexString((b & 0xFF) | 0x300), 0, 3);
			if (i != md5.length - 1) {
				sb.append("-");
			}
			i++;
		}
		return sb.toString();
	}
}
