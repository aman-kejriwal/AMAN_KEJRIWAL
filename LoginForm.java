// import java.sql.*;

// class login_db {
// 	Connection con;
// 	PreparedStatement pst;
// 	ResultSet rs;

// 	login_db() {
// 		try {

// 			// MAKE SURE YOU KEEP THE mysql_connector.jar file in java/lib folder
// 			// ALSO SET THE CLASSPATH
// 			Class.forName("com.mysql.cj.jdbc.Driver");
// 			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/loginform", "root", "root");
// 			pst = con.prepareStatement("select * from login where username=? and password=?");

// 		} catch (Exception e) {
// 			System.out.println(e);
// 		}
// 	}

// 	// ip:username,password
// 	// return boolean
// 	public Boolean checkLogin(String uname, String pwd) {
// 		try {

// 			pst.setString(1, uname); // this replaces the 1st "?" in the query for username
// 			pst.setString(2, pwd); // this replaces the 2st "?" in the query for password
// 			// executes the prepared statement
// 			rs = pst.executeQuery();
// 			if (rs.next()) {
// 				// TRUE if the query founds any corresponding data
// 				return true;
// 			} else {
// 				return false;
// 			}
// 		} catch (Exception e) {
// 			// TODO Auto-generated catch block
// 			System.out.println("error while validating" + e);
// 			return false;
// 		}
// 	}
// }
//package MyFrame;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

class MyFrame extends JFrame implements ActionListener {
	Container c;
	JLabel label1, label2;
	JTextField user;
	JPasswordField pass;

	JButton btn;

	MyFrame() {

		JLabel background;
		setSize(1200, 700);
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		ImageIcon img = new ImageIcon("Background.251-2514687_4k-city-wallpaper.jpg");
		background = new JLabel("", img, JLabel.CENTER);
		background.setBounds(0, 0, 1200, 700);
		add(background);

		setTitle("LOGIN FORM");
		setSize(400, 300);
		setLocation(100, 100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		c = getContentPane();
		c.setLayout(null);
		label1 = new JLabel("Username");
		label2 = new JLabel("Password");

		label1.setBounds(10, 50, 100, 20);
		label2.setBounds(10, 100, 100, 20);

		c.add(label1);
		c.add(label2);

		user = new JTextField();
		user.setBounds(120, 50, 120, 20);
		c.add(user);
		pass = new JPasswordField();
		pass.setBounds(120, 100, 120, 20);
		c.add(pass);

		btn = new JButton("Login");
		btn.setBounds(100, 150, 70, 30);
		c.add(btn);
		btn.addActionListener(this);
		setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("Username:" + user.getText());
		System.out.println("Password:" + pass.getText());
	}

}

final class LoginForm {

	public static void main(String[] args) {
		MyFrame frame = new MyFrame();

	}

}