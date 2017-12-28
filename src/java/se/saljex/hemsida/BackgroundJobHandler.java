/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.saljex.hemsida;

/**
 *
 * @author ulf
 */
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import se.saljex.hemsida.post.GPlusActivityListCache;

/**
 *
 * @author ulf
 */
@Singleton
public class BackgroundJobHandler {

//    @Schedule(hour="0", minute="0", second="0", persistent=false)
//    public void daily() {
//    }
    

//    @Schedule(hour="*/1", minute="0", second="0", persistent=false)
//    public void hourly() {
//    }

    @Schedule(hour="*", minute="*/15", second="0", persistent=false)
    public void quarterly() {
        GPlusActivityListCache.refreshAll();
    }
  

}
