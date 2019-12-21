package com.qf.util;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

public class AlipayUtil {

   public static String publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqUZ29+oSwITBvRqPeFlZv0P99p7M2sGsVHCdFBDagrkuhWHSN04hVhcdjeKDDFp1L22Yce48t0bGrnxzvs3tAKxBWBS9T8mS8iyQ48aZQ9uzJS92iEOdToo2SF5WovtF3nqXzNejp6o2ZyOXyoAMhS3tHQO43cNWYJC+hdTCsVwIDAQAB";

    private static AlipayClient alipayClient;

    static {
        alipayClient = new DefaultAlipayClient(
                "https://openapi.alipay.com/gateway.do",
                "2016101500692726",
                "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKpRnb36hLAhMG9Go94WVm/Q/32nszawaxUcJ0UENqCuS6FYdI3TiFWFx2N4oMMWnUvbZhx7jy3RsaufHO+ze0ArEFYFL1PyZLyLJDjxplD27MlL3aIQ51OijZIXlai+0XeepfM16OnqjZnI5fKgAyFLe0dA7jdw1ZgkL6F1MKxXAgMBAAECgYAEpOMTOhCCKPWEIz32oU9MDzvYNklptVfGJJVhiWLf8MC5LotHDryJ8HLmYHSpvSUe22WmYywdSLl6f4UveAplQvh80F8kZuo0JBJhgRnOG5aTmjEggxWN0W3U9JsO4T/N/RXQ0AYy3qvGEIPPlwFxOHRl7IMuU7kt7RjvXkyyEQJBAPg3unqg0KWwBwALt+3Re69Lqq+aro88tiRh/gKGi9W2p7JIEVAmWbUtM2K5VQN9x7d6Li9Fu4tQSLL6vkXEqmkCQQCvqKW7jouZsPn7DD6eN8syCvWHYoKXAJVUY64Xor2+dZpca++sT/kXV/xZAzMN+ckL9wQF5WQhL7Fj51GaSEi/AkAvcvfC2VsJUU0FRTgzzgfGysAmMXuNBh4LdnAIK5AvmmsUOZKVr78WUz2vcZ4+vJyhBkot3zaquv4quKQG0kGBAkAB8tX70aX8y3jKbyVWrfnEEVkN6mrWb/w4Fm789iIyta1DrzLWb4Cs4J39AssTMZ0oU9IompXUxGmsWQmPs5OTAkAUfN2bYc6nkcqMzgWWZ/Yu+UE1wp2HkcKmQo5yiw9/r8Gqina/yJbaqCQGpf9He39Zb/VEI2zvG2exglXAX2xY",
                "json",
                "UTF-8",
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqUZ29+oSwITBvRqPeFlZv0P99p7M2sGsVHCdFBDagrkuhWHSN04hVhcdjeKDDFp1L22Yce48t0bGrnxzvs3tAKxBWBS9T8mS8iyQ48aZQ9uzJS92iEOdToo2SF5WovtF3nqXzNejp6o2ZyOXyoAMhS3tHQO43cNWYJC+hdTCsVwIDAQAB",
                "RSA"); //获得初始化的AlipayClient
    }

    public static AlipayClient getAlipayClient(){
        return alipayClient;
    }

}
