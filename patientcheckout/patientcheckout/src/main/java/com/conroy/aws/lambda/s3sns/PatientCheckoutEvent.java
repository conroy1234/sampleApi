package com.conroy.aws.lambda.s3sns;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PatientCheckoutEvent {
    private String firstName;
    private String middleName;
    private String lastName;
    private String ssn;
}
