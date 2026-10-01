import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BmiCalculator extends JFrame {

    private final JRadioButton englishRadioButton = new JRadioButton("English");
    private final JRadioButton metricRadioButton = new JRadioButton("Metric");
    private final JTextField weightTextField = new JTextField();
    private final JTextField heightTextField = new JTextField();
    private final JLabel weightLabel = new JLabel("Weight (lb)");
    private final JLabel heightLabel = new JLabel("Height (in)");
    private final JButton calculateButton = new JButton("Calculate BMI");
    private final JLabel bmiValueLabel = new JLabel("--", SwingConstants.CENTER);
    private final JLabel categoryLabel = new JLabel("--", SwingConstants.CENTER);

    private final BmiCalculatorViewModel viewModel = new BmiCalculatorViewModel();

    public BmiCalculator() {
        setTitle("BMI Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(430, 650);
        setMinimumSize(new Dimension(400, 600));
        setLocationRelativeTo(null);

        buildUI();
        updateCalculationInputs();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(18, 22, 22, 22));
        setContentPane(root);

        JLabel title = new JLabel("BMI Calculator", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setBorder(new EmptyBorder(5, 0, 20, 0));
        root.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setBackground(Color.WHITE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel unitTitle = new JLabel("Unit");
        unitTitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        form.add(unitTitle);
        form.add(Box.createVerticalStrut(7));

        JPanel unitPanel = new JPanel(new GridLayout(1, 2));
        unitPanel.setBackground(Color.WHITE);
        unitPanel.setBorder(new LineBorder(Color.GRAY, 1, true));
        englishRadioButton.setBackground(Color.WHITE);
        metricRadioButton.setBackground(Color.WHITE);
        englishRadioButton.setFocusPainted(false);
        metricRadioButton.setFocusPainted(false);
        ButtonGroup unitGroup = new ButtonGroup();
        unitGroup.add(englishRadioButton);
        unitGroup.add(metricRadioButton);
        englishRadioButton.setSelected(true);
        unitPanel.add(englishRadioButton);
        unitPanel.add(metricRadioButton);
        form.add(unitPanel);
        form.add(Box.createVerticalStrut(18));

        form.add(weightLabel);
        weightLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        form.add(Box.createVerticalStrut(6));
        configureTextField(weightTextField);
        form.add(weightTextField);
        form.add(Box.createVerticalStrut(16));

        heightLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        form.add(heightLabel);
        form.add(Box.createVerticalStrut(6));
        configureTextField(heightTextField);
        form.add(heightTextField);
        form.add(Box.createVerticalStrut(18));

        calculateButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        calculateButton.setPreferredSize(new Dimension(100, 45));
        calculateButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        calculateButton.setFocusPainted(false);
        form.add(calculateButton);
        form.add(Box.createVerticalStrut(22));

        JPanel resultPanel = createResultPanel();
        form.add(resultPanel);

        root.add(form, BorderLayout.CENTER);

        englishRadioButton.addActionListener(this::unitChanged);
        metricRadioButton.addActionListener(this::unitChanged);
        calculateButton.addActionListener(this::calculateBMI);
        weightTextField.addActionListener(this::calculateBMI);
        heightTextField.addActionListener(this::calculateBMI);
    }

    private void configureTextField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 15));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setPreferredSize(new Dimension(300, 42));
    }

    private JPanel createResultPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.GRAY, 1, true),
                new EmptyBorder(16, 12, 16, 12)
        ));

        JLabel bmiTitle = new JLabel("Your BMI:", SwingConstants.CENTER);
        bmiTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        bmiTitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(bmiTitle);

        bmiValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bmiValueLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        panel.add(bmiValueLabel);

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(Box.createVerticalStrut(8));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(8));

        JLabel categoryTitle = new JLabel("Category:", SwingConstants.CENTER);
        categoryTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        categoryTitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        panel.add(categoryTitle);

        categoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        categoryLabel.setFont(new Font("SansSerif", Font.BOLD, 21));
        panel.add(categoryLabel);

        return panel;
    }

    private void unitChanged(ActionEvent event) {
        viewModel.setUnit(englishRadioButton.isSelected() ? "English" : "Metric");
        updateCalculationInputs();
    }

    private void updateCalculationInputs() {
        if (englishRadioButton.isSelected()) {
            weightLabel.setText("Weight (lb)");
            heightLabel.setText("Height (in)");
            weightTextField.setToolTipText("Enter weight in pounds");
            heightTextField.setToolTipText("Enter height in inches");
        } else {
            weightLabel.setText("Weight (kg)");
            heightLabel.setText("Height (cm)");
            weightTextField.setToolTipText("Enter weight in kilograms");
            heightTextField.setToolTipText("Enter height in centimeters");
        }

        weightTextField.setText("");
        heightTextField.setText("");
        bmiValueLabel.setText("--");
        categoryLabel.setText("--");
    }

    private void calculateBMI(ActionEvent event) {
        String weightText = weightTextField.getText().trim();
        String heightText = heightTextField.getText().trim();

        if (weightText.isEmpty()) {
            showError("Please enter your weight.", weightTextField);
            return;
        }
        if (heightText.isEmpty()) {
            showError("Please enter your height.", heightTextField);
            return;
        }

        try {
            double weight = Double.parseDouble(weightText);
            double height = Double.parseDouble(heightText);

            if (!Double.isFinite(weight) || !Double.isFinite(height)) {
                showError("Please enter valid numbers.", weightTextField);
                return;
            }

            if (weight <= 0 || height <= 0) {
                showError("Weight and height must be greater than 0.", weightTextField);
                return;
            }

            viewModel.setWeight(weight);
            viewModel.setHeight(height);

            double bmi = viewModel.calculateBMIValue();
            String category = BMICategoryUtility.determineCategory(bmi);
            BMIResultModel result = new BMIResultModel(bmi, category);

            bmiValueLabel.setText(String.format("%.1f", result.getBmiValue()));
            categoryLabel.setText(result.getCategory());
        } catch (NumberFormatException ex) {
            showError("Please enter valid numbers (for example: 65 or 65.5).", weightTextField);
        }
    }

    private void showError(String message, JComponent focusComponent) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
        focusComponent.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Use the default Swing look and feel if the system L&F is unavailable.
            }
            new BmiCalculator().setVisible(true);
        });
    }
}
