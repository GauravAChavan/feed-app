package com.feedapp.service.impl;

import com.feedapp.cache.Cache;
import com.feedapp.exception.InvalidInputException;
import com.feedapp.model.Comment;
import com.feedapp.model.Story;
import com.feedapp.repository.StoryRepository;
import com.feedapp.service.StoryService;
import com.feedapp.utility.Convertor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
public class StoryServiceImpl implements StoryService {

    @Value("${hackernews.api.path.story_id_url}")
    private String storyIdUrl;

    @Value("${hackernews.api.path.item_url}")
    private String itemUrl;

    private static final Logger LOGGER = LoggerFactory.getLogger(StoryServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private Cache cache;


    @Override
    public List getTopTenStories() {
        List<Story> topTenStories = cache.getCache("stories", 15, () -> fetchTopStoriesFromHackerNewsAndFiler());
        return topTenStories;
    }

    private List<Story> fetchTopStoriesFromHackerNewsAndFiler() {
        LOGGER.info("Going to fetch all story");
        ResponseEntity<List> result = restTemplate.getForEntity(storyIdUrl, List.class, new HashMap<>());
        List<Story> stories = new ArrayList<>();
        if (result != null && result.getBody() != null) {
            result.getBody().forEach(s -> {
                ResponseEntity<Map> storyMap = restTemplate.getForEntity(String.format(itemUrl, s), Map.class, new HashMap<>());
                stories.add(Convertor.convertMapStoryToStoryObject(storyMap.getBody()));
            });
            if (stories != null) {
                List<Story> topTenStoresRankedByScoreInLastFifteenMin = stories.stream()
                        .filter(story -> story.getSubmissionTime().isAfter(LocalDateTime.now().minusMinutes(15)))
                        .sorted(comparing(Story::getScore).reversed()).limit(10).collect(Collectors.toList());

                //save stories in DB
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(() -> saveStories(topTenStoresRankedByScoreInLastFifteenMin));

                LOGGER.info("After executor");
                return topTenStoresRankedByScoreInLastFifteenMin;
            }
        }
        return new ArrayList<>();
    }

    public List<Story> saveStories(List<Story> stories) {
        LOGGER.info("Call to save stories");
        if (stories != null && !stories.isEmpty()) {
            return storyRepository.saveAll(stories);
        } else {
            throw new InvalidInputException("No data available to save");
        }
    }

    @Override
    public Page<Story> getPastStories(Pageable pageable) {
        return storyRepository.findAll(pageable);
    }

    @Override
    public List getComments(String storyId) {
        if (StringUtils.isEmpty(storyId)) {
            ResponseEntity<Map> storyMap = restTemplate.getForEntity(String.format(itemUrl, storyId), Map.class, new HashMap<>());
            List<Integer> commentIdList = (List<Integer>) storyMap.getBody().get("kids");
            List<Comment> comments = new ArrayList<>();
            //fetch comment one by one
            commentIdList.forEach(commentId -> {
                ResponseEntity<Map> commentMap = restTemplate.getForEntity(String.format(itemUrl, commentId), Map.class, new HashMap<>());
                comments.add(Convertor.convertMapCommentToCommentObject(commentMap.getBody()));

            });
            // return max 10 comments sorted by child comments
            return comments.stream().sorted(comparing(Comment::getKids).reversed()).limit(10).collect(Collectors.toList());
        } else {
            throw new InvalidInputException("Invalid storyId.");
        }

    }
}
