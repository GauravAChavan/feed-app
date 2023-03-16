package com.feedapp;

import com.feedapp.model.Story;
import com.feedapp.repository.StoryRepository;
import com.feedapp.service.StoryService;
import com.feedapp.service.impl.StoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoryServiceImplTest {
    @Mock
    private StoryRepository storyRepository;

    @InjectMocks
    private StoryServiceImpl storyService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void get_all_story() {
        //Given
        Page<Story> storyPage = mock(Page.class);
        Pageable paging = PageRequest.of(0, 1);
        when(storyRepository.findAll(paging)).thenReturn(storyPage);

        //When
        Page<Story> pastStories = storyService.getPastStories(paging);

        //Then
        assertEquals(storyPage, pastStories);
    }


    @Test
    public void save_story() {
        //Given
        Story story = new Story();
        story.setId("1");
        List<Story> stories = Collections.singletonList(story);
        when(storyRepository.saveAll(stories)).thenReturn(stories);

        //When
        List<Story> result = storyService.saveStories(stories);

        //Then
        assertEquals(stories, result);
    }

}