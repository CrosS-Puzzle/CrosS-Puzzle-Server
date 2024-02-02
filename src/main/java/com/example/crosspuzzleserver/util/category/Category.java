package com.example.crosspuzzleserver.util.category;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum Category {

    NETWORK("NETWORK"),
    DS("DATA STRUCTURE"),
    DATABASE("DATABASE"),
    OS("OPERATING SYSTEM"),
    DESIGN_PATTERN("DESIGN PATTERN"),
    ALGORITHM("ALGORITHM");

    private final String name;

}
