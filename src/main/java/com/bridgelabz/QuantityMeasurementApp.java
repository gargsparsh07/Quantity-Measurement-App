package com.bridgelabz;

public class QuantityMeasurementApp {

    // Inner class for Feet
    public static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {

            // Same reference
            if (this == obj)
                return true;

            // Null or different type
            if (obj == null || getClass() != obj.getClass())
                return false;

            Feet feet = (Feet) obj;

            // Compare using Double.compare
            return Double.compare(this.value, feet.value) == 0;
        }
    }
    public static class Inches {
        private final double value;

        public Inches(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) return true;

            if (obj == null || getClass() != obj.getClass())
                return false;

            Inches inches = (Inches) obj;

            return Double.compare(this.value, inches.value) == 0;
        }
    }

    // Main method for manual run
    public static void main(String[] args) {

        Feet value1 = new Feet(1.0);
        Feet value2 = new Feet(1.0);

        boolean result = value1.equals(value2);

        System.out.println("Are equal? " + result);
    }
}