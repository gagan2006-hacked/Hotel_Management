package Service;

import DAO.BookingRepository;
import DAO.CustomerRepository;
import DAO.PaymentRepository;
import DAO.RoomRepository;
import Data.Enums.BookingStatus;
import Data.Enums.PaymentMode;
import Data.Enums.PaymentStatus;
import Data.Enums.RoomStatus;
import Data.Insertion.Booking;
import Data.Insertion.Customer;
import Data.Insertion.Payment;
import Data.Insertion.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookingsPaymentsPage extends JFrame {
    CustomerRepository repository=new CustomerRepository();
    RoomRepository repo=new RoomRepository();

    BookingRepository bookRepo=new BookingRepository();
    PaymentRepository paymentRepository=new PaymentRepository();
    ImageIcon i=new ImageIcon("D:\\Hotel_Mangement\\icon.jpg");

    JTable table;
    JTable paymentTable;


    public BookingsPaymentsPage() {
        setLayout(new BorderLayout());
        setIconImage(i.getImage());
        JPanel panel=new JPanel(new BorderLayout());

        JPanel dashBoard=new JPanel();
        JButton dash=new JButton("DashBoard");
        dash.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==dash){
                    dispose();
                    HotelServicePage.main(new String[]{});
                }
            }
        });
        dashBoard.add(dash);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Bookings", createBookingsPanel());
        tabs.addTab("Payments", createPaymentsPanel());
        tabs.addTab("DashBoard",dashBoard);

        panel.add(tabs, BorderLayout.CENTER);

        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setVisible(true);
    }

    // ----------------- BOOKINGS PANEL -----------------
    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create Booking Form
        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Create Booking"));

        JTextField customerId = new JTextField();
        JTextField roomId = new JTextField();
        JTextField checkIn = new JTextField();
        JTextField checkOut = new JTextField();

        form.add(new JLabel("Customer ID:"));
        form.add(customerId);

        form.add(new JLabel("Room ID:"));
        form.add(roomId);

        form.add(new JLabel("Check-in Date:"));
        form.add(checkIn);

        form.add(new JLabel("Check-out Date:"));
        form.add(checkOut);

        JButton btnCreate = new JButton("Create Booking");
        form.add(new JLabel());
        form.add(btnCreate);
        btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==btnCreate){
                    if (customerId.getText().isBlank()){
                        JOptionPane.showMessageDialog(null,"Customer ID is Blank");
                        return;
                    }
                    String cu=customerId.getText();
                    int cuId=0;
                    try
                    {
                        cuId+=Integer.parseInt(cu);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null,"Enter a valid Id");
                        return;
                    }
                    Customer customer=repository.getCustomerById(cuId);
                    if (customer==null){
                        JOptionPane.showMessageDialog(null,"Invalid Customer ID");
                        return;
                    }

                    if (roomId.getText().isBlank()){
                        JOptionPane.showMessageDialog(null,"Room ID is Blank");
                        return;
                    }

                    String ro=roomId.getText();
                    int roId=0;
                    try
                    {
                        roId+=Integer.parseInt(ro);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null,"Enter a valid Id");
                        return;
                    }
                    Room room=repo.getRoomById(roId);
                    if (room==null){
                        JOptionPane.showMessageDialog(null,"Invalid Room ID");
                        return;
                    }
                    if (room.getStatus()!= RoomStatus.Available){
                        JOptionPane.showMessageDialog(null,"Room is not Available Now");return;
                    }

                    room.setStatus(RoomStatus.Booked);
                    String check=checkIn.getText();
                    Date chec=null;
                    try {
                        chec=Date.valueOf(check);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,"Enter a Valid Check IN Date in this Format (YYYY-MM-DD)");
                        return;
                    }
                    String out=checkOut.getText();
                    Date ou=null;
                    try{
                        ou=Date.valueOf(out);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,"Enter a Valid Check Out Date in this Format (YYYY-MM-DD)");
                        return;
                    }
                    if (chec.compareTo(ou)>0){
                        JOptionPane.showMessageDialog(null,"Enter the Correct Check-In, Check-IN is Done After CheckOut");
                        return;
                    }

                    BookingStatus status;
                    if (chec.compareTo(new Date(System.currentTimeMillis()))==0){
                        status=BookingStatus.Checked_in;
                    }else {
                        status=BookingStatus.Confirmed;
                    }
                    int rate=(ou.compareTo(chec)==0)?1:ou.compareTo(chec);

                    Booking booking=new Booking(customer.getCustomer_id(),room.getRoomId(),chec,ou,status,room.getPrice()*rate);
                    if (repo.updateRoomStatus(room.getRoom_number(),room.getRoomId(),room.getStatus())&&bookRepo.addBooking(booking)){
                        JOptionPane.showMessageDialog(null, "Booking  added Successfully");
                    }else {
                        JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");
                    }
                }
            }
        });

        panel.add(form, BorderLayout.NORTH);

        // Booking Table
        String[] col = {"ID", "Customer", "Room", "Check-in", "Check-out", "Status","Total Amount"};
        DefaultTableModel tableModel = new DefaultTableModel(col, 0);
        table = new JTable(tableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Booking List"));

        panel.add(scroll, BorderLayout.CENTER);

        // Action Buttons
        JButton mod=new JButton("Modify Booking");
        mod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==mod){
                    int row=table.getSelectedRow();
                    if (row<=-1){
                        JOptionPane.showMessageDialog(null, "Select a Booking  to update.");
                        return;
                    }
                    int id=(int)table.getValueAt(row,0);
                    Booking book=bookRepo.getBookingById(id);
                    String s="";
                    s+=JOptionPane.showInputDialog("Enter The Booking Field to Modify ( Customer ID , Room ID , Check-in Date , Check-out Date , booking_status)");
                    if (s.equalsIgnoreCase("Customer ID")){

                        String cu="";
                        cu+=JOptionPane.showInputDialog("Enter the new Customer ID");
                        int cuId=0;
                        try
                        {
                            cuId+=Integer.parseInt(cu);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null,"Enter a valid Id");
                            return;
                        }
                        Customer customer=repository.getCustomerById(cuId);
                        if (customer==null){
                            JOptionPane.showMessageDialog(null,"Invalid Customer ID");
                            return;
                        }
                        book.setCustomer_id(cuId);

                        if (bookRepo.updateBooking(book)){
                            JOptionPane.showMessageDialog(null, "Booking  Update Successfully");
                        }else {
                            JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");
                        }

                    } else if (s.equalsIgnoreCase("Room ID")) {

                        String ro="";
                        ro+=JOptionPane.showInputDialog("Enter the new Room ID");
                        int roId=0;
                        try
                        {
                            roId+=Integer.parseInt(ro);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null,"Enter a valid Id");
                            return;
                        }
                        Room room=repo.getRoomById(roId);

                        if (room==null){
                            JOptionPane.showMessageDialog(null,"Invalid Room ID");
                            return;
                        }
                        book.setRoom_id(room.getRoomId());
                        if (bookRepo.updateBooking(book)){
                            JOptionPane.showMessageDialog(null, "Booking  Update Successfully");
                        }else {
                            JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");
                        }

                    } else if (s.equalsIgnoreCase("Check-in") || s.equalsIgnoreCase("Check in") || s.equalsIgnoreCase("Check-in Date ")) {
                        String check="";
                        check+=JOptionPane.showInputDialog("Enter the new Check-IN Date");
                        Date chec=null;
                        try {
                            chec=Date.valueOf(check);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null,"Enter a Valid Check IN Date in this Format (YYYY-MM-DD)");
                            return;
                        }
                        if (chec.compareTo(book.getCheck_out_date())>0){
                            JOptionPane.showMessageDialog(null,"Enter the Correct Check-In, Check-IN is Done After CheckOut");
                            return;
                        }
                        book.setCheck_in_date(chec);
                        if (bookRepo.updateBooking(book)){
                            JOptionPane.showMessageDialog(null, "Booking  Update Successfully");
                        }else {
                            JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");
                        }
                    } else if (s.equalsIgnoreCase(" Check-out Date ") || s.equalsIgnoreCase("Check out") || s.equalsIgnoreCase(" Check-out ")) {
                        String out="";
                        out+=JOptionPane.showInputDialog("Enter the new Check-Out Date");
                        Date ou=null;
                        try{
                            ou=Date.valueOf(out);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null,"Enter a Valid Check Out Date in this Format (YYYY-MM-DD)");
                            return;
                        }
                        if (book.getCheck_in_date().compareTo(ou)>0){
                            JOptionPane.showMessageDialog(null,"Enter the Correct Check-Out, Check-Out is Done After CheckOut");
                            return;
                        }

                        book.setCheck_out_date(ou);
                        if (bookRepo.updateBooking(book)){
                            JOptionPane.showMessageDialog(null, "Booking  Update Successfully");
                        }else {
                            JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");
                        }
                    } else if (s.equalsIgnoreCase(" booking_status") || s.equalsIgnoreCase("booking status") || s.equalsIgnoreCase("bookingstatus")) {
                        String[] options = {"confirmed", "checked_in", "checked_out","cancelled"};
                        JComboBox<String> combo = new JComboBox<>(options);

                        Object[] message = {
                                "Select Booking Status:",
                                combo
                        };

                        int choice = JOptionPane.showConfirmDialog(null, message, "Booking", JOptionPane.OK_CANCEL_OPTION);

                        if (choice == JOptionPane.OK_OPTION) {
                            Room room=repo.getRoomById(book.getRoom_id());
                            book.setBooking_status(BookingStatus.setValue(options[combo.getSelectedIndex()]));
                            if (book.getBooking_status()==BookingStatus.Cancelled||book.getBooking_status()==BookingStatus.Checked_out){
                                room.setStatus(RoomStatus.Available);
                            }else {
                                room.setStatus(RoomStatus.Booked);
                            }
                            if (repo.updateRoomStatus(room.getRoom_number(),room.getRoomId(),room.getStatus())&&bookRepo.updateBooking(book)){
                                JOptionPane.showMessageDialog(null, "Booking  Update Successfully");
                                return;
                            }
                        }
                        JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");
                    }
                    loadBooking(bookRepo.getAllBookings());
                }

            }
        });
        JButton cancel=new JButton("Cancel Booking");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==cancel) {
                    int row = table.getSelectedRow();
                    if (row <= -1) {
                        JOptionPane.showMessageDialog(null, "Select a Booking  to Cancel.");
                        return;
                    }
                    int id=(int) table.getValueAt(row,0);
                    Booking book=bookRepo.getBookingById(id);
                    Room room=repo.getRoomById(book.getRoom_id());
                    room.setStatus(RoomStatus.Available);
                    if (repo.updateRoomStatus(room.getRoom_number(),room.getRoomId(),room.getStatus())&&bookRepo.cancelBooking(id)){
                        JOptionPane.showMessageDialog(null,"Booking is Canceled");
                        List<Booking>list=new ArrayList<>();
                        list.add(bookRepo.getBookingById(id));loadBooking(list);
                        return;
                    }
                    JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");
                }
            }
        });
        JButton view=new JButton("View Details");
        view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==view){
                    int row = table.getSelectedRow();
                    if (row <= -1) {
                        JOptionPane.showMessageDialog(null, "Select a Booking  to See.");
                        return;
                    }
                    int id=(int) table.getValueAt(row,0);
                    Booking booking=bookRepo.getBookingById(id);

                    String bigText = booking.toString();
                    JTextArea textArea = new JTextArea(15, 40); // rows, columns
                    textArea.setText(bigText);
                    textArea.setEditable(false);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    JOptionPane.showMessageDialog(null, scrollPane, "Information", JOptionPane.INFORMATION_MESSAGE);

                }
            }
        });

        String[] options = {
                "getAllBookings",
                "getBookingsByCustomer",
                "getTodayBookings",
                "isRoomAvailable"
        };

        JComboBox<String> combo = new JComboBox<>(options);
        combo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==combo){
                    int select=combo.getSelectedIndex();
                    if (select<=-1){
                        JOptionPane.showMessageDialog(null,"Select Search Option");
                        return;
                    }
                    String s=options[select];
                    if (select==0){
                        List<Booking> list=bookRepo.getAllBookings();
                        loadBooking(list);
                    } else if (select == 1) {
                        String cu="";
                        cu+=JOptionPane.showInputDialog("Enter the new Customer ID");
                        int cuId=0;
                        try
                        {
                            cuId+=Integer.parseInt(cu);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null,"Enter a valid Id");
                            return;
                        }
                        Customer customer=repository.getCustomerById(cuId);
                        if (customer==null){
                            JOptionPane.showMessageDialog(null,"Invalid Customer ID");
                            return;
                        }
                       List<Booking>list=bookRepo.getBookingsByCustomer(cuId);
                        loadBooking(list);
                    } else if (select == 2) {
                       List<Booking>list=bookRepo.getTodayBookings();
                       loadBooking(list);
                    } else {
                        String ro="";
                        ro+=JOptionPane.showInputDialog("Enter the new Room ID");
                        int roId=0;
                        try
                        {
                            roId+=Integer.parseInt(ro);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null,"Enter a valid Id");
                            return;
                        }
                        Room room=repo.getRoomById(roId);
                        if (room==null){
                            JOptionPane.showMessageDialog(null,"Invalid Room ID");
                            return;
                        }

                        String check="";
                        check+=JOptionPane.showInputDialog("Enter the Check-IN Date");
                        Date chec=null;
                        try {
                            chec=Date.valueOf(check);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null,"Enter a Valid Check IN Date in this Format (YYYY-MM-DD)");
                            return;
                        }
                        String out="";
                        out+=JOptionPane.showInputDialog("Enter the  Check-Out Date");
                        Date ou=null;
                        try{
                            ou=Date.valueOf(out);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null,"Enter a Valid Check Out Date in this Format (YYYY-MM-DD)");
                            return;
                        }

                        if (bookRepo.isRoomAvailable(roId,chec,ou)){
                            JOptionPane.showMessageDialog(null,"Room is Available");

                        }else {
                            JOptionPane.showMessageDialog(null,"Room is not  Available");
                        }
                    }
                }
            }
