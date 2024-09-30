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
    /**치지직 클립 여러개 보기
     * 스트리머이름 / 정렬기준 / 가져오는 사이즈 입력 필요
     *TODO  10개 가지고 온 후, 만약 clipUID와 readCount값이 들어온다면 그렇게 url 보내기
     *
     * */
    public String streamerClipSearch(
            String streamerUid,
            String orderType,
            int size
    ){
        String returnUrl = "";

        //https://api.chzzk.naver.com/service/v1/channels/f5c058c445257fa60fc75f91d52712fc/clips?orderType=POPULAR&size=10
        return "https://api.chzzk.naver.com/service/v1/channels/" +
                streamerUid+"/clips?" +
                "orderType="+orderType+
                "&size="+size;
    }

    public String streamerClipSearch(
            String streamerUid,
            String orderType,
            int size,
            String clipUID,
            int readCount
    ){
        String returnUrl = "";

        //https://api.chzzk.naver.com/service/v1/channels/f5c058c445257fa60fc75f91d52712fc/clips?orderType=POPULAR&size=10
        return "https://api.chzzk.naver.com/service/v1/channels/" +
                streamerUid+"/clips?" +
                "orderType="+orderType+
                "&size="+size;
    }
}

