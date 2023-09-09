export interface MessageResponse {
  readonly message: string;
}

export interface ResultResponse {
  readonly errorMessages: MessageResponse[];
}

export interface LoginResponse {
  readonly token: string;
  readonly type: string;
  readonly id: string;
  readonly username: string;
  readonly role: Role;
}

export interface UserResponse {
  readonly username: string;
  readonly firstname: string;
  readonly surname: string;
}

export interface AdminResponse extends UserResponse {}

export interface AdminResponses {
  readonly adminResponses: AdminResponse[];
}

export enum Role {
  ADMIN = "ADMIN",
  TEACHER = "TEACHER",
  STUDENT = "STUDENT",
  NOROLE = "NOROLE"
}
