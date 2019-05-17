package cn.ok.domains;

import lombok.Data;

/**
 * @author kyou on 2019-05-17 09:46
 */
@Data
public class Message {
    public static final int HELLO = 0;
    public static final int GOODBYE = 1;

    private String message;
    private int status;
}