//            {"ID", "Customer", "Room", "Check-in", "Check-out", "Status"};

        });



        JPanel actions = new JPanel(new GridLayout(1, 3, 10, 10));
        actions.add(mod);
        actions.add(cancel);
        actions.add(view);
        actions.add(combo);

        panel.add(actions, BorderLayout.SOUTH);
        loadBooking(bookRepo.getTodayBookings());
        return panel;
    }
    private void loadBooking(List<Booking>list){
        DefaultTableModel model=(DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Booking b:list){
            if (b!=null){
                model.addRow(new Object[]{b.getBooking_id(),b.getCustomer_id(),b.getRoom_id(),b.getCheck_in_date(),b.getCheck_out_date(),b.getBooking_status(),b.getTotal_amount()});
            }
        }
    }






    // ----------------- PAYMENTS PANEL -----------------
    private JPanel createPaymentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Payment Form
        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Add Payment"));

        JTextField bookingId = new JTextField();
        JComboBox<String> mode = new JComboBox<>(new String[]{"CASH", "CARD", "UPI","Net Banking"});
        JTextField amount = new JTextField();

        form.add(new JLabel("Booking ID:"));
        form.add(bookingId);

        form.add(new JLabel("Payment Mode:"));
        form.add(mode);

        form.add(new JLabel("Amount:"));
        form.add(amount);

        JButton btnPay = new JButton("Add Payment");
        btnPay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==btnPay){
                    String b=bookingId.getText();
                    if (b.isBlank()){
                        JOptionPane.showMessageDialog(null,"Booking ID is Blank ");
                        return;
                    }

                    int bId=0;
                    try{
                        bId+=Integer.parseInt(b);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null,"Enter the Valid Booking ID");
                    }

                    Booking booking=bookRepo.getBookingById(bId);
                    if (booking==null){
                        JOptionPane.showMessageDialog(null,"Booking Id Does Not Exist");
                    }

                    int select=mode.getSelectedIndex();
                    if (select<=-1){
                        JOptionPane.showMessageDialog(null,"Choose a Payment Mode");
                        return;
                    }

                    PaymentMode paymentMode=null;
                    if (select==0){
                        paymentMode=PaymentMode.Cash;
                    } else if (select==1) {
                        paymentMode=PaymentMode.Card;
                    } else if (select == 2) {
                        paymentMode=PaymentMode.Upi;
                    }else {
                        paymentMode=PaymentMode.Netbanking;
                    }

                    String am=amount.getText();
                    double amt=0;
                    try{
                        amt+=Double.parseDouble(am);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null,"Enter the valid Amount");
                        return;
                    }
                    if (amt<0){
                        JOptionPane.showMessageDialog(null,"Enter the Positive  Amount ");
                        return;
                    }

                    double tp= Objects.requireNonNull(booking).getTotal_amount();
                    PaymentStatus status=null;
                    if (tp-amt<=0){
                        status=PaymentStatus.Completed;
                    }else if (tp>amt) {
                        status=PaymentStatus.Pending;
                    }

                    Payment payment=new Payment(booking.getBooking_id(),paymentMode,amt,status, LocalDateTime.now());
                    if (paymentRepository.createPayment(payment)){
                        JOptionPane.showMessageDialog(null,"Payment is Successfully Completed ");
                        loadPayment(paymentRepository.getAllPayments());
                        return;
                    }
                    JOptionPane.showMessageDialog(null,"Payment is Not Completed");
                }
            }
        });

        form.add(new JLabel());
        form.add(btnPay);

        panel.add(form, BorderLayout.NORTH);

        // Payment Table
        String[] col = {"ID", "Booking ID", "Amount", "Status", "Date","Amount Due"};
        DefaultTableModel paymentModel = new DefaultTableModel(col, 0);
        paymentTable = new JTable(paymentModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane scroll = new JScrollPane(paymentTable);
        scroll.setBorder(BorderFactory.createTitledBorder("Payment List"));

        panel.add(scroll, BorderLayout.CENTER);

        // Filter Buttons
        JPanel filter = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton completed=new JButton("Completed");
        completed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==completed){
                    int row=paymentTable.getSelectedRow();
                    if (row<=-1){
                        JOptionPane.showMessageDialog(null,"Selected Payment");return;
                    }
                    int id=(int) paymentTable.getValueAt(row,0);
                    Payment payment=paymentRepository.getPaymentById(id);
                    if (payment==null){
                        JOptionPane.showMessageDialog(null,"Payment Does Does not Exist");return;
                    }
                    if (paymentRepository.updatePaymentStatus(payment.getPayment_id(),PaymentStatus.Completed)){
                        JOptionPane.showMessageDialog(null,"Payment Status is Updated");
                        List<Payment> list = new ArrayList<>(paymentRepository.getCompletedPayments());
                        loadPayment(list);
                        return;
                    }
                    JOptionPane.showMessageDialog(null,"Error Please try again");
                }
            }
        });
        JButton pending =new JButton("Pending");
        pending.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==pending) {
                    int row = paymentTable.getSelectedRow();
                    if (row <= -1) {
                        JOptionPane.showMessageDialog(null, "Selected Payment");
                        return;
                    }
                    int id = (int) paymentTable.getValueAt(row, 0);
                    Payment payment = paymentRepository.getPaymentById(id);
                    if (payment == null) {
                        JOptionPane.showMessageDialog(null, "Payment Does Does not Exist");
                        return;
                    }
                    if (paymentRepository.updatePaymentStatus(payment.getPayment_id(), PaymentStatus.Pending)) {
                        JOptionPane.showMessageDialog(null, "Payment Status is Updated");
                        List<Payment>list=new ArrayList<>();
                        list.add(paymentRepository.getPaymentById(id));
                        loadPayment(list);
                        return;
                    }
                    JOptionPane.showMessageDialog(null, "Error Please try again");
                }
            }
        });

        String[] items = {
                "getAllPayments",
                "getPaymentsByMode",
                "getTotalRevenue",
                "getCompletedPayments"
        };

        JComboBox<String> paymentOptions = new JComboBox<>(items);
        paymentOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==paymentOptions){
                    int row=paymentOptions.getSelectedIndex();
                    if (row<=-1){
                        JOptionPane.showMessageDialog(null,"Select the Option to Search");return;
                    }
                    String s=items[row];
                    if (row==0){
                        List<Payment>list=paymentRepository.getAllPayments();
                        loadPayment(list);
                    } else if (row == 1) {
                        String b="";
                        b+=JOptionPane.showInputDialog("Enter The Payment Mode");
                        PaymentMode mode1=PaymentMode.setValue(b);
                        if (mode1==null){
                            JOptionPane.showMessageDialog(null,"Select the Correct Payment Mode");return;
                        }
                        List<Payment>list=paymentRepository.getPaymentsByMode(mode1);
                        loadPayment(list);
                    } else if (row == 2) {
                        double rev=paymentRepository.getTotalRevenue();
                        JOptionPane.showMessageDialog(null,"Total Revenue is "+rev);
                    } else {
                        List<Payment>list=paymentRepository.getCompletedPayments();
                        loadPayment(list);
                    }
                }
            }
        });


        filter.add(completed);
        filter.add(pending);
        filter.add(paymentOptions);

        panel.add(filter, BorderLayout.SOUTH);
        loadPayment(paymentRepository.getCompletedPayments());
        return panel;
    }
    private void loadPayment(List<Payment>list){
        DefaultTableModel model=(DefaultTableModel) paymentTable.getModel();
        model.setRowCount(0);
        for (Payment p:list){
            if (p!=null){
                double amtDue = 0;
                if (p.getPayment_status()==PaymentStatus.Pending) {
                    int bid = p.getBooking_id();
                    Booking booking = bookRepo.getBookingById(bid);
                    amtDue+=(booking.getTotal_amount()-p.getAmount());
                }
                model.addRow(new Object[]{p.getPayment_id(),p.getBooking_id(),p.getAmount(),p.getPayment_status(),p.getPayment_date(),amtDue});
            }
        }
    }


    public static void main(String[] args) {
        new BookingsPaymentsPage().setVisible(true);
    }
}