public class BMIResultModel {

    private double bmiValue;
    private String category;

    public BMIResultModel(double bmiValue, String category) {
        this.bmiValue = bmiValue;
        this.category = category;
    }

    public double getBmiValue() {
        return bmiValue;
    }

    public String getCategory() {
        return category;
    }
}
