package com.macro.mall.search.service.Impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.util.ObjectBuilder;
import com.macro.mall.search.dao.EsProductDao;
import com.macro.mall.search.domain.EsProduct;
import com.macro.mall.search.domain.EsProductRelatedInfo;
import com.macro.mall.search.repository.EsProductRepository;
import com.macro.mall.search.service.EsProductService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * @Author Caosen
 * @Date 2022/9/18 13:34
 * @Version 1.0
 */

@Service
public class EsProductSerivceImpl implements EsProductService {

    @Autowired
    private EsProductDao esProductDao;

    @Autowired
    private EsProductRepository esProductRepository;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Override
    public int importAll() {
        List<EsProduct> allEsProductList = esProductDao.getAllEsProductList(null);
        Iterable<EsProduct> esProducts = esProductRepository.saveAll(allEsProductList);
        Iterator<EsProduct> iterator = esProducts.iterator();
        int result = 0;
        while (iterator.hasNext()) {
            result++;
            iterator.next();
        }
        return result;
    }

    @Override
    public void addIndex(String name) throws IOException {
        elasticsearchClient.indices().create(b -> b.index(name));

    }

    @Override
    public boolean indexExists(String name) throws IOException {
        return elasticsearchClient.indices().exists(b -> b.index(name)).value();
    }

    @Override
    public void delIndex(String name) throws IOException {
        elasticsearchClient.indices().delete(b -> b.index(name));
    }

    @Override
    public void create(String name, Function<IndexSettings.Builder, ObjectBuilder<IndexSettings>> setting, Function<TypeMapping.Builder, ObjectBuilder<TypeMapping>> mapping) throws IOException {
        elasticsearchClient.indices()
                .create(b -> b
                        .index(name)
                        .settings(setting)
                        .mappings(mapping));
    }

    @Override
    public void delete(Long id) {
        esProductRepository.deleteById(id);

    }

    @Override
    public EsProduct create(Long id) {
        EsProduct result = null;
        List<EsProduct> allEsProductList = esProductDao.getAllEsProductList(id);
        if (allEsProductList.size() > 0){
            EsProduct esProduct = allEsProductList.get(0);
            result = esProductRepository.save(esProduct);
        }
        return result;
    }

    @Override
    public void delete(List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            List<EsProduct> esProductList = new ArrayList<>();
            for (Long id : ids) {
                EsProduct esProduct = new EsProduct();
                esProduct.setId(id);
                esProductList.add(esProduct);
            }
            esProductRepository.deleteAll(esProductList);
        }
    }

    @Override
    public Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return esProductRepository.findByNameOrSubTitleOrKeywords(keyword, keyword, keyword, pageable);
    }

    @Override
    public Page<EsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort) {
        return null;
    }

    @Override
    public Page<EsProduct> recommend(Long id, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public EsProductRelatedInfo searchRelatedInfo(String keyword) {
        return null;
    }
}
