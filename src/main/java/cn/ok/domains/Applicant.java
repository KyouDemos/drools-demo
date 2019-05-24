package cn.ok.domains;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author kyou on 2019-05-24 07:57
 */
@Data
@AllArgsConstructor
public class Applicant {
    private String name;
    private int age;
    private boolean valid;
}
