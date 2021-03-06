package jjc.springboot1.comparator;

import jjc.springboot1.pojo.Product;

import java.util.Comparator;

public class ProductPriceComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return (int) (p1.getPromotePrice()-p2.getPromotePrice());   //价格从低到高排序
    }
}
