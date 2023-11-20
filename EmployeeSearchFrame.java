import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;

/**
 * This program provides a GUI interface to search an employee database 
 * and display results.
 *
 * The database connection information is read from a properties file. 
 * The user enters a database name, and can select departments and projects
 * to search on. The results are displayed in a text area via GUI.
 */
public class EmployeeSearchFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtDatabase;
    private JList<String> lstDepartment;
    private DefaultListModel<String> department = new DefaultListModel<String>();
    private JList<String> lstProject;
    private DefaultListModel<String> project = new DefaultListModel<String>();
    private JTextArea textAreaEmployee;
    private Connection connection = null;

    /**
     * Main method to launch the application.
     * 
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    EmployeeSearchFrame frame = new EmployeeSearchFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the EmployeeSearchFrame.
     * 
     *  @throws IOException if an I/O error occurs while reading the properties file
     */
    public EmployeeSearchFrame() throws IOException {
        // Code for linking properties file
        FileReader reader = new FileReader("database.props");
        Properties properties = new Properties();
        properties.load(reader);

        // Set up the GUI
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
                if (txtDatabase.getText() != null) {
                    try {
                        // Properties variables
                        String dbdriver = properties.getProperty("db.driver");
                        String dbuser = properties.getProperty("db.user");
                        String dbpassword = properties.getProperty("db.password");
                        String dburl = properties.getProperty("db.url") + txtDatabase.getText() + "?useSSL=false";

                        // Create connection
                        Class.forName(dbdriver);
                        connection = DriverManager.getConnection(dburl, dbuser, dbpassword);

                        // Retrieve project names
                        String projectQuery = "SELECT * FROM PROJECT;";
                        ResultSet result = connection.createStatement().executeQuery(projectQuery);
                        ArrayList<String> projectsList = new ArrayList<>();
                        while (result.next()) {
                            projectsList.add(result.getString("Pname"));
                        }
                        Object[] prj = projectsList.toArray();

                        // Retrieve department names
                        String departmentQuery = "SELECT * FROM DEPARTMENT";
                        result = connection.createStatement().executeQuery(departmentQuery);
                        ArrayList<String> departmentsList = new ArrayList<>();
                        while (result.next()) {
                            departmentsList.add(result.getString("Dname"));
                        }
                        Object[] dept = departmentsList.toArray();

                        // Populate the department and project lists
                        for (int i = 0; i < dept.length; i++) {
                            department.addElement((String) dept[i]);
                        }
                        for (int j = 0; j < prj.length; j++) {
                            project.addElement((String) prj[j]);
                        }

                        connection.close();
                    } catch (Exception ex) {
                        textAreaEmployee.setText("Exception: " + ex.getMessage());
                    }
                } else {
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
            /**
             * Performs a search based on the selected departments and projects.
             *
             * @param e the action event
             */
            public void actionPerformed(ActionEvent e) {
                StringBuilder queryBuilder = new StringBuilder("SELECT * FROM EMPLOYEE WHERE ");

                // Check if "Not" checkbox for Department is selected
                if (chckbxNotDept.isSelected()) {
                    queryBuilder.append("NOT ");
                }

                // Build the IN clause for selected departments
                queryBuilder.append("Dname IN (");
                for (int i = 0; i < lstDepartment.getSelectedValuesList().size(); i++) {
                    queryBuilder.append("'");
                    queryBuilder.append(lstDepartment.getSelectedValuesList().get(i));
                    queryBuilder.append("'");
                    if (i < lstDepartment.getSelectedValuesList().size() - 1) {
                        queryBuilder.append(", ");
                    }
                }
                queryBuilder.append(") ");

                // Check if "Not" checkbox for Project is selected
                if (chckbxNotProject.isSelected()) {
                    queryBuilder.append("AND NOT ");
                } else {
                    queryBuilder.append("AND ");
                }

                // Build the IN clause for selected projects
                queryBuilder.append("Pname IN (");
                for (int i = 0; i < lstProject.getSelectedValuesList().size(); i++) {
                    queryBuilder.append("'");
                    queryBuilder.append(lstProject.getSelectedValuesList().get(i));
                    queryBuilder.append("'");
                    if (i < lstProject.getSelectedValuesList().size() - 1) {
                        queryBuilder.append(", ");
                    }
                }
                queryBuilder.append(")");

                try {
                    // Establish the database connection
                    Class.forName(properties.getProperty("db.driver")).newInstance();
                    connection = DriverManager.getConnection(properties.getProperty("db.url") + txtDatabase.getText() + "?useSSL=false",
                            properties.getProperty("db.user"), properties.getProperty("db.password"));

                    // Execute the query
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(queryBuilder.toString());

                    // Display the results in the text area
                    StringBuilder resultText = new StringBuilder();
                    while (resultSet.next()) {
                        resultText.append(resultSet.getString("FName")).append("\n");
                    }
                    textAreaEmployee.setText(resultText.toString());

                    // Close the resources
                    resultSet.close();
                    statement.close();
                    connection.close();
                } catch (Exception ex) {
                    textAreaEmployee.setText("Exception: " + ex.getMessage());
                }
            }
        });
    btnSearch.setBounds(80, 276, 89, 23);
    contentPane.add(btnSearch);

    // Button to clear the text area
    JButton btnClear = new JButton("Clear");
    btnClear.addActionListener(new ActionListener() {
        /**
         * Clears the text area and resets the department and project lists.
         *
         * @param e the action event
         */
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

