package cocodas.prier.point.kakao.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;

@Getter
@AllArgsConstructor
public class PayRequest {
    private String url;
    private LinkedMultiValueMap<String,String> map;
}