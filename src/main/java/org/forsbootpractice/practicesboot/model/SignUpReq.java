package org.forsbootpractice.practicesboot.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SignUpReq {
    private String name;
    private String password;
    private String part;

    private MultipartFile profile;
    private String profileUrl;
}
