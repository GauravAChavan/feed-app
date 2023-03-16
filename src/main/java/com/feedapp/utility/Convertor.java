package com.feedapp.utility;

import com.feedapp.model.Comment;
import com.feedapp.model.Story;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Convertor {

    public static Story convertMapStoryToStoryObject(Map storyMap) {
        Story story = new Story();
        story.setTitle((String) storyMap.getOrDefault("title", ""));
        String epochString = storyMap.getOrDefault("time", "").toString();
        long epoch = Long.parseLong(epochString);
        Date date = new Date(epoch * 1000);
        story.setSubmissionTime(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        story.setUrl((String) storyMap.getOrDefault("url", ""));
        story.setScore((Integer) storyMap.getOrDefault("score", 0));
        story.setUser((String) storyMap.getOrDefault("by", ""));
        return story;
    }


    public static Comment convertMapCommentToCommentObject(Map commentMap) {
        Comment comment = new Comment();
        comment.setText((String) commentMap.getOrDefault("text", ""));
        comment.setUser((String) commentMap.getOrDefault("by", ""));
        comment.setKids(((List) commentMap.getOrDefault("kids", new ArrayList<>())).size());
        return comment;
    }
}
