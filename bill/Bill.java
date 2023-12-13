package bill;

import user.User;
import region.Region;

public class Bill {
    private User user;
    private Region region;
    private double unitsConsumed;

    public Bill(User user, Region region, double unitsConsumed) {
        this.user = user;
        this.region = region;
        this.unitsConsumed = unitsConsumed;
    }

    public double calculateBillAmount() {
        return unitsConsumed * region.getUnitRate();
    }
}
