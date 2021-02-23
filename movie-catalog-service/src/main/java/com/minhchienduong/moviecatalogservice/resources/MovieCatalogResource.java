package com.minhchienduong.moviecatalogservice.resources;

import com.minhchienduong.moviecatalogservice.models.CatalogItem;
import com.minhchienduong.moviecatalogservice.models.Movie;
import com.minhchienduong.moviecatalogservice.models.Rating;
import com.minhchienduong.moviecatalogservice.models.UserRating;
import com.minhchienduong.moviecatalogservice.services.MovieInfo;
import com.minhchienduong.moviecatalogservice.services.UserRatingInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    MovieInfo movieInfo;

    @Autowired
    UserRatingInfo userRatingInfo;


    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
        /*return Collections.singletonList(
                new CatalogItem("Transformer", "Test", 4)
        );*/

        UserRating ratings = userRatingInfo.getUserRating(userId);

        return ratings.getRatings().stream().map(rating -> {
            /**WEB CLIENT BUILDER**/
            /*Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/movie/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();*/

            return movieInfo.getCatalogItem(rating);
        })
        .collect(Collectors.toList());


    }

   /* public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId) {
        return Arrays.asList(new CatalogItem("No movie", "", 0));
    }*/


}
