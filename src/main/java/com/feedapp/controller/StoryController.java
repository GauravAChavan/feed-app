package com.feedapp.controller;

import com.feedapp.model.Story;
import com.feedapp.service.StoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("")
public class StoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoryController.class);

    @Autowired
    private StoryService storyService;

    /**
     * This method will return top ten stories in last 15 min
     *
     * @return List of story
     */

    @GetMapping("/top-stories")
    public ResponseEntity getTopStories() {
        LOGGER.info("Rest rest to get top ten stories");
        List<Story> topTenStories = storyService.getTopTenStories();
        return new ResponseEntity(topTenStories, HttpStatus.OK);
    }

    /**
     * This method will return past stories
     *
     * @param pageable
     * @return stories
     */

    @GetMapping("/past-stories")
    public ResponseEntity getPastStories(Pageable pageable) {
        LOGGER.info("Rest rest to get past stories");
        Page<Story> pastStories = storyService.getPastStories(pageable);
        return new ResponseEntity(pastStories, HttpStatus.OK);
    }

    /**
     * This method will return max 10 comments for a given story
     *
     * @param storyId
     * @return comments
     */

    @GetMapping("/comments/{storyId}")
    public ResponseEntity getComments(@PathVariable String storyId) {
        LOGGER.info("Rest rest to get comments");
        List comments = storyService.getComments(storyId);
        return new ResponseEntity(comments, HttpStatus.OK);
    }
}
