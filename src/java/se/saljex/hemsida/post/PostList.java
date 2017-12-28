/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.saljex.hemsida.post;
import java.util.ArrayList;

/**
 *
 * @author ulf
 */
public class PostList {
    private ArrayList<Post> posts = new ArrayList<>();
    private Integer displayItems=null;
    private String displayWidth="";
    private boolean showFullContent=false;
    private Post currentPost=null;

    public Post getCurrentPost() {
        return currentPost;
    }

    public void setCurrentPost(Post currentPost) {
        this.currentPost = currentPost;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public Integer getDisplayItems() {
        return displayItems;
    }

    public void setDisplayItems(Integer displayItems) {
        this.displayItems = displayItems;
    }

    public String getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(String displayWidth) {
        this.displayWidth = displayWidth;
    }

    public boolean isShowFullContent() {
        return showFullContent;
    }

    public void setShowFullContent(boolean showFullContent) {
        this.showFullContent = showFullContent;
    }
    
  
}
