package com.wechatorder.sell.controller;

import com.wechatorder.sell.VO.ProductInfoVO;
import com.wechatorder.sell.VO.ProductVO;
import com.wechatorder.sell.VO.ResultVO;
import com.wechatorder.sell.dataobject.ProductCategory;
import com.wechatorder.sell.dataobject.ProductInfo;
import com.wechatorder.sell.service.CategoryService;
import com.wechatorder.sell.service.ProductService;
import com.wechatorder.sell.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 买家商品
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/list")
    @Cacheable(cacheNames = "product",key = "123")
    public ResultVO list(){
        //查询所有的上架商品
        List<ProductInfo> productInfoList = productService.findUpAll();

        //查询类目(一次性查询)
        /*for (ProductInfo productInfo : productInfoList) {
            categoryTypeList.add(productInfo.getCategoryType());
        }*/
        List<Integer> categoryTypeList = productInfoList.stream().map(e->e.getCategoryType()).collect(Collectors.toList());
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);
        //数据拼装
        List<ProductVO> productVOList=new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            ProductVO productVO=new ProductVO();
            productVO.setCategoryName(productCategory.getCategoryName());
            productVO.setCategoryType(productCategory.getCategoryType());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                if(productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO=new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVoList(productInfoVOList);
            productVOList.add(productVO);
        }


        return ResultVOUtil.success(productVOList);
    }
}
