package jjc.springboot1.comparator;

import jjc.springboot1.pojo.Product;

import java.util.Comparator;

public class ProductAllComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {

        return p2.getSaleCount()*p2.getReviewCount() - p2.getSaleCount()*p2.getReviewCount(); //从高到低排序
    }
}
