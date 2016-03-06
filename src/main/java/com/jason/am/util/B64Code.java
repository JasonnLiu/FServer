package com.jason.am.util;

import java.io.IOException;

public class B64Code {
	
	static final char[] __rfc1421alphabet=
        {
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P',
            'Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f',
            'g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v',
            'w','x','y','z','0','1','2','3','4','5','6','7','8','9','+','/'
        };
	
	public static void encode(long lvalue,Appendable buf) throws IOException {
        int value=(int)(0xFFFFFFFC&(lvalue>>32));
        buf.append(__rfc1421alphabet[0x3f&((0xFC000000&value)>>26)]);
        buf.append(__rfc1421alphabet[0x3f&((0x03F00000&value)>>20)]);
        buf.append(__rfc1421alphabet[0x3f&((0x000FC000&value)>>14)]);
        buf.append(__rfc1421alphabet[0x3f&((0x00003F00&value)>>8)]);
        buf.append(__rfc1421alphabet[0x3f&((0x000000FC&value)>>2)]);
        
        buf.append(__rfc1421alphabet[0x3f&((0x00000003&value)<<4) + (0xf&(int)(lvalue>>28))]);
        
        value=0x0FFFFFFF&(int)lvalue;
        buf.append(__rfc1421alphabet[0x3f&((0x0FC00000&value)>>22)]);
        buf.append(__rfc1421alphabet[0x3f&((0x003F0000&value)>>16)]);
        buf.append(__rfc1421alphabet[0x3f&((0x0000FC00&value)>>10)]);
        buf.append(__rfc1421alphabet[0x3f&((0x000003F0&value)>>4)]);
        buf.append(__rfc1421alphabet[0x3f&((0x0000000F&value)<<2)]);
    }
	
}
