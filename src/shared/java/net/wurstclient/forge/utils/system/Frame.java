package net.wurstclient.forge.utils.system;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.minecraft.client.Minecraft;
import net.wurstclient.forge.utils.ChatUtils;

import java.awt.Color;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.TextField;
import javax.swing.JLabel;

public class Frame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame();
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
	public Frame() {
		setTitle("KICK");
		setBackground(Color.LIGHT_GRAY);
		/* setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); */
		setBounds(100, 100, 451, 302);
		contentPane = new JPanel();
		contentPane.setForeground(Color.WHITE);
		contentPane.setToolTipText("");
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Start");
		btnNewButton.setBounds(12, 154, 103, 25);
		contentPane.add(btnNewButton);
		
		TextField textField = new TextField();
		textField.setBackground(Color.PINK);
		textField.setBounds(10, 110, 348, 24);
		contentPane.add(textField);
		
		JLabel lblThisToolWas = new JLabel("This tool was developed by huangbai and beimian");
		lblThisToolWas.setBounds(12, 11, 348, 93);
		contentPane.add(lblThisToolWas);
		
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("OK!");
				try {
					String message = textField.getText();
					Class var51 = Minecraft.getMinecraft().getSession().getClass();
					Field f = var51.getDeclaredFields()[0];
					f.setAccessible(true);
					f.set(Minecraft.getMinecraft().getSession(), message);
					ChatUtils.message("OK");
					ChatUtils.message("test");

				} catch (Exception var5) {
					var5.printStackTrace();

				}

				
			}
		});
	}
}
