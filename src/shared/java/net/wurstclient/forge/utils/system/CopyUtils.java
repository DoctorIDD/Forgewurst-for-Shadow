package net.wurstclient.forge.utils.system;

import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;

public class CopyUtils {
	public void copy(String str) {
	      Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(str), (ClipboardOwner)null);
	   }
}
