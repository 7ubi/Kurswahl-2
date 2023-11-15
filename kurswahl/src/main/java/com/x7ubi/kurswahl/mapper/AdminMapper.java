package com.x7ubi.kurswahl.mapper;

import com.x7ubi.kurswahl.models.Admin;
import com.x7ubi.kurswahl.request.admin.AdminSignupRequest;
import com.x7ubi.kurswahl.response.admin.user.AdminResponse;
import com.x7ubi.kurswahl.response.admin.user.AdminResponses;
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

    default AdminResponses adminsToAdminResponses(List<Admin> admins) {
        AdminResponses adminResponses = new AdminResponses();
        adminResponses.setAdminResponses(adminsToAdminResponseList(admins));
        return adminResponses;
    }

    List<AdminResponse> adminsToAdminResponseList(List<Admin> admins);

    @Mapping(source = "user.surname", target = "surname")
    @Mapping(source = "user.firstname", target = "firstname")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.generatedPassword", target = "generatedPassword")
    AdminResponse adminToAdminResponse(Admin admin);


}