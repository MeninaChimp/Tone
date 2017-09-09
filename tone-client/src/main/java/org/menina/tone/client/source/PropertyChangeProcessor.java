package org.menina.tone.client.source;

import java.util.Map;

/**
 * Created by Menina on 2017/6/10.
 */
public interface PropertyChangeProcessor
{
    Map<String, String> reloadProperty(String nodePath);

    void notifier(Map<String, String> data);

}
