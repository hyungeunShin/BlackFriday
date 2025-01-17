package com.example.catalogservice.feign;

import com.example.catalogservice.dto.ProductTagsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "searchClient", url = "http://search-service:8080")
public interface SearchClient {
    @PostMapping(value = "/search/addTag")
    void addTagCache(@RequestBody ProductTagsDTO dto);

    @PostMapping(value = "/search/removeTag")
    void removeTagCache(@RequestBody ProductTagsDTO dto);
}
