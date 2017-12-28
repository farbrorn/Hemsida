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
public class Post {
   private String title;
   private String published;
   private String content;
   private String url;
   private ArrayList<Attachment> attachments = new ArrayList<>();

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPublished() {
            return published;
        }
        public String getPublishedDatum() {
            try {
                return published.substring(0,10);
            } catch (Exception e) { return ""; }
        }

        public void setPublished(String published) {
            this.published = published;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public ArrayList<Attachment> getAttachments() {
            return attachments;
        }

        public void setAttachments(ArrayList<Attachment> attachments) {
            this.attachments = attachments;
        }
        
        public Attachment newAttachment() {
            Attachment a = new Attachment();
            attachments.add(a);
            return a;
        }
           
   
public class Attachment {
    private String imageUrl;
    private String objectType;
    private String url;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getObjectType() {
            return objectType;
        }

        public void setObjectType(String objectType) {
            this.objectType = objectType;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    
}
}
