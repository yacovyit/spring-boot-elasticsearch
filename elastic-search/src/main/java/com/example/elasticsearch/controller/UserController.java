package com.example.elasticsearch.controller;

import com.example.elasticsearch.datamodel.User;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.management.monitor.StringMonitor;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@RestController
@RequestMapping(value = "user")
@EnableAutoConfiguration
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private Client client;

    @PostMapping("/create")
    public String create(@RequestBody User user) throws IOException {
        IndexResponse indexResponse = client.prepareIndex("users", "employee", user.getUserId())
                .setSource(jsonBuilder()
                        .startObject()
                        .field("name", user.getName())
                        .field("userSettings", user.getUserSettings())
                        .endObject()


                ).get();
        logger.info("Response Id {}", indexResponse.getId());
        return indexResponse.getResult().toString();

    }
    @GetMapping("/view/{id}")
    public Map<String, Object> view(@PathVariable final String id){
        GetResponse getResponse = client.prepareGet("users", "employee", id).get();
        logger.info("Request View {}", id);
        return getResponse.getSource();
    }
    @GetMapping("view/name/{field}")
    public Map<String, Object> searchByName(@PathVariable final String field){
        Map<String, Object> map = null;
        SearchResponse searchResponse = client.prepareSearch("users")
                .setTypes("employee")
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchQuery("name", field))
                .get();
        List<SearchHit> searchHits = Arrays.asList(searchResponse.getHits().getHits());
        map = searchHits.get(0).getSourceAsMap();
        return map;
    }
    @PutMapping("/update/{id}/{name}")
    public String update(@PathVariable final String id, @PathVariable final String name) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("users")
                .type("employee")
                .id(id)
                .doc(jsonBuilder()
                        .startObject()
                        .field("name", name)
                        .endObject());
        try {
            UpdateResponse updateResponse = client.update(updateRequest).get();
            logger.info("{}",updateResponse.status());
            return updateResponse.status().toString();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e);
        }
        return "Exception";
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable final String id){
        DeleteResponse deleteResponse = client.prepareDelete("users","employee", id).get();
        return deleteResponse.getResult().toString();
    }
}
