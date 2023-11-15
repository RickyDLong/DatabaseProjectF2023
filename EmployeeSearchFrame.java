/**
 * Author: Lon Smith, Ph.D.
 * Description: This is the framework for the database program. Additional requirements and functionality
 *    are to be built by you and your group.
 */


import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.util.Properties;
import java.io.FileReader;
import java.sql.*;
import java.io.IOException;
import java.util.ArrayList;

public class EmployeeSearchFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtDatabase;
    private JList<String> lstDepartment;
    private DefaultListModel<String> department = new DefaultListModel<String>();
    private JList<String> lstProject;
    private DefaultListModel<String> project = new DefaultListModel<String>();
    private JTextArea textAreaEmployee;
    Connection connection = null;

    /**
     * The main method to launch the application.
     */
    public static void main(String[] args) 
    {
        EventQueue.invokeLater(new Runnable() 
        {
            public void run() 
            {
                try 
                {
                    EmployeeSearchFrame frame = new EmployeeSearchFrame();
                    frame.setVisible(true);
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the EmployeeSearchFrame.
     */
    public EmployeeSearchFrame() throws IOException
    {
        //code for linking properties file
        FileReader reader = new FileReader("database.props");
        Properties properties = new Properties();
        properties.load(reader);



        

        //stuff for the gui
        setTitle("Employee Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 347);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Label for the "Database" input
        JLabel lblNewLabel = new JLabel("Database:");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblNewLabel.setBounds(21, 23, 59, 14);
        contentPane.add(lblNewLabel);

        // Text field to input the database name
        txtDatabase = new JTextField();
        txtDatabase.setBounds(90, 20, 193, 20);
        contentPane.add(txtDatabase);
        txtDatabase.setColumns(10);

        // Button to fill the department and project lists
        JButton btnDBFill = new JButton("Fill");
        btnDBFill.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                if(txtDatabase.getText() != null)
                {

                    try
                    {
                        //properties variables
                        String dbdriver = properties.getProperty("db.driver");
                        String dbuser = properties.getProperty("db.user");
                        String dbpassword = properties.getProperty("db.password");
                        String dburl = properties.getProperty("db.url") + txtDatabase.getText() + "?useSSL=false";
                        
                        //create connection
                        Class.forName(dbdriver).newInstance();
                        connection = DriverManager.getConnection(dburl, dbuser, dbpassword);
                        
                        //retrieve project names
                        ResultSet result = connection.createStatement().executeQuery("SELECT * FROM PROJECT;");
                        ArrayList<String> projectsList = new ArrayList<>();
                        while(result.next())
                            projectsList.add(result.getString("Pname"));
                        Object[] prj = projectsList.toArray();
                        
                        //retrieve department names
                        result = connection.createStatement().executeQuery("SELECT * FROM DEPARTMENT");
                        ArrayList<String> departmentsList = new ArrayList<>();
                        while(result.next())
                            departmentsList.add(result.getString("Dname"));
                        Object[] dept = departmentsList.toArray();

                        // Populate the department and project lists
                        for (int i = 0; i < dept.length; i++) {
                            department.addElement((String) dept[i]);
                        }
                        for (int j = 0; j < prj.length; j++) {
                            project.addElement((String) prj[j]);
                        }

                        connection.close();

                        
 
                    }
                    catch(Exception ex)
                    {
                        textAreaEmployee.setText("Exception: " + ex.getMessage());
                    }

                }
                else
                {
                    textAreaEmployee.setText("Error: must enter the name of the database to connect and search!");
                }
            }
        });
        btnDBFill.setFont(new Font("Times New Roman", Font.BOLD, 12));
        btnDBFill.setBounds(307, 19, 68, 23);
        contentPane.add(btnDBFill);

        // Label for the "Department" list
        JLabel lblDepartment = new JLabel("Department");
        lblDepartment.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblDepartment.setBounds(52, 63, 89, 14);
        contentPane.add(lblDepartment);

        // Label for the "Project" list
        JLabel lblProject = new JLabel("Project");
        lblProject.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblProject.setBounds(255, 63, 47, 14);
        contentPane.add(lblProject);

        // Department list with scrollable view
        JScrollPane departmentScrollPane = new JScrollPane();
        departmentScrollPane.setBounds(36, 84, 172, 40);
        contentPane.add(departmentScrollPane);
        lstDepartment = new JList<String>(new DefaultListModel<String>());
        departmentScrollPane.setViewportView(lstDepartment);
        lstDepartment.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lstDepartment.setModel(department);

        // "Not" checkbox for Department
        JCheckBox chckbxNotDept = new JCheckBox("Not");
        chckbxNotDept.setBounds(71, 133, 59, 23);
        contentPane.add(chckbxNotDept);

        // "Not" checkbox for Project
        JCheckBox chckbxNotProject = new JCheckBox("Not");
        chckbxNotProject.setBounds(270, 133, 59, 23);
        contentPane.add(chckbxNotProject);

        // Project list with scrollable view
        JScrollPane projectScrollPane = new JScrollPane();
        projectScrollPane.setBounds(225, 84, 150, 42);
        contentPane.add(projectScrollPane);
        lstProject = new JList<String>(new DefaultListModel<String>());
        projectScrollPane.setViewportView(lstProject);
        lstProject.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lstProject.setModel(project);

        // Label for "Employee" text area
        JLabel lblEmployee = new JLabel("Employee");
        lblEmployee.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblEmployee.setBounds(52, 179, 89, 14);
        contentPane.add(lblEmployee);

        // Button to perform a search
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // For demonstration, display hardcoded employee names in the text area
                textAreaEmployee.setText("John Smith\nFranklin Wong");
            }
        });
        btnSearch.setBounds(80, 276, 89, 23);
        contentPane.add(btnSearch);

        // Button to clear the text area
        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Clear the text area
                textAreaEmployee.setText("");
                // Clear the department and project lists
                department.removeAllElements();
                project.removeAllElements();
                // Uncheck the "Not" checkboxes
                chckbxNotDept.setSelected(false);
                chckbxNotProject.setSelected(false);
            }
        });
        btnClear.setBounds(236, 276, 89, 23);
        contentPane.add(btnClear);

        // Text area to display employee names with scrollable view
        JScrollPane employeeScrollPane = new JScrollPane();
        employeeScrollPane.setBounds(36, 197, 339, 68);
        contentPane.add(employeeScrollPane);
        textAreaEmployee = new JTextArea();
        employeeScrollPane.setViewportView(textAreaEmployee);
    }
}
