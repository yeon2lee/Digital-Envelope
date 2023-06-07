package com.project.webcode.util;

import java.security.Key;

public interface KeyManage {

    Key save(String fileName, Key key);
    Key load(String fileName);
    Key bytesToKey(byte[] byteKey);
}
