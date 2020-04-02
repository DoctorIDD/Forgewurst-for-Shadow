package net.wurstclient.forge.utils.system;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.minecraft.client.Minecraft;
import net.wurstclient.forge.utils.ChatUtils;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class SponsoredList extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SponsoredList frame = new SponsoredList();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SponsoredList() {

		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTheSponsoredList = new JLabel("The Sponsored LIst");
		lblTheSponsoredList.setBounds(12, 0, 408, 100);
		lblTheSponsoredList.setIcon(new ImageIcon(SponsoredList.class.getResource("/assets/hurricane/logo.png")));
		lblTheSponsoredList.setBackground(Color.LIGHT_GRAY);
		lblTheSponsoredList.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 15));
		contentPane.add(lblTheSponsoredList);
		
		JLabel lblThanksFor = new JLabel("");
		lblThanksFor.setForeground(Color.RED);
		lblThanksFor.setBackground(Color.LIGHT_GRAY);
		lblThanksFor.setBounds(0, 111, 432, 144);
		contentPane.add(lblThanksFor);
		
		JButton btnBuy = new JButton("Buy");
		/* btnBuy.setBackground(Color.RED); */
		btnBuy.setForeground(Color.RED);
		btnBuy.setBounds(12, 219, 103, 25);
		contentPane.add(btnBuy);
btnBuy.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("OK!");
				try {
					java.net.URI uri = new java.net.URI("https://afdian.net/@beimian");
					java.awt.Desktop.getDesktop().browse(uri);
					ChatUtils.message("OK");
				

				} catch (Exception var5) {
					var5.printStackTrace();

				}

				
			}
		});
	}
}
