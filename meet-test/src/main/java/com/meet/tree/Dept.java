package com.meet.tree;

import lombok.Data;

/**
 * @program: meet-boot
 * @ClassName Dept
 * @description:
 * @author: MT
 * @create: 2022-12-14 08:07
 **/
@Data
public class Dept {

    private Integer id;
    private String name;
    private Integer parentId;
    private Integer sort;
}