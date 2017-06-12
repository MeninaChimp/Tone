package org.menina.tone.client.source;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Menina on 2017/6/10.
 */
@Data
@NoArgsConstructor
public abstract class ResourceLoaderAdapter<T> implements ResourceLoader<T>{

    private String url;

    private T resourceContainerListener;

}
