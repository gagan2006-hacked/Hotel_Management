package Service;

import DBConnection.AccessLevel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JFrame {

    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;

    static int chances=0;

    ImageIcon i=new ImageIcon("D:\\hotel\\Hotel_Management\\assest\\icon.jpg");
    public LoginPage() {
        if (chances>=5){
            JOptionPane.showMessageDialog(this,"Sorry to many Tries, Try again later ");
            System.exit(0);
        }
        setTitle("User Login");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setIconImage(i.getImage());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);

        // Username Label
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("Username:"), c);

        // Username Field
        c.gridx = 1;
        userField = new JTextField(15);
        add(userField, c);

        // Password Label
        c.gridx = 0;
        c.gridy = 1;
        add(new JLabel("Password:"), c);

        // Password Field
        c.gridx = 1;
        passField = new JPasswordField(15);
        add(passField, c);

        // Login Button
        c.gridx = 1;
        c.gridy = 2;
        loginButton = new JButton("Login");
        add(loginButton, c);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chances>=5){
                    JOptionPane.showMessageDialog(null,"Sorry to many Tries, Try again later ");
                    System.exit(0);
                }
                String username = userField.getText();
                String password = String.valueOf(passField.getPassword());

                if(username.equals(AccessLevel.getAdmin()) && password.equals(AccessLevel.getAdHashPass())) {
                    JOptionPane.showMessageDialog(null, 
                        "Login Successful!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    HotelServicePage.main(new String[]{});
                } else {
                    JOptionPane.showMessageDialog(null, 
                        "Invalid username or password.", 
                        "Login Failed", 
                        JOptionPane.ERROR_MESSAGE);
                    chances++;
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
