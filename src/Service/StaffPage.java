package Service;

import DAO.StaffRepository;
import Data.Enums.StaffRole;
import Data.Enums.StaffStatus;
import Data.Insertion.Staff;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class StaffPage extends JFrame implements ActionListener {
    StaffRepository repo=new StaffRepository();
    Staff staff,staffUp;
    boolean add=false,update;


    JTable staffTable;
    JButton addStaffBtn,saveStaffBtn,submitBtn,updateBtn,deleteBtn,statusBtn,searchBtn,refreshBtn,dashBoard;
    JComboBox<String> searchChoice, role;
    JTextField nameField,salaryField,contactField,joiningDateField;
    ImageIcon i=new ImageIcon("D:\\Hotel_Mangement\\icon.jpg");
    String[] item = {
            "Cleaner","Manager","Receptionist",
    };
    String[] col = {"ID", "Name", "Role", "Salary", "Status", "Contact"};
    String[] items = {
            "Search by ID", "Search all Active", "Search by Role", "Search All ",
    };

    public StaffPage() {

        // ----------- FRAME SETTINGS -----------
        setTitle("Staff Management");
        setSize(1150, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setIconImage(i.getImage());


        Color primary = new Color(52, 152, 219);      // Blue
        Color secondary = new Color(236, 240, 241);   // Light gray
        Color bg = new Color(250, 250, 250);          // Soft background
        Color darkText = new Color(44, 62, 80);

        getContentPane().setBackground(bg);

        // ----------------------------------------------------------
        // TOP PANEL
        // ----------------------------------------------------------
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        topPanel.setBackground(primary);

        addStaffBtn = styledButton("Add Staff", Color.WHITE, primary);
        refreshBtn=styledButton("Refresh", Color.WHITE, new Color(155, 89, 182));
        dashBoard=styledButton("DashBoard",Color.white,new Color(241, 196, 15));

        topPanel.add(dashBoard);
        topPanel.add(refreshBtn);
        topPanel.add(addStaffBtn);

        add(topPanel, BorderLayout.NORTH);

        // ----------------------------------------------------------
        // LEFT FORM PANEL
        // ----------------------------------------------------------
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Staff"));
        formPanel.setBackground(bg);
        formPanel.setPreferredSize(new Dimension(350, 0));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        nameField = styledTextField();
        nameField.setText("Enter you name");

        role=new JComboBox<>(item);
        role.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        salaryField = styledTextField();
        salaryField.setText("Enter the Salary");
        contactField = styledTextField();
        contactField.setText("Enter the Contact");
        joiningDateField = styledTextField();
        joiningDateField.setText("Enter the Date");

        formPanel.add(new JLabel("Name:", JLabel.RIGHT));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Role:", JLabel.RIGHT));
        formPanel.add(role);

        formPanel.add(new JLabel("Salary:", JLabel.RIGHT));
        formPanel.add(salaryField);

        formPanel.add(new JLabel("Contact:", JLabel.RIGHT));
        formPanel.add(contactField);

        formPanel.add(new JLabel("Joining Date (YYYY-MM-DD):", JLabel.RIGHT));
        formPanel.add(joiningDateField);

        saveStaffBtn = styledButton("Save Staff", Color.WHITE, new Color(46, 204, 113));
        formPanel.add(new JLabel());
        formPanel.add(saveStaffBtn);

        add(formPanel, BorderLayout.WEST);

        // ----------------------------------------------------------
        // TABLE PANEL
        // ----------------------------------------------------------

        DefaultTableModel staffModel = new DefaultTableModel(col, 0);
        staffTable = new JTable(staffModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        staffTable.setRowHeight(28);
        staffTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        staffTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        staffTable.getTableHeader().setBackground(primary);
        staffTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane tablePane = new JScrollPane(staffTable);
        tablePane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(tablePane, BorderLayout.CENTER);

        // ----------------------------------------------------------
        // BOTTOM ACTION PANEL
        // ----------------------------------------------------------

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        actionPanel.setBackground(bg);

        submitBtn=styledButton("Submit",Color.white,primary);
        updateBtn = styledButton("Update Staff", Color.WHITE, new Color(241, 196, 15));
        deleteBtn = styledButton("Delete Staff", Color.WHITE, new Color(231, 76, 60));
        statusBtn = styledButton("Activate / Deactivate", Color.WHITE, new Color(155, 89, 182));




        searchChoice = new JComboBox<>(items);
        searchChoice.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchChoice.setPreferredSize(new Dimension(180, 40));

        searchBtn=styledButton("Search",Color.white,primary);


// Add bottom container to frame
        actionPanel.add(submitBtn);
        actionPanel.add(updateBtn);
        actionPanel.add(deleteBtn);
        actionPanel.add(statusBtn);
        actionPanel.add(searchChoice);
        actionPanel.add(searchBtn);
        add(actionPanel, BorderLayout.SOUTH);

        dashBoard.addActionListener(this);
        refreshBtn.addActionListener(this);
        addStaffBtn.addActionListener(this);
        saveStaffBtn.addActionListener(this);
        submitBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        statusBtn.addActionListener(this);
        searchBtn.addActionListener(this);
    }


    // --------------------------------------------------------------
    // STYLING HELPERS
    // --------------------------------------------------------------

    private JButton styledButton(String text, Color fg, Color bg) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(160, 40));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return btn;
    }

    private JTextField styledTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return tf;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==dashBoard){
            dispose();
            HotelServicePage.main(new String[]{});
        } else if (e.getSource() == refreshBtn) {


        } else if (e.getSource()==addStaffBtn){
//            nameField,roleField,salaryField,contactField,joiningDateField
//            JOptionPane.showMessageDialog(null," ");
            String name=nameField.getText(),salaryT=salaryField.getText(),contact=contactField.getText(),join=joiningDateField.getText();
            if (name.equalsIgnoreCase("Enter you name") ||salaryT.equalsIgnoreCase("Enter the Salary") ||contact.equalsIgnoreCase("Enter the Contact")||join.equalsIgnoreCase("Enter the Date (YYYY-MM-DD)"))
            {
                JOptionPane.showMessageDialog(null,"Enter all the Data correctly in the form");
                return;
            }
            double sal=0;
            try {
                sal+=Double.parseDouble(salaryT);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,"Enter the correct salary ");
                return;
            }
            Date date=null;
            try {
                date=Date.valueOf(join);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,"Enter the correct Date (YYYY-MM-DD) ");
                return;
            }
            staff=new Staff(name, StaffRole.setValue(item[role.getSelectedIndex()]),sal,contact,date, StaffStatus.Active);
            add=true;
        }

        else if (e.getSource() == saveStaffBtn) {
            if (staff!=null) {
                if (add) {
                    if (repo.addStaff(staff)) {
                        JOptionPane.showMessageDialog(null, "Staff added Successfully");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");
                    }
                    staff = null;
                    add=false;
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Use the Add staff or Update  button to get Your input verified");
        }

        else if (e.getSource() == updateBtn) {
           int row= staffTable.getSelectedRow();
           if (row<=-1){
               JOptionPane.showMessageDialog(this, "Select a Staff to update.");
               return;
           }
           int id= (int) staffTable.getValueAt(row,0);
           Staff staff=repo.getStaffById(id);

           nameField.setText(staff.getName());
           salaryField.setText(staff.getSalary()+"");
           contactField.setText(staff.getContact());
           joiningDateField.setText(staff.getHire_data()+"");
           role.setSelectedItem(staff.getRole().getValue());
           JOptionPane.showMessageDialog(null,"Update the Value of the selected Staff in form");
           staffUp=new Staff(staff.getStaff_id());
           update=true;
        } else if (e.getSource() == submitBtn) {
            if (update){
                String name=nameField.getText(),salaryT=salaryField.getText(),contact=contactField.getText(),join=joiningDateField.getText();
                double sal=0;
                try {
                    sal+=Double.parseDouble(salaryT);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,"Enter the correct salary ");
                    return;
                }
                Date date=null;
                try {
                    date=Date.valueOf(join);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Enter the correct Date (YYYY-MM-DD) ");
                    return;
                }


                this.staffUp=new Staff(staffUp.getStaff_id(),name, StaffRole.setValue(item[role.getSelectedIndex()]),sal,contact,date, StaffStatus.Active);

                String s=JOptionPane.showInputDialog("Do you want to update the Staff data");
                if ((s.equalsIgnoreCase("yes")||s.equalsIgnoreCase("y"))&&repo.updateStaff(staffUp)){
                    JOptionPane.showMessageDialog(null, "Staff Updated Successfully");
                }else {
                    JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");
                }
                staffUp=null;
                update=false;
            }
        } else if (e.getSource() == deleteBtn) {
            int row= staffTable.getSelectedRow();
            if (row<=-1){
                JOptionPane.showMessageDialog(this, "Select a Staff to update.");
                return;
            }
            int id= (int) staffTable.getValueAt(row,0);

            if (repo.deleteStaff(id)){
                JOptionPane.showMessageDialog(null, "Staff Deleted Successfully");
            }else {
                JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");
            }
        } else if (e.getSource()==statusBtn) {
            int row= staffTable.getSelectedRow();
            if (row<=-1){
                JOptionPane.showMessageDialog(this, "Select a Staff to update.");
                return;
            }
            int id= (int) staffTable.getValueAt(row,0);
            staff=repo.getStaffById(id);

            if (staff.getStatus().getValue()=="active"){
                staff.setStatus(StaffStatus.InActive);
            }else {
                staff.setStatus(StaffStatus.Active);
            }

            if (repo.updateStaff(staff)){
                JOptionPane.showMessageDialog(null, "Staff Status Updated Successfully");
            }else {
                JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");
            }
            staff = null;
        } else if (e.getSource()==searchBtn) {
            int index=searchChoice.getSelectedIndex();
            if (index==0){
                String idT=JOptionPane.showInputDialog("Enter  the ID of the staff");
                if (idT==null){
                    JOptionPane.showMessageDialog(null,"Error occurred while input");
                    return;
                }
                int id=0;
                try
                {
                    id+=Integer.parseInt(idT);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,"Enter the correct ID ");
                }
                Staff s=repo.getStaffById(id);
                if (s==null){
                    JOptionPane.showMessageDialog(null,"There is no user With this ID");
                    return;
                }
                List<Staff>list=new ArrayList<>();list.add(s);
                loadStaff(list);
            } else if (index == 1) {
               refresh();
            } else if (index==2) {
                JOptionPane.showMessageDialog(null,"Select the Role using the Role in Form ");
                List<Staff>list=repo.getStaffByRole(StaffRole.setValue(item[role.getSelectedIndex()]));
                loadStaff(list);
            } else if (index == 3) {
                List<Staff>list=repo.getAllStaff();
                loadStaff(list);
            }
            return;
        }
        refresh();
    }
    public void refresh(){
        List<Staff>list=repo.getAllActiveStaff();
        loadStaff(list);
    }

    private void  loadStaff(List<Staff> list) {
        DefaultTableModel model = (DefaultTableModel) staffTable.getModel();
        model.setRowCount(0);   // clear old rows

        for (Staff s : list) {
            if (s!=null) {
                model.addRow(new Object[]{
                        s.getStaff_id(),
                        s.getName(),
                        s.getRole().getValue(),
                        s.getSalary(),
                        s.getStatus().getValue(),
                        s.getContact()
                });
            }
        }
    }

    public static void main(String[] args) {
        new StaffPage().setVisible(true);
    }
}
