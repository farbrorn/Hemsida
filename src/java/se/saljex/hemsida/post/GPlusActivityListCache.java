/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.saljex.hemsida.post;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ulf
 */

public class GPlusActivityListCache {
    
    private static ArrayList<GPlusActivityListCache> activityList = new ArrayList<>();
    
  
    private String id;
    private String apiKey;

    public GPlusActivityListCache(String id, String apiKey) {
        this.id = id;
        this.apiKey=apiKey;
    }
    

   private  static GPlusActivityListCache getObject(String id) {
       if (id!=null) for (GPlusActivityListCache a : activityList) {
           if (id.equals(a.id)) return a; 
       }
       return null;
   }
   
   public static void refreshAll() {
       for (GPlusActivityListCache a : activityList) {
           a.refresh();
       }
   }

   public static PostList getPostList(String id, String apiKey) {
       GPlusActivityListCache r = getObject(id);
       if (r==null) {
           r = new GPlusActivityListCache(id, apiKey);
           activityList.add(r);
       }
       if (r.needsRefresh()) {
           r.refresh();
       }
       
       return r.getPostList();
   }


   
  private String responseText = null;
  private ArrayList<Post> activities = new ArrayList<>();
  
  public String getResponseText() { return responseText; }

  public PostList getPostList() {
      PostList pl = new PostList();
      pl.setPosts(activities);
      return pl;
  }

  public boolean needsRefresh() {
      return responseText==null;
  }
  public void refresh()  {
    InputStream is=null;
    try {
        URL url = getURL();
        is=url.openStream();

        ByteArrayOutputStream bstreamResult = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            bstreamResult.write(buffer, 0, length);
        }
        responseText=bstreamResult.toString("UTF-8");            
        
        
        
        JsonReader rdr = Json.createReader(new StringReader(responseText));
        JsonObject lst = rdr.readObject();
        JsonArray marr = lst.getJsonArray("items");

        activities = new ArrayList<>();
        for ( JsonObject obj : marr.getValuesAs(JsonObject.class)) {
            Post activity = new Post();
            activities.add(activity);
            activity.setTitle(obj.getString("title", ""));
            activity.setPublished(obj.getString("published", ""));
            activity.setContent(obj.getJsonObject("object").getString("content", ""));
            activity.setUrl(obj.getString("url", ""));

            JsonArray jarr = obj.getJsonObject("object").getJsonArray(("attachments"));
            for (JsonObject result : jarr.getValuesAs(JsonObject.class)) {
                Post.Attachment attachment = activity.newAttachment();
                
                attachment.setImageUrl(result.getJsonObject("image").getString("url", ""));
                attachment.setObjectType(result.getString("objectType", ""));
                attachment.setUrl(result.getString("url", ""));
            }
        }
        
    } 
    catch (Exception e) {System.out.print("Fel vid refresh. id="+ id + " - " + e.toString() + e.getMessage());}
    finally {
        try { if (is!=null) is.close(); } catch(Exception e) {}
    }
  }

  public URL getURL() throws MalformedURLException {
      String c = "https://www.googleapis.com/plus/v1/people/" + id + "/activities/public?key=" + apiKey;
      return new URL(c);
  }

  
}
