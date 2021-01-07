package jjc.springboot1.comparator;

import jjc.springboot1.pojo.Product;

import java.util.Comparator;

public class ProductReviewComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return p2.getReviewCount()-p1.getReviewCount();
    } //根据评价数量从高到低排序
}
