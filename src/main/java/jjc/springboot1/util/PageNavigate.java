package jjc.springboot1.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页类，对Page类进行了封装，提供必要的分页信息。
 * @param <T>类型参数是要展示的实体类。
 */
public class PageNavigate<T> {
    @JsonIgnore
    private Page<T> pageFromJPA;    //框架自带的分页类
    private int totalPages;     //总的页数
    private int number;     //页码
    private int size;   //每页上的数据条数
    private long totalElements;
    private int numberOfElements;
    private int navigatorPages;     //可供点击的页数
    private boolean first;  //首页标识
    private boolean last;   //最后一页标识
    private int[] navigatorPageNums;    //可供点击的页码
    private List<T> content;     //数据内容
    private boolean isHasNext;  //下一页存在标识
    private boolean isHasPre;   //上一页存在标识
    private boolean isHasContent;   //页面是否有内容

    /**
     * 无参数构造器，空分页，用于Redis 从 json格式转换为 PageNavigate
     *
     */
    public PageNavigate(){
    }

    /**
     * 构造器，对实例字段进行初始化，页码数组通过调用方法进行初始化
     * @param pageFromJPA
     * @param navigatorPages
     */
    public PageNavigate(Page<T> pageFromJPA, int navigatorPages){
        this.pageFromJPA = pageFromJPA;
        this.navigatorPages = navigatorPages;
        totalPages = pageFromJPA.getTotalPages();
        number = pageFromJPA.getNumber();
        size = pageFromJPA.getSize();
        totalElements = pageFromJPA.getTotalElements();
        numberOfElements = pageFromJPA.getNumberOfElements();
        first = pageFromJPA.isFirst();
        last = pageFromJPA.isLast();
        content = pageFromJPA.getContent();
        isHasNext = pageFromJPA.hasNext();
        isHasPre = pageFromJPA.hasPrevious();
        isHasContent = pageFromJPA.hasContent();
        calcNavigatorPageNums();
    }

    /**
     * 计算页码数值，由构造器调用，进行页码数组的初始化
     */
    public void calcNavigatorPageNums(){
        int[] navigatorPageNums;
        int totalPages = getTotalPages();
        int num = getNumber();

        //当总页数小于分页页码数时
        if (totalPages < navigatorPages){
            navigatorPageNums = new int[totalPages];
            for(int i = 0; i < totalPages; i++){
                navigatorPageNums[i] = i+1;
            }
        }else {     //当总页数多余分页页码数时
            navigatorPageNums = new int[navigatorPages];
            int startNum = number - navigatorPages/2;   //起始页码
            int endNum = number + navigatorPages/2;     //结束页码

            //当起始页码数小于1时
            if(startNum < 1){
                startNum = 1;
                for(int i = 0; i < navigatorPages; i++){
                    navigatorPageNums[i] = startNum;
                    startNum++;
                }
            }else if (endNum > totalPages){     //结束页码大于总页数
                endNum = totalPages;
                for (int i = navigatorPages-1; i >= 0; i--){
                    navigatorPageNums[i] = totalPages;
                    totalPages--;
                }
            }else {     //中间页
                for (int i = 0; i < navigatorPages; i++){
                    navigatorPageNums[i] = startNum;
                    startNum++;
                }
            }
        }
        this.navigatorPageNums = navigatorPageNums;
    }

    public Page<T> getPageFromJPA() {
        return pageFromJPA;
    }

    public void setPageFromJPA(Page<T> pageFromJPA) {
        this.pageFromJPA = pageFromJPA;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getNavigatorPages() {
        return navigatorPages;
    }

    public void setNavigatorPages(int navigatorPages) {
        this.navigatorPages = navigatorPages;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int[] getNavigatorPageNums() {
        return navigatorPageNums;
    }

    public void setNavigatorPageNums(int[] navigatorPageNums) {
        this.navigatorPageNums = navigatorPageNums;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public boolean isHasNext() {
        return isHasNext;
    }

    public void setHasNext(boolean hasNext) {
        isHasNext = hasNext;
    }

    public boolean isHasPre() {
        return isHasPre;
    }

    public void setHasPre(boolean hasPre) {
        isHasPre = hasPre;
    }

    public boolean isHasContent() {
        return isHasContent;
    }

    public void setHasContent(boolean hasContent) {
        isHasContent = hasContent;
    }
}
