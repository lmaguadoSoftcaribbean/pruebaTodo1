package com.lmaguado.hulkStore.models.implement.httpRequest;

public class ProductModel {
    private ProductModel() {
        throw new IllegalStateException("ProductModel - Utility class");
    }

    public static class ProductFilter {

    }

    public static class ProductFilterBuy {
        private Integer units;

        public Integer getUnits() {
            return units;
        }

        public void setUnits(Integer units) {
            this.units = units;
        }
    }

    public static class ProductFilterReplenish {
        private Integer units;

        public Integer getUnits() {
            return units;
        }

        public void setUnits(Integer units) {
            this.units = units;
        }
    }
}
