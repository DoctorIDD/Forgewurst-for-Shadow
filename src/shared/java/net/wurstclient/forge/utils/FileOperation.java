package net.wurstclient.forge.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FileOperation {
	   public FileOperation() {
	   }

	   public static boolean createFile(File fileName) throws Exception {
	      boolean flag = false;

	      try {
	         if(!fileName.exists()) {
	            fileName.createNewFile();
	            flag = true;
	         }
	      } catch (Exception var3) {
	         var3.printStackTrace();
	      }

	      return true;
	   }

	   public static String readTxtFile(String fileName) {
	      StringBuffer result = new StringBuffer();
	      FileReader fileReader = null;
	      BufferedReader bufferedReader = null;

	      try {
	         fileReader = new FileReader(new File(fileName));
	         bufferedReader = new BufferedReader(fileReader);

	         try {
	            String var13 = null;

	            while((var13 = bufferedReader.readLine()) != null) {
	               result.append(var13 + "\r\n");
	            }
	         } catch (Exception var14) {
	            var14.printStackTrace();
	         }
	      } catch (Exception var15) {
	         var15.printStackTrace();
	      } finally {
	         try {
	            if(bufferedReader != null) {
	               bufferedReader.close();
	            }

	            if(fileReader != null) {
	               fileReader.close();
	            }
	         } catch (IOException var131) {
	            var131.printStackTrace();
	         }

	      }

	      return result.substring(4, result.length() - 2);
	   }

	   public static String readTxtFile(String fileName, String charset) {
	      StringBuffer result = new StringBuffer();
	      BufferedReader bufferedReader = null;

	      try {
	         InputStreamReader var14 = new InputStreamReader(new FileInputStream(fileName), charset);
	         bufferedReader = new BufferedReader(var14);

	         try {
	            String var15 = null;

	            while((var15 = bufferedReader.readLine()) != null) {
	               result.append(var15 + "\r\n");
	            }
	         } catch (Exception var151) {
	            var151.printStackTrace();
	         }
	      } catch (Exception var16) {
	         var16.printStackTrace();
	      } finally {
	         try {
	            if(bufferedReader != null) {
	               bufferedReader.close();
	            }
	         } catch (IOException var141) {
	            var141.printStackTrace();
	         }

	      }

	      return result.substring(0, result.length() - 2);
	   }

	   public static boolean writeTxtFile(String filePath, String content) {
	      boolean flag = false;
	      FileOutputStream o = null;

	      try {
	         o = new FileOutputStream(new File(filePath));
	         new OutputStreamWriter(o, "utf-8");
	         o.write(content.getBytes("UTF-8"));
	         flag = true;
	         o.close();
	      } catch (Exception var5) {
	         var5.printStackTrace();
	      }

	      return flag;
	   }

	   public static boolean writeTxtFile(String filePath, List content) {
	      StringBuffer sb = new StringBuffer("");
	      Iterator var3 = content.iterator();

	      while(var3.hasNext()) {
	         String s = (String)var3.next();
	         sb.append(s + "\r\n");
	      }

	      return writeTxtFile(filePath, sb.toString().substring(0, sb.length() - 2));
	   }

	   public static void appendContent(String fileName, String content) {
	      FileWriter writer = null;

	      try {
	         writer = new FileWriter(fileName, true);
	         writer.write(content);
	      } catch (IOException var12) {
	         var12.printStackTrace();
	      } finally {
	         try {
	            if(writer != null) {
	               writer.close();
	            }
	         } catch (IOException var11) {
	            var11.printStackTrace();
	         }

	      }

	   }

	   public static void moveFile(File file, String url, String newFileName) {
	      try {
	         FileInputStream var8 = new FileInputStream(file);
	         FileOutputStream os = new FileOutputStream(new File(url, newFileName));
	         byte[] buffer = new byte[500];
	         boolean length = false;

	         while(-1 != var8.read(buffer, 0, buffer.length)) {
	            os.write(buffer);
	         }

	         os.close();
	         var8.close();
	      } catch (FileNotFoundException var7) {
	         var7.printStackTrace();
	      } catch (IOException var81) {
	         var81.printStackTrace();
	      }

	   }

	   public static void fileMove(String oldPath, String newPath) {
	      try {
	         File var7 = new File(oldPath);
	         if(var7.exists()) {
	            FileInputStream inStream = new FileInputStream(oldPath);
	            FileOutputStream fs = new FileOutputStream(newPath);
	            byte[] buffer = new byte[1024];

	            int length;
	            while((length = inStream.read(buffer)) != -1) {
	               fs.write(buffer, 0, length);
	            }

	            inStream.close();
	            fs.close();
	         }
	      } catch (Exception var71) {
	         System.out.println("");
	         var71.printStackTrace();
	      }

	   }

	   public static List getFiletoList(String path) {
	      return Arrays.asList(readTxtFile(path).split("\r\n"));
	   }

	   public static void updateAttr(boolean hide, String url) {
	      try {
	         int c = hide?43:45;
	         Runtime.getRuntime().exec("attrib " + c + "s " + c + "a " + c + "h " + c + "r \"" + url + "\"");
	      } catch (IOException var3) {
	         ;
	      }

	   }
	}