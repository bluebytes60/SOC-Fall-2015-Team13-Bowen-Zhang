/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package controllers;

import com.google.gson.Gson;
import models.Post;
import models.PostRepository;
import models.User;
import models.UserRepository;
import play.mvc.Controller;
import play.mvc.Result;
import search.PostSearch;
import search.UserSearch;
import search.SearchMode;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * The main set of web services.
 */
@Named
@Singleton
public class SearchController extends Controller {

    private UserSearch searchUser = new UserSearch();
    private PostSearch searchPost = new PostSearch();
    private UserRepository userRepository;
    private PostRepository postRepository;

    // We are using constructor injection to receive a repository to support our
    // desire for immutability.
    @Inject
    public SearchController(final UserRepository userRepository, final PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public Result searchPost(String keyword) {
        List<Post> result = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        try {
            ids = searchPost.basicSearch(parse(keyword), SearchMode.FUZZY, "content");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String id : ids) {
            List<Post> posts= postRepository.findPostByPostID(Long.valueOf(id));
            for(Post p:posts) {
                User user = userRepository.findByID(p.getAuthorID());
                p.setAuthorName(user.getUserName());
            }
            result.addAll(posts);
        }
        return ok(new Gson().toJson(result));

    }

    public Result defaultSearch(String keyword) {
        List<User> result = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        try {
            ids = searchUser.basicSearch(parse(keyword), SearchMode.FUZZY, "default");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String id : ids) result.add(userRepository.findByID(Long.valueOf(id)));
        return ok(new Gson().toJson(result));
    }

    public Result fuzzySearch(String keyword, String field) {
        List<User> result = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        try {
            ids = searchUser.basicSearch(parse(keyword), SearchMode.FUZZY, field);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String id : ids) result.add(userRepository.findByID(Long.valueOf(id)));
        return ok(new Gson().toJson(result));
    }


    public Result exactlySearch(String keyword, String field) {
        List<User> result = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        try {
            ids = searchUser.basicSearch(parse(keyword), SearchMode.FUZZY, field);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String id : ids) result.add(userRepository.findByID(Long.valueOf(id)));
        return ok(new Gson().toJson(result));
    }

    private String parse(String keyword) {
        return keyword.replace("_", " ");
    }

}