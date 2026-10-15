import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;


public class LoginInterface extends JFrame{
    private static final Color NAVY = new Color(25, 45, 76);
    private static final Color ACCENT= new Color(0, 174, 239);
    private static final Color BACKGROUND = new Color(247, 249, 252);
    private static final Color MUTED = new Color(91, 119, 161);
    private static final Color BORDER = new Color(0, 154, 210);
                                                                    //219, 228, 240 can use



    public LoginInterface() {
        setTitle("Faculty of Technology Management System (FoT-TMS) - Login  ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 580);
        setMinimumSize(new Dimension(1100, 580));
        setResizable(true);

        setLocationRelativeTo(null);// open wenne center
/*
        ImageIcon windowIcon = new ImageIcon(getClass().getResource("C:\\Users\\ASUS\\OneDrive\\Desktop\\FoT_TMS_Login_Interface_Only - Copy\\FotLogo.png"));
        setIconImage(windowIcon.getImage());
*/
        buildInterface();
    }

    private void buildInterface() {
        JPanel container = new JPanel(new GridLayout(1, 2));
        container.setBackground(BACKGROUND);

        // Left side ---------------------------------------

        JPanel leftPanel = new JPanel(new BorderLayout(20, 20));
        leftPanel.setBackground(NAVY);
        leftPanel.setBorder(new EmptyBorder(47, 47, 47, 47));

        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));

        JLabel university = new JLabel("UNIVERSITY OF RUHUNA");
        university.setFont(new Font("Segoe UI", Font.BOLD, 16));
        university.setForeground(ACCENT);


        JLabel faculty = new JLabel("Faculty of Technology");
        faculty.setFont(new Font("Segoe UI", Font.BOLD, 32));
        faculty.setForeground(Color.WHITE);

        JLabel system = new JLabel("Management System (FoT-TMS)");
        system.setFont(new Font("Segoe UI", Font.PLAIN, 23));
        system.setForeground(new Color(203, 213, 225));




 //description
        JLabel description = new JLabel(
                "<html>"
                        + "<br><br>"
                        + "• Role-Based Access Control (Admin, Lecturer, TO, Student)<br>"
                        + "• Theory & Practical Attendance Engine<br>"
                        + "• Medical Record Management &amp; Approval Workflow<br>"
                        + "• Continuous Assessment (CA) &amp; Exam Eligibility Checker<br>"
                        + "• UGC Circular No. 12-2024 Compliant Grading System<br>"
                        + "• SGPA &amp; CGPA Calculation Platform<br>"
                        + "• Batch &amp; Individual Summary Reports Generation</html>");

        description.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        description.setForeground(new Color(226, 232, 240));



        brand.add(university);
        brand.add(Box.createRigidArea(new Dimension(0, 20)));
        brand.add(faculty);
        brand.add(system);
        brand.add(Box.createRigidArea(new Dimension(0, 22)));
        brand.add(description);


        JLabel footer = new JLabel("Faculty of TEchnology University of Ruhuna 2026");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.setForeground(new Color(148, 163, 184));
        leftPanel.add(brand, BorderLayout.CENTER);
        leftPanel.add(footer, BorderLayout.SOUTH);




        // Right side--------------------------
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(BACKGROUND);
        rightPanel.setBorder(new EmptyBorder(47, 47, 47, 47));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(BORDER, 2));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 22, 6, 22);

        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.weightx = 1;
        JLabel heading = new JLabel("Account Login");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 27));
        heading.setForeground(new Color(10, 18, 31));
        card.add(heading, gbc);

        gbc.gridy = 1;
        JLabel prompt = new JLabel("Enter your credentials to access your dashboard.");
        prompt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        prompt.setForeground(MUTED);
        card.add(prompt, gbc);


        gbc.gridy = 2;
        gbc.gridwidth = 2;
        card.add(Box.createRigidArea(new Dimension(1, 40)), gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 3; gbc.gridx = 0;
        JLabel usernameLabel = new JLabel("Registration Number :");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        card.add(usernameLabel, gbc);

        gbc.gridx = 1;
        JTextField username = new JTextField(18);
        styleField(username);
        card.add(username, gbc);

        gbc.gridy = 4; gbc.gridx = 0;
        JLabel passwordLabel = new JLabel("Password :");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        card.add(passwordLabel, gbc);



        gbc.gridx = 1;
        JPasswordField password = new JPasswordField(18);
        styleField(password);
        card.add(password, gbc);



        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttons.setOpaque(false);

        JButton clear = new JButton("Clear");
        clear.setFont(new Font("Segoe UI", Font.BOLD, 15));
        clear.setForeground(new Color(15, 23, 42));
        clear.setBackground(Color.WHITE);
        clear.setBorder(new LineBorder(BORDER, 1));
        clear.setFocusPainted(false);
        clear.setPreferredSize(new Dimension(138, 44));

        JButton signIn = new JButton("Sign In");
        signIn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        signIn.setForeground(Color.WHITE);
        signIn.setBackground(new Color(39, 99, 235));
        signIn.setBorderPainted(false);
        signIn.setFocusPainted(false);
        signIn.setPreferredSize(new Dimension(138, 44));



        buttons.add(clear);
        buttons.add(signIn);
        card.add(buttons, gbc);

        rightPanel.add(card, BorderLayout.CENTER);
        container.add(leftPanel);
        container.add(rightPanel);
        setContentPane(container);
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setPreferredSize(new Dimension(217, 40));
        field.setBorder(new LineBorder(BORDER, 1));
        field.setBackground(Color.WHITE);
    }










    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginInterface().setVisible(true));
    }











}
