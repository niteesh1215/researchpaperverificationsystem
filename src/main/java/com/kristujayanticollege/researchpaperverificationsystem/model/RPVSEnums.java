package com.kristujayanticollege.researchpaperverificationsystem.model;

import com.kristujayanticollege.researchpaperverificationsystem.projectenums.VerificationStatus;

public class RPVSEnums {

    public static String getVerificationStatusEnumValue(VerificationStatus status) {
        switch (status) {
            case FOUND:
                return "found";
            case NOT_FOUND:
                return "not_found";
            case PARTIAL_MATCH:
                return "partial_match";
            case FAILED:
                return "failed";
            case NOT_VERIFIED:
                return "not_verified";
            default:
                return "not_verified";
        }
    }

    public static VerificationStatus getVerificationStatusEnum(String value) {
        switch (value) {
            case "found":
                return VerificationStatus.FOUND;
            case "not_found":
                return VerificationStatus.NOT_FOUND;
            case "partial_match":
                return VerificationStatus.PARTIAL_MATCH;
            case "failed":
                return VerificationStatus.FAILED;
            case "not_verified":
                return VerificationStatus.NOT_VERIFIED;
            default:
                return VerificationStatus.NOT_VERIFIED;
        }
    }

}
