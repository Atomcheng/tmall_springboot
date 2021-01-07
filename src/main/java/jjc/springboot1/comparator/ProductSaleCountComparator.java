package jjc.springboot1.comparator;

import jjc.springboot1.pojo.Product;

import java.util.Comparator;

public class ProductSaleCountComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return p2.getSaleCount()-p1.getSaleCount(); //根据销售数量从高到低排序。
    }
}
