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


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Comment;
import models.User;
import models.Post;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.APICall;
import util.Constants;

import java.util.ArrayList;
import java.util.List;

public class PostController extends Controller {
    final static Form<Post> postForm = Form.form(Post.class);
    final static Form<Comment> commentForm = Form.form(Comment.class);

    public static Result newPost() {
        Form<Post> dc = postForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();
        String id = "";
        try {
            jsonData.put("authorId", dc.field("authorId").value());
            id = dc.field("authorId").value();
            jsonData.put("content", dc.field("postContent").value());
            jsonData.put("security", dc.field("security").value());
            if ("yes".equals(dc.field("includeL").value())) {
            jsonData.put("location", dc.field("location").value());
            }
            JsonNode response = Post.create(jsonData);
            System.out.println("post created with response: " + response);
            Application.flashMsg(response);
        } catch (IllegalStateException e) {
            e.printStackTrace();Application.flashMsg(APICall.createResponse(APICall.ResponseType.CONVERSIONERROR));
        } catch (Exception e) {
            e.printStackTrace();
            Application.flashMsg(APICall.createResponse(APICall.ResponseType.UNKNOWN));
        }
        return redirect("/network/home/" + id);
    }

    public static Result updatePost() {
        Form<Post> dc = postForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();
        try {
            jsonData.put("postId", dc.field("postId").value());
            if (dc.field("content").value() != null && !dc.field("content").value().isEmpty()) {
                jsonData.put("content", dc.field("content").value());
            }
            if (dc.field("numOfLikes").value() != null && !dc.field("numOfLikes").value().isEmpty()) {
                jsonData.put("numOfLikes", dc.field("numOfLikes").value());
            }

            JsonNode response = Post.update(jsonData);
            Application.flashMsg(response);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Application.flashMsg(APICall
                    .createResponse(APICall.ResponseType.CONVERSIONERROR));
        } catch (Exception e) {
            e.printStackTrace();
            Application.flashMsg(APICall.createResponse(APICall.ResponseType.UNKNOWN));
        }

        return ok("updated");
    }

    public static Result deletePost() {
        Form<Post> dc = postForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();
        try {
            jsonData.put("postId", dc.field("postId").value());
            JsonNode response = Post.delete(jsonData);
            Application.flashMsg(response);
        }catch (IllegalStateException e) {
            e.printStackTrace();
            Application.flashMsg(APICall
                    .createResponse(APICall.ResponseType.CONVERSIONERROR));
        } catch (Exception e) {
            e.printStackTrace();
            Application.flashMsg(APICall.createResponse(APICall.ResponseType.UNKNOWN));
        }
        return ok("deleted");
    }

    public static Result sharePost() {
        Form<Post> dc = postForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();
        try {
            jsonData.put("postId", dc.field("postId").value());
            jsonData.put("sharerId", session("current_user"));
            JsonNode response = Post.share(jsonData);
            Application.flashMsg(response);
        }catch(IllegalStateException e) {
            e.printStackTrace();
            Application.flashMsg(APICall
                    .createResponse(APICall.ResponseType.CONVERSIONERROR));
        } catch (Exception e) {
            e.printStackTrace();
            Application.flashMsg(APICall.createResponse(APICall.ResponseType.UNKNOWN));
        }
        return ok("post shared");
    }

    public static Result addComment() {
        Form<Comment> dc = commentForm.bindFromRequest();
        ObjectNode jsonData = Json.newObject();
        try {
            jsonData.put("postId", dc.field("postId").value());
            jsonData.put("content", dc.field("content").value());
            jsonData.put("commenterId", session("current_user"));
            JsonNode reponse = Comment.create(jsonData);
        }catch (IllegalStateException e) {
            e.printStackTrace();
            Application.flashMsg(APICall
                    .createResponse(APICall.ResponseType.CONVERSIONERROR));
        } catch (Exception e) {
            e.printStackTrace();
            Application.flashMsg(APICall.createResponse(APICall.ResponseType.UNKNOWN));
        }
        return ok("comment added");
    }

    public static Result changeSecurity(String postID, String security){
        Post.changeSecurity(postID,security);
        return ok("security changed");
    }
}
