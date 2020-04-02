package net.wurstclient.forge.utils.system;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class KickFrame extends JFrame {
	public KickFrame() {
	}

    private static final long serialVersionUID = 1L;
    static String path = null;

    public static void main(final String[] args) {

        // 画出窗口
        final JFrame frame = new KickFrame();
        frame.getContentPane().setLayout(null);
        frame.setTitle("myFrame");
        frame.setSize(400, 170);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 添加按钮
        final JButton btn1 = new JButton("浏览");
        final JButton btn2 = new JButton("确定");
        btn1.setBounds(80, 70, 60, 25);
        btn2.setBounds(240, 70, 60, 25);
        btn1.setVisible(true);
        btn2.setVisible(true);
        frame.getContentPane().add(btn1);
        frame.getContentPane().add(btn2);

        // 设置字体
        final Font font = new Font("宋体", Font.BOLD, 11);
        btn1.setFont(font);
        btn2.setFont(font);

        // 添加显示框
        final JLabel label = new JLabel("路径:");
        label.setBounds(60, 20, 50, 25);
        label.setVisible(true);
        label.setFont(font);
        frame.getContentPane().add(label);

        // 添加文本框
        final JTextField textField = new JTextField();
        textField.setBounds(120, 20, 190, 22);
        textField.setVisible(true);
        frame.getContentPane().add(textField);
        frame.setVisible(true);

        // 为按钮添加事件
        btn1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                final int returnVal = chooser.showOpenDialog(btn1);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    final File file = chooser.getSelectedFile();
                    path = file.getAbsolutePath();
                    textField.setText(path);
                }
            }
        });

        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final KickFrame myFrame = new KickFrame();
                myFrame.addInfo(path);
            }
        });

        // 为文本框添加事件
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (new File(textField.getText()).isDirectory()) {
                    path = textField.getText();
                } else {
                    final KickFrame myFrame = new KickFrame();
                    myFrame.addInfo("路径名错误");
                }

            }
        });
    }

    public void addInfo(final String string) {
        final JFrame f = new JFrame("提示");
        f.getContentPane().setLayout(null);
        f.setBounds(40, 40, 300, 100);
        f.setVisible(true);
        final JLabel label = new JLabel(string);
        label.setBounds(30, 20, 250, 20);
        final Font f1 = new Font("宋体", Font.BOLD, 12);
        label.setFont(f1);
        f.getContentPane().add(label);

    }
}
