package com.example.clipdownloader1.config;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
/**치지직 관련 connection에 사용되는 url들을 모아둔 클래스*/
public class chzzkUrls {
    /**치지직 스트리머 검색url
     * 해당 결과에서 스트리머의 uid를 얻어낼 수 있다.
     * */
    public String streamerSearchUrl(
            String streamerName
    ) throws UnsupportedEncodingException {
        return "https://api.chzzk.naver.com/service/v1/search/channels?keyword="
                + URLEncoder.encode(streamerName, "UTF-8")
                +"&offset=0&size=1";
    }
    /**치지직 스트리머 클립 url
     *
     * */
    public String streamerClipSearch(
            String streamerUid,
            String orderType
    ){
        return "https://chzzk.naver.com/" +
                streamerUid+"/clips?orderType=" +
                orderType+"&clipType=ALL";
    }
}

