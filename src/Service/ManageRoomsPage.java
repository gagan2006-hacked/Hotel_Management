package Service;

import DAO.RoomRepository;
import Data.Enums.RoomStatus;
import Data.Enums.RoomType;
import Data.Insertion.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class ManageRoomsPage extends JFrame implements ActionListener {
    ImageIcon i=new ImageIcon("D:\\hotel\\Hotel_Management\\assest\\icon.jpg");
    RoomRepository roomRepository=new RoomRepository();
    JTextField roomNumberField,priceField;
    JComboBox<String> typeBox,statusBox,searchBox;
    JButton addBtn,updateBtn,deleteBtn,dashBoard ;
    String[] types=new String[]{"SINGLE", "DOUBLE", "SUITE", "DELUXE"};
    String[] status=new String[]{"AVAILABLE", "BOOKED", "MAINTENANCE"};
    String[] roomOptions = {
            "getRoomById",
            "getRoomByRoomNumber",
            "getRoomByType",
            "getRoomByPrice",
            "getRoomByStatus",
            "getAllRoom"
    };
    JTable roomTable;

    public ManageRoomsPage() {
        setTitle("Manage Rooms - Hotel Management System");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setIconImage(i.getImage());


        // Top Header
        JPanel header = new JPanel();
        header.setBackground(new Color(32, 136, 203));
        JLabel title = new JLabel("Manage Rooms");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // LEFT SIDE FORM PANEL
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Add / Update Room"));
        formPanel.setLayout(new GridLayout(10, 1, 5, 5));
        formPanel.setPreferredSize(new Dimension(250, 600));

        roomNumberField = new JTextField();
        typeBox = new JComboBox<>(types);
        priceField = new JTextField();
        statusBox = new JComboBox<>(status);

        addBtn = new JButton("Add Room");
        updateBtn = new JButton("Update Room");
        deleteBtn = new JButton("Delete Room");
        dashBoard=new JButton("DashBoard");
//        searchBtn =new JButton("Submit");

        formPanel.add(new JLabel("Room Number:"));
        formPanel.add(roomNumberField);

        formPanel.add(new JLabel("Room Type:"));
        formPanel.add(typeBox);

        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);

        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusBox);

        searchBox=new JComboBox<>(roomOptions);

        formPanel.add(addBtn);
        formPanel.add(updateBtn);
        formPanel.add(deleteBtn);
        formPanel.add(searchBox);
        formPanel.add(dashBoard);
        searchBox.addActionListener(this);


        add(formPanel, BorderLayout.WEST);

        // RIGHT SIDE TABLE PANEL
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("All Rooms"));

        String[] columns = {"Room ID", "Room Number", "Type", "Price", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        roomTable = new JTable(tableModel){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomTable.setDragEnabled(false);


        JScrollPane scrollPane = new JScrollPane(roomTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);
        addBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        dashBoard.addActionListener(this);
        loadRoom(roomRepository.getAllRoom());

    }

    public static void main(String[] args) {
        new ManageRoomsPage().setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==addBtn){
            String roomN=roomNumberField.getText();
            String price=priceField.getText();
            if (roomN.isBlank()||price.isBlank()){
                JOptionPane.showMessageDialog(null,"Enter the valid room or price");return;
            }
            double pr=0;
            try
            {   pr+=Double.parseDouble(price);
                if (pr<0){
                    JOptionPane.showMessageDialog(null,"Enter the Positive price");return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,"Enter a valid price");return;
            }
            int ro=typeBox.getSelectedIndex();
            if (ro<=-1){
                JOptionPane.showMessageDialog(null,"Select the Type ");return;
            }
            int st=statusBox.getSelectedIndex();
            if (st<=-1){
                JOptionPane.showMessageDialog(null,"Select the Status ");return;
            }
            RoomType roomType=RoomType.setValue(types[ro]);
            RoomStatus roomStatus=RoomStatus.setValue(status[st]);

            Room r=roomRepository.getRoomByRoomNumber(roomN);
            if (r!=null){
                JOptionPane.showMessageDialog(null,"Room already Exist");return;
            }

            Room room=new Room(roomN,roomType,pr,roomStatus);
            if (roomRepository.addRoom(room)){
                JOptionPane.showMessageDialog(null,"Room added Successfully ");
            }else {
                JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");
            }
        } else if (e.getSource() == updateBtn) {
            int row=roomTable.getSelectedRow();
            if (row<=-1) {
                JOptionPane.showMessageDialog(null, "Select the Room To Edit ");
                return;
            }
            String field=""+JOptionPane.showInputDialog("Enter Room Field to Edit");
            if (field.equalsIgnoreCase("Room Number")){
                int id=(int)roomTable.getValueAt(row,0);
                String roomNum=JOptionPane.showInputDialog("Enter the new RoomNumber");
                if (roomNum==null){
                    JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");return;
                }
                Room r=roomRepository.getRoomByRoomNumber(roomNum);
                if (r!=null){
                    JOptionPane.showMessageDialog(null, "This Room Number it already taken by other Room");return;
                }
                Room room=roomRepository.getRoomById(id);
                if (roomRepository.updateRoomNumber(room.getRoom_number(),room.getRoomId(),roomNum)){
                    JOptionPane.showMessageDialog(null,"Room Number is Updated");
                }else {
                    JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");
                }
            }

            else if (field.equalsIgnoreCase("Room Type")) {
                int id=(int)roomTable.getValueAt(row,0);
                String roomType=JOptionPane.showInputDialog("Enter the new Room Type ("+ Arrays.toString(types)+")");
                if (roomType==null){
                    JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");return;
                }
                Room room=roomRepository.getRoomById(id);
                RoomType type=RoomType.setValue(roomType);
                if (type==null){
                    JOptionPane.showMessageDialog(null,"Enter valid Room Type");return;
                }
                if (roomRepository.updateRoomType(room.getRoom_number(),room.getRoomId(),type)){
                    JOptionPane.showMessageDialog(null,"Room Type is Updated");
                }else {
                    JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");
                }
            }

            else if (field.equalsIgnoreCase("Price")) {
                int id=(int)roomTable.getValueAt(row,0);
                String price=JOptionPane.showInputDialog("Enter the new Room Price ");
                if (price==null){
                    JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");return;
                }
                Room room=roomRepository.getRoomById(id);
                double pr=0;
                try
                {   pr+=Double.parseDouble(price);
                    if (pr<0){
                        JOptionPane.showMessageDialog(null,"Enter the Positive price");return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,"Enter a valid price");return;
                }
                if (roomRepository.updateRoomPrice(room.getRoom_number(),room.getRoomId(),pr)){
                    JOptionPane.showMessageDialog(null,"Room Price is Updated");
                }else {
                    JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");
                }
            }

            else if (field.equalsIgnoreCase("status")) {
                int id=(int)roomTable.getValueAt(row,0);
                String roomStatus=JOptionPane.showInputDialog("Enter the new Room Status ("+ Arrays.toString(status)+")");
                if (roomStatus==null){
                    JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");return;
                }
                Room room=roomRepository.getRoomById(id);
                RoomStatus roomStatus1=RoomStatus.setValue(roomStatus);
                if (roomStatus1==null){
                    JOptionPane.showMessageDialog(null,"Enter valid Room Status");return;
                }
                if (roomRepository.updateRoomStatus(room.getRoom_number(), room.getRoomId(),roomStatus1)){
                    JOptionPane.showMessageDialog(null,"Room Status is Updated");
                }else {
                    JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");
                }
            }

        }

        else if (e.getSource() == deleteBtn) {
            int row=roomTable.getSelectedRow();
            if (row<=-1) {
                JOptionPane.showMessageDialog(null, "Select the Room To Delete ");
                return;
            }
            int id=(int)roomTable.getValueAt(row,0);
            Room room=roomRepository.getRoomById(id);
            String con=JOptionPane.showInputDialog("If you want to delete (yes/no) (y/n)");
            if (con==null){
                return;
            }
            boolean b=con.equalsIgnoreCase("yes")||con.equalsIgnoreCase("y");
            if (b&&roomRepository.deleteRoom(room.getRoom_number(),room.getRoomId())){
                JOptionPane.showMessageDialog(null,"Room Deleted Successfully ");
            }else {
                JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");
            }
        }

        else if (e.getSource() == searchBox) {
//             "getRoomById",
//            "getRoomByRoomNumber",
//            "getRoomByType",
//            "getRoomByPrice",
//            "getRoomByStatus",
//            "getAllRoom"
            String selected= Objects.requireNonNull(searchBox.getSelectedItem()).toString();
            if (selected.equalsIgnoreCase("getRoomById")){
                String roomI=JOptionPane.showInputDialog("Enter The Room Id");
                if (roomI==null){
                    JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");return;
                }
                int rid=0;
                try {
                    rid+=Integer.parseInt(roomI);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,"Enter the Valid Room Id");return;
                }
                Room room=roomRepository.getRoomById(rid);
                if (room!=null){
                    List<Room>list=new ArrayList<>();
                    list.add(room);
                    loadRoom(list);
                }
                JOptionPane.showMessageDialog(null,"Room Does Not Exist");
            } else if (selected.equalsIgnoreCase("getRoomByRoomNumber")) {
                String roomNum=JOptionPane.showInputDialog("Enter the new RoomNumber");
                if (roomNum==null){
                    JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");return;
                }
                Room room=roomRepository.getRoomByRoomNumber(roomNum);
                if (room!=null){
                    List<Room>list=new ArrayList<>();
                    list.add(room);
                    loadRoom(list);
                }
                JOptionPane.showMessageDialog(null,"Room Does Not Exist");
            } else if (selected.equalsIgnoreCase("getRoomByType")) {
                String roomType=JOptionPane.showInputDialog("Enter the new Room Type "+ Arrays.toString(types));
                if (roomType==null){
                    JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");return;
                }
                RoomType type=RoomType.setValue(roomType);
                if (type==null){
                    JOptionPane.showMessageDialog(null,"Enter valid Room Type");return;
                }
                List<Room>list=roomRepository.getRoomByType(type);
                loadRoom(list);
            } else if (selected.equalsIgnoreCase("getRoomByPrice")) {
                String price=JOptionPane.showInputDialog("Enter the new Room Price ");
                if (price==null){
                    JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");return;
                }
                double pr=0;
                try
                {   pr+=Double.parseDouble(price);
                    if (pr<0){
                        JOptionPane.showMessageDialog(null,"Enter the Positive price");return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,"Enter a valid price");return;
                }
                List<Room>list=roomRepository.getRoomByPrice(pr,0);
                loadRoom(list);
            } else if (selected.equalsIgnoreCase("getRoomByStatus")) {
                String roomStatus=JOptionPane.showInputDialog("Enter the new Room Status ("+ Arrays.toString(status)+")");
                if (roomStatus==null){
                    JOptionPane.showMessageDialog(null, "Error Has Occurred please try again later");return;
                }
                RoomStatus roomStatus1=RoomStatus.setValue(roomStatus);
                if (roomStatus1==null){
                    JOptionPane.showMessageDialog(null,"Enter valid Room Status");return;
                }
                List<Room>list=roomRepository.getRoomByStatus(roomStatus1);
                loadRoom(list);
            }

            else if (selected.equalsIgnoreCase("getAllRoom")) {
                List<Room>list=roomRepository.getAllRoom();
                loadRoom(list);
            }
        }

        else if (e.getSource()==dashBoard) {
            dispose();
            HotelServicePage.main(new String[]{});
        }
        loadRoom(roomRepository.getAllRoom());
    }
    public void loadRoom(List<Room> list){
        if (list==null||list.isEmpty()){
            return;
        }
        DefaultTableModel model=(DefaultTableModel) roomTable.getModel();
        model.setRowCount(0);
        for (Room room:list){
//            "Room ID", "Room Number", "Type", "Price", "Status"
            if (room!=null) {
                model.addRow(new Object[]{room.getRoomId(),room.getRoom_number(),room.getType(),room.getPrice(),room.getStatus()});
            }
        }
    }


}