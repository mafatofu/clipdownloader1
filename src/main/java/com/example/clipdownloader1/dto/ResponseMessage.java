package com.example.clipdownloader1.dto;

import lombok.*;

/**ResponseEntity를 사용하여 return 시 사용되는 메시지용 클래스*/
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ResponseMessage {
    private StatusEnum status;
    private String message;
    private Object data;

    public ResponseMessage() {
        this.status = StatusEnum.BAD_REQUEST;
        this.data = null;
        this.message = null;
    }
}
