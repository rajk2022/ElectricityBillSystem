package user;

import region.Region;

public class User {
    private String username;
    private String password;
    public int id;
    private double[][] monthlyUnitConsumption;

    public User(String username, String password, int numMonths, int numRegions,int i) {
        this.username = username;
        this.password = password;
        this.id = i;
        this.monthlyUnitConsumption = new double[numMonths][numRegions];
    }

    public void storeData(String month, double unitsConsumed, Region region) {
        int monthIndex = getMonthIndex(month);

        if (monthIndex != -1) {
            int regionIndex = getRegionIndex(region);

            if (regionIndex != -1) {
                monthlyUnitConsumption[monthIndex][regionIndex] = unitsConsumed;
                System.out.println("Data stored for " + month + " in region " + region.getName() +
                        " with units consumed: " + unitsConsumed);
            } else {
                System.out.println("Invalid region. Data not stored.");
            }
        } else {
            System.out.println("Invalid month. Data not stored.");
        }
    }

    public void showStoredData() {
        System.out.println("Stored Data for User: " + username);
        for (int i = 0; i < monthlyUnitConsumption.length; i++) {
            System.out.println("Month " + (i + 1) + ":");
            for (int j = 0; j < monthlyUnitConsumption[i].length; j++) {
                System.out.println("  Region " + (j + 1) + ": " + monthlyUnitConsumption[i][j]);
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    private int getMonthIndex(String month) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (int i = 0; i < months.length; i++) {
            if (months[i].equalsIgnoreCase(month)) {
                return i;
            }
        }
        return -1;
    }

    private int getRegionIndex(Region region) {
        String[] regionNames = {"Haryana", "Uttar Pradesh", "Delhi", "Rajasthan"};
        for (int i = 0; i < regionNames.length; i++) {
            if (regionNames[i].equalsIgnoreCase(region.getName())) {
                return i;
            }
        }
        return -1;
    }
}
