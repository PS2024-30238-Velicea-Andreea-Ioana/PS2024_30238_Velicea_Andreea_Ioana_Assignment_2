package com.example.untoldpsproject.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Mail {
    private String to;
    private String subject;
    private String body;

    @Override
    public String toString() {
        return to + "," + subject + "," + body;
    }
}
