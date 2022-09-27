package com.macro.mall.search.controller;

import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
//import com.macro.mall.search.config.EsUtilConfigClint;
import com.macro.mall.search.config.EsUtilConfigClint2;
import com.macro.mall.search.domain.EsProduct;
import com.macro.mall.search.domain.EsProductRelatedInfo;
import com.macro.mall.search.service.EsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 搜索商品管理Controller
 * Created by macro on 2018/6/19.
 */
@Controller
@Api(tags = "EsProductController", description = "搜索商品管理")
@RequestMapping("/esProduct")
public class EsProductController {







    @Autowired
    private EsUtilConfigClint2 client;

    @Autowired
    private EsProductService esProductService;

    @ApiOperation(value = "导入所有数据库中商品到ES")
    @PostMapping(value = "/importAll")
    @ResponseBody
    public CommonResult<Integer> importAllList() {
        int count = esProductService.importAll();
        return CommonResult.success(count);
    }
    @ApiOperation(value = "测试1")
    @PostMapping(value = "/test1")
    public void test1() throws IOException {
        CreateIndexRequest test1 = new CreateIndexRequest.Builder().index("test1").build();
        CreateIndexResponse createIndexResponse = client.configClint().indices().create(test1);
        System.out.println("创建成功");
    }


//    @ApiOperation(value = "根据id删除商品")
//    @GetMapping(value = "/delete/{id}")
//    @ResponseBody
//    public CommonResult<Object> delete(@PathVariable Long id) {
//        esProductService.delete(id);
//        return CommonResult.success(null);
//    }
//
//    @ApiOperation(value = "根据id批量删除商品")
//    @PostMapping(value = "/delete/batch")
//    @ResponseBody
//    public CommonResult<Object> delete(@RequestParam("ids") List<Long> ids) {
//        esProductService.delete(ids);
//        return CommonResult.success(null);
//    }
//
//    @ApiOperation(value = "根据id创建商品")
//    @PostMapping(value = "/create/{id}")
//    public CommonResult<EsProduct> create(@PathVariable Long id) {
//        EsProduct esProduct = esProductService.create(id);
//        if (esProduct != null) {
//            return CommonResult.success(esProduct);
//        } else {
//            return CommonResult.failed();
//        }
//    }
//
//    @ApiOperation(value = "简单搜索")
//    @GetMapping(value = "/search/simple")
//    public CommonResult<CommonPage<EsProduct>> search(@RequestParam(required = false) String keyword,
//                                                      @RequestParam(required = false, defaultValue = "0") Integer pageNum,
//                                                      @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
//        Page<EsProduct> esProductPage = esProductService.search(keyword, pageNum, pageSize);
//        return CommonResult.success(CommonPage.restPage(esProductPage));
//    }
//
//    @ApiOperation(value = "综合搜索、筛选、排序")
//    @ApiImplicitParam(name = "sort", value = "排序字段:0->按相关度；1->按新品；2->按销量；3->价格从低到高；4->价格从高到低",
//            defaultValue = "0", allowableValues = "0,1,2,3,4", paramType = "query", dataType = "integer")
//    @GetMapping(value = "/search")
//    @ResponseBody
//    public CommonResult<CommonPage<EsProduct>> search(@RequestParam(required = false) String keyword,
//                                                      @RequestParam(required = false) Long brandId,
//                                                      @RequestParam(required = false) Long productCategoryId,
//                                                      @RequestParam(required = false, defaultValue = "0") Integer pageNum,
//                                                      @RequestParam(required = false, defaultValue = "5") Integer pageSize,
//                                                      @RequestParam(required = false, defaultValue = "0") Integer sort) {
//        Page<EsProduct> esProductPage = esProductService.search(keyword, brandId, productCategoryId, pageNum, pageSize, sort);
//        return CommonResult.success(CommonPage.restPage(esProductPage));
//    }
//
//    @ApiOperation(value = "根据商品id推荐商品")
//    @GetMapping(value = "/recommend/{id}")
//    @ResponseBody
//    public CommonResult<CommonPage<EsProduct>> recommend(@PathVariable Long id,
//                                                         @RequestParam(required = false, defaultValue = "0") Integer pageNum,
//                                                         @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
//        Page<EsProduct> esProductPage = esProductService.recommend(id, pageNum, pageSize);
//        return CommonResult.success(CommonPage.restPage(esProductPage));
//    }
//
//    @ApiOperation(value = "获取搜索的相关品牌、分类及筛选属性")
//    @GetMapping(value = "/search/relate")
//    public CommonResult<EsProductRelatedInfo> searchRelatedInfo(@RequestParam(required = false) String keyword) {
//        EsProductRelatedInfo productRelatedInfo = esProductService.searchRelatedInfo(keyword);
//        return CommonResult.success(productRelatedInfo);
//    }
}
