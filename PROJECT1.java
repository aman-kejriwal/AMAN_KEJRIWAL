//package sg;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class PROJECT1 {

    private JFrame frame;
    private JTextField user;
    private JPasswordField pass;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PROJECT1 window = new PROJECT1();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public PROJECT1() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 462, 389);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        user = new JTextField();
        user.setBounds(236, 75, 158, 37);
        frame.getContentPane().add(user);
        user.setColumns(10);

        pass = new JPasswordField();
        pass.setBounds(236, 171, 158, 37);
        frame.getContentPane().add(pass);

        JLabel lblNewLabel = new JLabel("User Name");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel.setBounds(42, 76, 134, 26);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblPassword.setBounds(42, 172, 134, 26);
        frame.getContentPane().add(lblPassword);

        JButton btnNewButton = new JButton("Login");
        // JButton btnNewButton = new JButton("Login");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String u = user.getText();
                String p = pass.getText();
                if (u.equals("AMAN") && p.equals("AMAN")) {
                    JOptionPane.showMessageDialog(null, "Logging successfully");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalied Username or Password!!");
                }
            }
        });
        btnNewButton.setBounds(161, 263, 117, 37);
        frame.getContentPane().add(btnNewButton);
    }
}