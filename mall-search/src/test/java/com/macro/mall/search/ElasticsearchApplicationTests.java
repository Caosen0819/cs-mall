package com.macro.mall.search;

import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.transport.endpoints.BooleanResponse;
//import com.macro.mall.search.config.EsUtilConfigClint;
import co.elastic.clients.util.ObjectBuilder;
import com.macro.mall.search.config.EsUtilConfigClint2;
import com.macro.mall.search.domain.Product;
import com.macro.mall.search.service.EsProductService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * @Author Caosen
 * @Date 2022/9/18 15:04
 * @Version 1.0
 */



@SpringBootTest
class ElasticsearchApplicationTests {
    @Autowired
    private EsUtilConfigClint2 client;

    @Autowired
    private EsProductService esProductService;


    @Test
    void addIndexSimple() throws IOException {
        String s = "simple";

        esProductService.addIndex(s);
        System.out.println("创建success");
    }
    @Test
    void addIndexComplicated() throws IOException {

        String s = "complicated";
        Function<IndexSettings.Builder, ObjectBuilder<IndexSettings>> setting = builder -> builder
                .index(i -> i.numberOfShards("3").numberOfReplicas("1"));
        Property keywordproperty = Property.of(p -> p.keyword(k -> k.ignoreAbove(256)));
        Property testproperty = Property.of(p -> p.text(builder -> builder));
        Property integerproperty = Property.of(builder -> builder.integer(i -> i));

        Function<TypeMapping.Builder, ObjectBuilder<TypeMapping>> mapping = builder -> builder
                .properties("name", keywordproperty)
                .properties("description", testproperty)
                .properties("price", integerproperty);
        esProductService.create(s, setting, mapping);

    }





    /**
     * 判断索引是否存在
     * @throws IOException
     */
    @Test
    void existsIndex() throws IOException {
        ExistsRequest existsRequest = new ExistsRequest.Builder().index("ouldindex").build();
        BooleanResponse existsResponse = client.configClint().indices().exists(existsRequest);
        System.out.println("是否存在："+existsResponse.value());
    }
    /**
     * 创建索引
     * 创建索引时，必须是小写，否则创建报错
     * @throws IOException
     */
    @Test
    void createIndex() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder().index("ouldindex").build();
        CreateIndexResponse createIndexResponse = client.configClint().indices().create(createIndexRequest);
        System.out.println("是否成功："+createIndexResponse.acknowledged());
    }

    /**
     * 删除索引
     * @throws IOException
     */
    @Test
    void deleteIndex() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest.Builder().index("ouldindex").build();
        DeleteIndexResponse deleteIndexResponse = client.configClint().indices().delete(deleteIndexRequest);
        System.out.println("是否成功："+deleteIndexResponse.acknowledged());
    }

    /**
     * 同步方式
     * 向索引中添加信息,此操作不存在索引时会直接创建索引，使用时需要各种校验使逻辑更严谨
     * @throws IOException
     */
    @Test
    void setIndex() throws IOException {
        Product product = new Product("帽子",44.5,9);
        IndexRequest<Product> indexRequest = new IndexRequest.Builder<Product>().index("newindex")
                .id(String.valueOf(product.getNumber()))
                .document(product)
                .build();
        IndexResponse indexResponse = client.configClint().index(indexRequest);
        System.out.println(indexResponse);
    }

    /**
     * 批量写入数据
     * @throws IOException
     */
    @Test
    void bulkIndex() throws IOException{
        List<Product> products = new ArrayList<Product>();
        products.add(new Product("香烟",135,1));
        products.add(new Product("瓜子",154,2));
        products.add(new Product("矿泉水",613,3));
        products.add(new Product("酱油",72,4));
        products.add(new Product("大米",771,5));
        BulkRequest.Builder bk = new BulkRequest.Builder();
        int indexId = 4;
        for (Product product:products) {
            bk.operations(op->op.index(i->i.index("newindex")
                    .id(UUID.randomUUID().toString())
                    .document(product)));
        }
        BulkResponse response = client.configClint().bulk(bk.build());
        if (response.errors()) {
            System.out.println("Bulk had errors");
            for (BulkResponseItem item: response.items()) {
                if (item.error() != null) {
                    System.out.println(item.error().reason());
                }
            }
        }
    }

    /**
     * 根据索引文档id获取文档信息
     * @throws IOException
     */
    @Test
    void getIndexById() throws IOException {
        GetRequest getRequest = new GetRequest.Builder().index("newindex")
                .id("9")
                .build();
        GetResponse<Product> response = client.configClint().get(getRequest, Product.class);
        if (response.found()) {
            Product product = response.source();
            System.out.println("Product name " + product.getNumber());
            System.out.println("Product price " + product.getPrice());
        } else {
            System.out.println("Product not found");
        }
    }

    /**
     * 简单查询文档信息
     * @throws IOException
     */
    @Test
    void getSearch() throws IOException{

        /*此处 .from(1).size(2) 表示分页查询，从第一页开始查询，大小为两条*/
        SearchRequest searchRequest = new SearchRequest.Builder().index("newindex")
                .query(q -> q.match(m -> m.field("productName").query("烟"))).from(1).size(2).build();

        SearchResponse<Product> response = client.configClint().search(searchRequest,Product.class);

        TotalHits total = response.hits().total();
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

        if (isExactResult) {
            System.out.println("There are " + total.value() + " results");
        } else {
            System.out.println("There are more than " + total.value() + " results");
        }

        List<Hit<Product>> hits = response.hits().hits();
        for (Hit<Product> hit: hits) {
            Product product = hit.source();
            System.out.println("Found product " + product.getProductName() + ", score " + hit.score());
        }
    }

    /**
     * 多条件嵌套查询文档信息
     * @throws IOException
     */
    @Test
    void getSearchs() throws IOException{
        String productName = "衣服";
        double price = 115;

        //按照产品名称搜索
        Query byname = MatchQuery.of(m -> m.field("productName")
                .query(productName))._toQuery();

        //按照产品价格搜索
        Query byprice = RangeQuery.of(r -> r
                .field("price")
                .gte(JsonData.of(price))
        )._toQuery();

        //结合名称和价格查询
        SearchResponse<Product> response = client.configClint().search(s -> s
                        .index("newindex")
                        .query(q -> q
                                .bool(b -> b
                                        .must(byname)
                                        .must(byprice)
                                )
                        )
                        .from(1)
                        .size(2),
                Product.class
        );

        List<Hit<Product>> hits = response.hits().hits();
        for (Hit<Product> hit : hits){
            Product product = hit.source();
            System.out.println(product.getProductName()+" "+product.getPrice());
        }
    }

}