package com.minhchienduong.moviecatalogservice.resources;

import com.minhchienduong.moviecatalogservice.models.CatalogItem;
import com.minhchienduong.moviecatalogservice.models.Movie;
import com.minhchienduong.moviecatalogservice.models.Rating;
import com.minhchienduong.moviecatalogservice.models.UserRating;
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


    @RequestMapping("/{userId}")
    public List<CatalogItem> getcatalog(@PathVariable("userId") String userId) {
        /*return Collections.singletonList(
                new CatalogItem("Transformer", "Test", 4)
        );*/


        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId, UserRating.class);

        return ratings.getUserRating().stream().map(rating -> {
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);

            /**WEB CLIENT BUILDER**/
            /*Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/movie/" + rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();*/
            return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
        })
        .collect(Collectors.toList());


    }


}
