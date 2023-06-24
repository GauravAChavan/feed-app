package com.feedapp.service;

import com.feedapp.model.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoryService {

    List<Story> getTopTenStories();

    Page<Story> getPastStories(Pageable pageable);

    List getComments(String storyId);
}
