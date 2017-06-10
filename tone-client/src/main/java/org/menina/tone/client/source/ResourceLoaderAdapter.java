package org.menina.tone.client.source;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Menina on 2017/6/10.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ResourceLoaderAdapter implements ResourceLoader{

    private String url;

}
