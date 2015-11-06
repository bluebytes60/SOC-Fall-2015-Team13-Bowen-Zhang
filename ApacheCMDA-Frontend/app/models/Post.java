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
package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.JsonNode;
import play.data.validation.Constraints;
import play.mvc.Result;
import util.APICall;
import util.Constants;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String authorId;
    private String content;
    private int numOfLikes;
    private long timestamp;

    public static String getADD_POST_CALL() {
        return ADD_POST_CALL;
    }

    private String authorName;

    private static final String ADD_POST_CALL = Constants.NEW_BACKEND + "post";
    private static final String GET_POST_CALL = Constants.NEW_BACKEND + "post/personal/";

    public Post() {
    }

    public Post(String authorId, String content) {
        this.authorId = authorId;
        this.content = content;
        this.numOfLikes = 0;
        this.timestamp = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setNumOfLikes(int numOfLikes) {
        this.numOfLikes = numOfLikes;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthorId() {
        return authorId;
    }

    public int getNumOfLikes() {
        return numOfLikes;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     * Create a new post
     *
     * @param jsonData
     * @return the response from the API server
     */
    public static JsonNode create(JsonNode jsonData) {
        return APICall.postAPI(ADD_POST_CALL, jsonData);
    }

    /**
     * Get a user based on its username
     *
     * @return
     */

    //{"id":5,"authorID":1,"authorName":"bluebyte60","content":"ff","likes":0,"timeStamp":1446785065268}
    public static List<Post> get(String id) {
        JsonNode json = APICall.callAPI(GET_POST_CALL + id);
        List<Post> posts = new ArrayList<>();
        for (JsonNode node : json) {
            Post p = new Post();
            p.setContent(node.path("content").asText());
            p.setAuthorName(node.path("authorName").asText());
            p.setTimestamp(node.path("timeStamp").asLong());
            p.setAuthorId(node.path("authorID").asText());
            p.setNumOfLikes(node.path("likes").asInt());
            posts.add(p);
            System.out.println(posts);
        }
        return posts;
    }

    @Override
    public String toString() {
        return "Post [id=" + id + ", authorId=" + authorId + " authorName=" + authorName + ", content="
                + content + ", numOfLikes=" + numOfLikes + ", timestamp="
                + timestamp + "]";
    }
}