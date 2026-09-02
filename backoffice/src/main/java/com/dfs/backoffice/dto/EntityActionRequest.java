package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityActionRequest {
    private McActionRequest mcActionRequest;
    private CreateUserRequest createUserRequest;
}
