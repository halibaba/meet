package com.meet.tree;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: meet-boot
 * @ClassName TreeTest
 * @description:
 * @author: MT
 * @create: 2022-12-14 08:07
 **/
@RestController
@RequestMapping
public class TreeTest {

    @GetMapping("tree")
    public List<Tree<Integer>> getTree(){
        Dept dept = new Dept();
        dept.setId(1);
        dept.setSort(0);
        dept.setName("P1");
        dept.setParentId(0);
        Dept dept2 = new Dept();
        dept2.setId(2);
        dept2.setSort(2);
        dept2.setName("P2");
        dept2.setParentId(1);
        Dept dept3 = new Dept();
        dept3.setId(3);
        dept3.setSort(2);
        dept3.setName("P3");
        dept3.setParentId(1);
        Dept dept4 = new Dept();
        dept4.setId(4);
        dept4.setSort(1);
        dept4.setName("P4");
        dept4.setParentId(3);
        List<Dept> depts = new ArrayList<>();
        depts.add(dept);
        depts.add(dept2);
        depts.add(dept3);
        depts.add(dept4);
        List<TreeNode<Integer>> collect = depts.stream()
                .sorted(Comparator.comparingInt(Dept::getSort)).map(e -> {
                    TreeNode<Integer> treeNode = new TreeNode();
                    treeNode.setId(e.getId());
                    treeNode.setParentId(e.getParentId());
                    treeNode.setName(e.getName());
                    treeNode.setWeight(e.getSort());
                    return treeNode;
                }).collect(Collectors.toList());
        List<Tree<Integer>> build = TreeUtil.build(collect, 0);
        return build;
    }


}