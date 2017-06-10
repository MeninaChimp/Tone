package org.menina.tone.client.source;

import java.util.Map;

/**
 * Created by Menina on 2017/6/10.
 */
public abstract class ProerptyChangeListenerAdapter implements PropertyChangeListener{

    private ResouceLoader resouceLoader;

    public void setResouceLoader(ResouceLoader resouceLoader) {
        this.resouceLoader = resouceLoader;
    }

    abstract Map<String, String> loadKey(String key);
}
