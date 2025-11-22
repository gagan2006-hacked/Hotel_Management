package Service;

import DAO.CustomerRepository;
import Data.Insertion.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CustomerPage extends JFrame implements ActionListener {

    private JTextField txtName, txtPhone, txtEmail, txtAddress, txtSearch;
    private JButton btnAddCustomer, btnSearch, btnDashBoard;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    CustomerRepository repo;

    static ImageIcon i = new ImageIcon("D:\\hotel\\Hotel_Management\\assest\\icon.jpg");

    public CustomerPage() {
        super("Customer Page");   // frame title

        setLayout(new BorderLayout());
        setSize(1300, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(i.getImage());
        setLocationRelativeTo(null);

        // ---------- Top Panel – Add Customer Form ----------
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Customer"));
        repo = new CustomerRepository();

        txtName = new JTextField();
        txtPhone = new JTextField();
        txtEmail = new JTextField();
        txtAddress = new JTextField();

        formPanel.add(new JLabel("Name:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(txtPhone);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(txtAddress);

        btnAddCustomer = new JButton("Add Customer");
        btnDashBoard = new JButton("DashBoard");
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAddCustomer);
        btnPanel.add(btnDashBoard);

        // ---------- Search Panel ----------
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Customer"));
        txtSearch = new JTextField(20);
        btnSearch = new JButton("Search");
        searchPanel.add(new JLabel("Search by Name / Email:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // ---------- Customer Table ----------
        String[] columns = {"Customer ID", "Name", "Email", "Phone", "Created At"};
        tableModel = new DefaultTableModel(columns, 0);
        customerTable = new JTable(tableModel){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane tableScroll = new JScrollPane(customerTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Customer List"));

        // ADD PANELS TO FRAME
        add(formPanel, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.AFTER_LAST_LINE);
        add(searchPanel, BorderLayout.WEST);
        add(tableScroll, BorderLayout.CENTER);

        // Add Action Listeners
        btnAddCustomer.addActionListener(this);
        btnSearch.addActionListener(this);
        btnDashBoard.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddCustomer) {
            if (!txtEmail.getText().isBlank() &&
                    validEmail(txtEmail.getText()) &&
                    !repo.getCustomerByEmailConfirm(txtEmail.getText()) &&
                    !txtName.getText().isBlank()) {

                String nm = txtName.getText();
                String em = txtEmail.getText();
                String ph = txtPhone.getText();
                String address = txtAddress.getText();

                Customer customer = new Customer(nm, em, ph, address);
                if (repo.addCustomer(customer)) {
                    JOptionPane.showMessageDialog(null, "Customer Added Successfully");
                } else {
                    JOptionPane.showMessageDialog(null, "Error Adding Customer. Try Again");
                }

            } else {
                if (txtEmail.getText().isBlank()) {
                    JOptionPane.showMessageDialog(null, "Email is Blank");
                } else if (!validEmail(txtEmail.getText())) {
                    JOptionPane.showMessageDialog(null, "Enter a Valid Email");
                } else if (repo.getCustomerByEmailConfirm(txtEmail.getText())) {
                    JOptionPane.showMessageDialog(null, "Email Already Exists");
                } else if (txtName.getText().isBlank()) {
                    JOptionPane.showMessageDialog(null, "Customer Name is Blank");
                }
            }

        } else if (e.getSource() == btnSearch) {

            if (validEmail(txtSearch.getText())) {
                Customer customer = repo.getCustomerByEmail(txtSearch.getText());
                if (customer == null) {
                    JOptionPane.showMessageDialog(null, "No Customer Found");
                } else {
                    List<Customer> list = new ArrayList<>();
                    list.add(customer);
                    loadCustomer(list);
                }
            } else {
                if (!txtSearch.getText().isBlank()) {
                    List<Customer> list = repo.getCustomerByName(txtSearch.getText());
                    loadCustomer(list);
                } else {
                    JOptionPane.showMessageDialog(null, "Name/Email is Blank");
                }
            }

        } else if (e.getSource() == btnDashBoard) {
            dispose();
            HotelServicePage.main(new String[]{});
        }
    }

    public void loadCustomer(List<Customer> list) {
        if (list.isEmpty()) return;

        tableModel.setRowCount(0);
        for (Customer c : list) {
            tableModel.addRow(new Object[]{
                    c.getCustomer_id(),
                    c.getName(),
                    c.getEmail(),
                    c.getPhone(),
                    c.getCreated_at()
            });
        }
    }

    public boolean validEmail(String email) {
        if (email == null) return false;

        email = email.trim();
        if (email.isBlank()) return false;

        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(regex);
    }

    public static void main(String[] args) {
        new CustomerPage().setVisible(true);
    }
}
