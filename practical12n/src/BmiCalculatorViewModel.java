public class BmiCalculatorViewModel {

    private double weight;
    private double height;
    private String unit;

    public BmiCalculatorViewModel() {
        unit = "English";
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

    public String getUnit() {
        return unit;
    }

    public double calculateBMIValue() {
        if ("English".equals(unit)) {
            // English: weight in pounds, height in inches
            return (weight * 703.0) / (height * height);
        }

        // Metric: weight in kilograms, height in centimeters
        double heightInMeters = height / 100.0;
        return weight / (heightInMeters * heightInMeters);
    }
}
