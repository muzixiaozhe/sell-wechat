package com.wechatorder.sell.dataobject.mapper;

import com.wechatorder.sell.dataobject.ProductCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * mybatis测试
 */
public interface ProductategoryMapper {

    @Insert("insert info product_category(category_name,category_type) values (#{categoryName,jdbcType=VARCHAR},#{category_type,jdbcType=INTEGER})")
    int insertByMap(Map<String,Object> map);

    @Insert("insert info product_category(category_name,category_type) values (#{categoryName,jdbcType=VARCHAR},#{category_type,jdbcType=INTEGER})")
    int insertByObject(ProductCategory productCategory);

    @Select("select * from product_category where category_type=#{categoryType}")
    @Results({
            @Result(column = "category_id", property="categoryId"),
            @Result(column = "category_name", property="categoryName"),
            @Result(column = "category_type", property="categoryType")

    })
    ProductCategory findByCategoryType(Integer categoryType);

    @Select("select * from product_category where category_name=#{categoryName}")
    @Results({
            @Result(column = "category_id", property="categoryId"),
            @Result(column = "category_name", property="categoryName"),
            @Result(column = "category_type", property="categoryType")

    })
    List<ProductCategory> findByCategoryName(String categoryName);

    @Update("update product_category set category_name=#{categoryName} where category_type=#{categoryType}")
    int updateByCategoryType(@Param("categoryName") String categoryName,@Param("categoryType") Integer categoryType);


















}
