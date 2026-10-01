import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BmiCalculator extends JFrame {

    private JPanel mainPanel;

    private JRadioButton englishRadioButton;
    private JRadioButton metricRadioButton;

    private JTextField weightTextField;
    private JTextField heightTextField;

    private JLabel weightLabel;
    private JLabel heightLabel;

    private JButton calculateButton;

    private JLabel bmiValueLabel;
    private JLabel categoryLabel;

    private BmiCalculatorViewModel viewModel;

    public BmiCalculator() {

        setTitle("BMI Calculator");

        setContentPane(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(600, 700);

        setLocationRelativeTo(null);

        viewModel = new BmiCalculatorViewModel();

        // Default unit
        englishRadioButton.setSelected(true);

        updateCalculationInputs();

        // English radio button
        englishRadioButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                viewModel.setUnit("English");

                updateCalculationInputs();
            }
        });

        // Metric radio button
        metricRadioButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                viewModel.setUnit("Metric");

                updateCalculationInputs();
            }
        });

        // Calculate button
        calculateButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                calculateBMI();
            }
        });
    }

    private void updateCalculationInputs() {

        if (englishRadioButton.isSelected()) {

            weightLabel.setText("Weight (pounds)");
            heightLabel.setText("Height (inches)");

            weightTextField.setToolTipText(
                    "Enter weight in pounds"
            );

            heightTextField.setToolTipText(
                    "Enter height in inches"
            );

        } else {

            weightLabel.setText("Weight (kilograms)");
            heightLabel.setText("Height (meters)");

            weightTextField.setToolTipText(
                    "Enter weight in kilograms"
            );

            heightTextField.setToolTipText(
                    "Enter height in meters"
            );
        }

        weightTextField.setText("");
        heightTextField.setText("");

        bmiValueLabel.setText("--");
        categoryLabel.setText("--");
    }

    private void calculateBMI() {

        String weightText =
                weightTextField.getText().trim();

        String heightText =
                heightTextField.getText().trim();

        // Check empty inputs
        if (weightText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your weight.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );

            weightTextField.requestFocus();

            return;
        }

        if (heightText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your height.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );

            heightTextField.requestFocus();

            return;
        }

        try {

            double weight =
                    Double.parseDouble(weightText);

            double height =
                    Double.parseDouble(heightText);

            // Check positive values
            if (weight <= 0 || height <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Weight and height must be greater than 0.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            // Send values to ViewModel
            viewModel.setWeight(weight);
            viewModel.setHeight(height);

            // Calculate BMI
            double bmi =
                    viewModel.calculateBMIValue();

            // Determine category
            String category =
                    BMICategoryUtility.determineCategory(bmi);

            // Create result model
            BMIResultModel result =
                    new BMIResultModel(bmi, category);

            // Display result
            bmiValueLabel.setText(
                    String.format("%.1f",
                            result.getBmiValue())
            );

            categoryLabel.setText(
                    result.getCategory()
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numbers.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            BmiCalculator calculator =
                    new BmiCalculator();

            calculator.setVisible(true);
        });
    }
}
