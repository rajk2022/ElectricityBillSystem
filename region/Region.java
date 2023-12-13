package region;

public class Region {
    private String name;
    private double unitRate;

    public Region(String name, double unitRate) {
        this.name = name;
        this.unitRate = unitRate;
    }

    public double getUnitRate() {
        return unitRate;
    }

    public String getName() {
        return name;
    }
}
