package com.feedapp.repository;

import com.feedapp.model.Story;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StoryRepository extends MongoRepository<Story, String> {

}
