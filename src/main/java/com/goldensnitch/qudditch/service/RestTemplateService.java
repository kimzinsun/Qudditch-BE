package com.goldensnitch.qudditch.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
/*
    Builder를 먼저 주입받은 후 RestTemplate 호출하여 의존관계 주입
    -> PaymentService에서 사용

    - HTTP 요청 준비: 외부 API 호출을 위한 RestTemplate 인스턴스를 생성하고, 필요한 경우 기본 설정을 적용합니다.
    - API 호출의 캡슐화: 외부 시스템과의 통신을 담당하는 로직을 이 서비스 클래스 내에서 관리함으로써,
                       비즈니스 로직에서는 HTTP 통신의 세부 사항을 신경 쓰지 않고도 데이터를 주고받을 수 있게 합니다.
    - 응답 처리: 외부 시스템으로부터의 응답을 적절히 처리하고, 필요한 데이터 변환 작업을 수행합니다.
 */
@Service
public class RestTemplateService {
    private final RestTemplate restTemplate;

    public RestTemplateService(RestTemplateBuilder builder){
        restTemplate = builder.build();
    }
}
