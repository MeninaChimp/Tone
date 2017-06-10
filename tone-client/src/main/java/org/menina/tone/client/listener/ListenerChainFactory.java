package org.menina.tone.client.listener;

import java.util.Map;

/**
 * Created by Menina on 2017/6/10.
 */
public class ListenerChainFactory {

    private volatile static ListenerChain listenerChain;

    public ListenerChain getListenerChain(){
        if(null == listenerChain){
            synchronized (ListenerChainFactory.class){
                if (null == listenerChain){
                    listenerChain = this.buildListenerChain();
                }
            }
        }

        return this.buildListenerChain();
    }

    private ListenerChain buildListenerChain(){
        ListenerChain last = new InitializedListenerChain();
        for(int i = 0; i < ListenerHolder.getListenerList().size() - 1; i++){
            final Listener listener = ListenerHolder.getListenerList().get(i);
            final ListenerChain next = last;
            last = new ListenerChain() {
                @Override
                public void invoke(Map.Entry<String, String> data) {
                    listener.propertyChange(next, data);
                }
            };
        }

        return last;
    }
}
