package com.x7ubi.kurswahl.admin.user.mapper;

import com.x7ubi.kurswahl.admin.user.request.AdminSignupRequest;
import com.x7ubi.kurswahl.admin.user.response.AdminResponse;
import com.x7ubi.kurswahl.common.models.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AdminMapper {
    @Mapping(source = "surname", target = "user.surname")
    @Mapping(source = "firstname", target = "user.firstname")
    Admin adminRequestToAdmin(AdminSignupRequest adminSignupRequest);

    @Mapping(source = "surname", target = "user.surname")
    @Mapping(source = "firstname", target = "user.firstname")
    void adminRequestToAdmin(AdminSignupRequest adminSignupRequest, @MappingTarget Admin admin);

    List<AdminResponse> adminsToAdminResponseList(List<Admin> admins);

    @Mapping(source = "user.surname", target = "surname")
    @Mapping(source = "user.firstname", target = "firstname")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.generatedPassword", target = "generatedPassword")
    @Mapping(source = "user.userId", target = "userId")
    AdminResponse adminToAdminResponse(Admin admin);


}
