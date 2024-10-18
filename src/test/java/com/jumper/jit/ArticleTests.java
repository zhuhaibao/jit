package com.jumper.jit;

import com.jumper.jit.dto.ArticleDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * 测试层次化输出
 */
public class ArticleTests {
    static List<ArticleDTO> list;
    @BeforeAll
    static void beforeAll() {
        list = new ArrayList<>();
        list.add(ArticleDTO.builder().id(1).title("1").orderNum(1).build());
        list.add(ArticleDTO.builder().id(2).title("3_1").pid(7).orderNum(1).build());
        list.add(ArticleDTO.builder().id(3).title("2_1").pid(5).orderNum(1).build());
        list.add(ArticleDTO.builder().id(4).title("3_2").pid(7).orderNum(2).build());
        list.add(ArticleDTO.builder().id(5).title("2").orderNum(2).build());
        list.add(ArticleDTO.builder().id(6).title("2_1_1").pid(3).orderNum(1).build());
        list.add(ArticleDTO.builder().id(7).title("3").orderNum(3).build());
        list.add(ArticleDTO.builder().id(8).title("4").orderNum(4).build());
        list.add(ArticleDTO.builder().id(9).title("1_1").pid(1).orderNum(1).build());
        list.add(ArticleDTO.builder().id(10).title("1_2").pid(1).orderNum(2).build());
    }

    @Test
    void test(){
        List<ArticleDTO> sorted = new ArrayList<>();
        Map<Integer,ArticleDTO> index = new HashMap<>();
        list.forEach(a->index.put(a.getId(),a));
        list.forEach(a->{
            if(a.getPid()==null){
                sorted.add(a);//顶级
            }else{//非顶级
                List<ArticleDTO> children = index.get(a.getPid()).getChildren();
                if(children!=null) children.add(a);
                else {
                    children = new ArrayList<>();
                    children.add(a);
                    index.get(a.getPid()).setChildren(children);
                }
            }
        });
        sortAll(sorted);

        printSorted(sorted);
    }
    private static void sortAll(List<ArticleDTO> l){
        Collections.sort(l);
        l.forEach(e->{
            if(e.getChildren()!=null){
                sortAll(e.getChildren());
            }
        });
    }
    private static void printSorted(List<ArticleDTO> sorted){
        sorted.forEach(a-> {
            System.out.println(a.getTitle());
            if(a.getChildren()!=null) printSorted(a.getChildren());
        });

    }
}
