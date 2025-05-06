// src/main/java/com/example/cab_booking/payload/request/RoleUpdateRequest.java
package com.example.cab_booking.payload.request;

import com.example.cab_booking.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateRequest {

    /** New role to assign (ADMIN · DRIVER · CUSTOMER). */
    @NotNull
    private Role role;

    public Role getRole(){
        return role;
    }

    public void setRole(Role role){
        this.role = role;
    }
}
