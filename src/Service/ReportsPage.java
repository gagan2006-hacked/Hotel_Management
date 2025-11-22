package Service;

import DAO.BusinessReportRepository;
import Data.Insertion.BusinessReport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Month;
import java.time.Year;
import java.util.List;

public class ReportsPage extends JFrame {
    BusinessReportRepository reportRepository=new BusinessReportRepository();
   static ImageIcon i=new ImageIcon("D:\\hotel\\Hotel_Management\\assest\\icon.jpg");

   JPanel panel=new JPanel();
    public ReportsPage() {
        setTitle("ReportsPage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        add(panel);
        setIconImage(i.getImage());
        panel.setLayout(new BorderLayout());

        // -------------------- TOP: Generate Monthly Report --------------------
        JPanel generatePanel = new JPanel(new GridLayout(4, 2, 10, 10));
        generatePanel.setBorder(BorderFactory.createTitledBorder("Generate Monthly Report"));
        String[] yearsUP = new String[]{"2023", "2024", "2025", "2026"};
        String[] monthsUP=new String[]{
                "January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December"};
        JComboBox<String> yearBox = new JComboBox<>(yearsUP);
        JComboBox<String> monthBox = new JComboBox<>(monthsUP);
        JButton generateBtn = new JButton("Generate");

        generatePanel.add(new JLabel("Year:"));
        generatePanel.add(yearBox);
        generatePanel.add(new JLabel("Month:"));
        generatePanel.add(monthBox);
        generatePanel.add(new JLabel());
        generatePanel.add(generateBtn);

        panel.add(generatePanel, BorderLayout.NORTH);

        // -------------------- CENTER: Monthly/Annual Reports Table --------------------
        String[] columns = {"ID", "Year", "Month", "Revenue", "Expenses", "Profit/Loss", "Growth", "Generated At"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Reports List"));

        panel.add(scrollPane, BorderLayout.CENTER);

        // -------------------- BOTTOM: Buttons --------------------
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        String[] months = {
                "January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December"
        };

        String[] years = {
                "2014", "2015", "2016", "2017", "2018", "2019",
                "2020", "2021", "2022", "2023", "2024", "2025", "2026"
        };

        JComboBox<String> yearComboBox = new JComboBox<>(years);


        JComboBox<String> monthComboBox = new JComboBox<>(months);

        bottomPanel.add("view Monthly Report",monthComboBox);
        bottomPanel.add("Annual Summary",yearComboBox);
        JButton refresh=new JButton("Refresh");
        JButton dashBoard=new JButton("DashBoard");


        bottomPanel.add(refresh);
        bottomPanel.add(dashBoard);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        generateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==generateBtn){
                    int year=yearBox.getSelectedIndex();
                    if (year<=-1){
                        JOptionPane.showMessageDialog(null,"Select the Year");return;
                    }
                    int month=monthBox.getSelectedIndex();
                    if (month<=-1){
                        JOptionPane.showMessageDialog(null,"Select the Month");return;
                    }

                    String y=yearsUP[year];
                    String m=monthsUP[month];

                    Year yr=null;
                    Month mon=null;
                    try{
                        yr=Year.of(Integer.parseInt(y));
                        mon=Month.of(month+1);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,"Error Please try again ");
                        return;
                    }
                    if (reportRepository.genrateTheReport(yr,mon)){
                        JOptionPane.showMessageDialog(null,"Report Generated Successfully");
                    }else {
                        JOptionPane.showMessageDialog(null,"Error Please try again ");
                    }
                }
            }
        });

        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==refresh){
                    int y=yearComboBox.getSelectedIndex();
                    if (y<=-1){
                        JOptionPane.showMessageDialog(null,"Select the Year");return;
                    }
                    int m=monthComboBox.getSelectedIndex();
                    if (m<=-1){
                        JOptionPane.showMessageDialog(null,"Select the Month");return;
                    }

                    String year=years[y];
                    String month=months[m];

                    Year yr=null;
                    Month mon=null;

                    try {
                        yr=Year.of(Integer.parseInt(year));
                        mon=Month.of(m+1);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null,"Error Please try again ");
                        return;
                    }

                    BusinessReport report=reportRepository.getReportByYearMonth(yr,mon);
                    if (report==null){
                        if (reportRepository.genrateTheReport(yr,mon)){
                            JOptionPane.showMessageDialog(null,"Report Generated Successfully");
                        }else {
                            JOptionPane.showMessageDialog(null,"Error Please try again ");return;
                        }
                    }
                    report=reportRepository.getReportByYearMonth(yr,mon);
                    load(report);
                }
            }
            public void load(BusinessReport report){
                if (report==null){
                    JOptionPane.showMessageDialog(null,"Error Please try again ");return;
                }
//                {"ID", "Year", "Month", "Revenue", "Expenses", "Profit/Loss", "Growth", "Generated At"}
                DefaultTableModel model1=(DefaultTableModel) table.getModel();
                model1.addRow(new Object[]{report.getReport_id(),report.getYear(),report.getMonth(),report.getTotal_revenue(),report.getTotal_expenses(),report.getProfit_or_loss(),report.getGrowth_rate(),report.getGenerated_at()});
            }
        });
        dashBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==dashBoard){
                    dispose();
                    HotelServicePage.main(new String[]{});
                }
            }
        });
        setVisible(true);
    }

    public static void main(String[] args) {
        /*JFrame f = new JFrame("Reports Page");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(900, 600);
        f.add(new ReportsPage());
        f.setIconImage(i.getImage());
        f.setVisible(true);*/
        new ReportsPage().setVisible(true);
    }
}