package org.menina.tone.client.listener;

import com.google.common.collect.Lists;
import java.util.List;

/**
 * Created by Menina on 2017/6/10.
 */
public class ListenerHolder {

    private static List<Listener> listenerList = Lists.newArrayList();

    public static void register(Listener listener){
        listenerList.add(listener);
    }

    public static List<Listener> getListenerList() {
        return listenerList;
    }
}
