package com.wechatorder.sell.repository;

import com.wechatorder.sell.dataobject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository repository;

    @Test
    public void findOneTest() {
        ProductCategory productCategory = repository.findOne(1);
        System.out.println(productCategory);
    }

    @Test
    @Transactional
    public void saveTest() {
        ProductCategory productCategory = new ProductCategory("女生最爱", 3);

        ProductCategory result = repository.save(productCategory);
        //断言
        Assert.assertNotNull(result);
        // Assert.assertNotEquals(null,result);这两个相同
    }
    @Test
    public void findByCategoryTypeIn() {
        List<Integer> list = Arrays.asList(2, 3, 4);
        List<ProductCategory> productCategoryList = repository.findByCategoryTypeIn(list);
        Assert.assertNotEquals(0,productCategoryList);
    }

}