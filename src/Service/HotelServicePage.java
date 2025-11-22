package Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class HotelServicePage extends JFrame implements ActionListener {
    ImageIcon i=new ImageIcon("D:\\hotel\\Hotel_Management\\assest\\icon.jpg");
    JButton roomBtn,customerBtn,staffBtn,bookingBtn,paymentBtn,reportBtn,signOut;
    public HotelServicePage() {
        setTitle("Hotel Management System - DashBoard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(i.getImage());

        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(32, 136, 203));
        header.setPreferredSize(new Dimension(900, 70));
        JLabel title = new JLabel("Hotel Service Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Menu Panel
        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(200, 600));
        menuPanel.setLayout(new GridLayout(8, 1, 5, 5));
        menuPanel.setBackground(new Color(245, 245, 245));

        roomBtn = new JButton("Manage Rooms");
        roomBtn.setFocusable(false);

        customerBtn = new JButton("Customers");
        customerBtn.setFocusable(false);

        staffBtn = new JButton("Staff");
        staffBtn.setFocusable(false);

        bookingBtn = new JButton("Bookings");
        bookingBtn.setFocusable(false);

        paymentBtn = new JButton("Payments");
        paymentBtn.setFocusable(false);

        reportBtn = new JButton("Reports");
        reportBtn.setFocusable(false);

        signOut=new JButton("Sign out");
        signOut.setFocusable(false);

        menuPanel.add(roomBtn);
        menuPanel.add(customerBtn);
        menuPanel.add(staffBtn);
        menuPanel.add(bookingBtn);
        menuPanel.add(paymentBtn);
        menuPanel.add(reportBtn);
        menuPanel.add(signOut);

        add(menuPanel, BorderLayout.WEST);

        roomBtn.addActionListener(this);
        customerBtn.addActionListener(this);
        staffBtn.addActionListener(this);
        bookingBtn.addActionListener(this);
        paymentBtn.addActionListener(this);
        reportBtn.addActionListener(this);
        signOut.addActionListener(this);



        // Content Panel
        JPanel content = new JPanel(new BorderLayout());
        JLabel placeholder = new JLabel("Select an option from the left menu", SwingConstants.CENTER);
        placeholder.setFont(new Font("Arial", Font.PLAIN, 22));
        content.add(placeholder, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==roomBtn){
            ManageRoomsPage.main(new String[]{});
        } else if (e.getSource() == customerBtn) {
            CustomerPage.main(new String[]{});
        } else if (e.getSource() == staffBtn) {
            StaffPage.main(new String[]{});
        } else if (e.getSource()==bookingBtn||e.getSource()==paymentBtn) {
            BookingsPaymentsPage.main(new String[]{});
        } else if (e.getSource() == reportBtn) {
            ReportsPage.main(new String[]{});
        } else if (e.getSource()==signOut) {
            LoginPage.main(new String[]{});
        }
        dispose();
    }




    public static void main(String[] args) {
        new HotelServicePage().setVisible(true);
    }


}