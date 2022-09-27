package com.macro.mall.search.service;

import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.util.ObjectBuilder;
import com.macro.mall.search.domain.EsProduct;
import com.macro.mall.search.domain.EsProductRelatedInfo;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

/**
 * 商品搜索管理Service
 * Created by macro on 2018/6/19.
 */
public interface EsProductService {


    /**
     * 从数据库中导入所有商品到ES
     */
    int importAll();

    /**
     * 新建指定名称的索引
     * @param name
     * @throws IOException
     */
    void addIndex(String name) throws IOException;

    /**
     * 检查指定名称的索引是否存在
     * @param name
     * @return
     * @throws IOException
     */
    boolean indexExists(String name) throws IOException;

    /**
     * 删除指定索引
     * @param name
     * @throws IOException
     */
    void delIndex(String name) throws IOException;

    /**
     * 创建索引，指定setting和mapping
     * @param name 索引名称
     * @param settingFn 索引参数
     * @param mappingFn 索引结构
     * @throws IOException
     */
    void create(String name,
                Function<IndexSettings.Builder, ObjectBuilder<IndexSettings>> settingFn,
                Function<TypeMapping.Builder, ObjectBuilder<TypeMapping>> mappingFn) throws IOException;





    /**
     * 根据id删除商品
     */
    void delete(Long id);

    /**
     * 根据id创建商品
     */
    EsProduct create(Long id);

    /**
     * 批量删除商品
     */
    void delete(List<Long> ids);

    /**
     * 根据关键字搜索名称或者副标题
     */
    Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 根据关键字搜索名称或者副标题复合查询
     */
    Page<EsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort);

    /**
     * 根据商品id推荐相关商品
     */
    Page<EsProduct> recommend(Long id, Integer pageNum, Integer pageSize);

    /**
     * 获取搜索词相关品牌、分类、属性
     */
    EsProductRelatedInfo searchRelatedInfo(String keyword);
}
