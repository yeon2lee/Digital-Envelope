package com.project.webcode.util;

import java.security.Key;
import java.security.PublicKey;

public interface KeyManage {

    Key save(String fileName, Key key);
    Key load(String fileName);
    Key bytesToKey(byte[] byteKey);
}
