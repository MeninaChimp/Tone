package org.menina.tone.admin.controller;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by menina on 2017/10/2.
 */
@Data
@NoArgsConstructor
public class AppForm {

    private String owner;

    private String email;

    private String name;

    private String department;
}
