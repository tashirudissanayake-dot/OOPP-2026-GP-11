public class BmiCalculatorViewModel {

    private double weight;
    private double height;
    private String unit;

    public BmiCalculatorViewModel() {
        this.unit = "English";
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double calculateBMIValue() {

        if (unit.equals("English")) {

            // BMI = (weight in pounds × 703) / height² in inches
            return (weight * 703) / (height * height);

        } else {

            // BMI = weight in kilograms / height² in meters
            return weight / (height * height);
        }
    }
}
