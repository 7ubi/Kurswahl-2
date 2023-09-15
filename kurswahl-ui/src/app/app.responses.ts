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
  readonly generatedPassword: number;
}

export interface AdminResponse extends UserResponse {
  readonly adminId: number;
}

export interface AdminResponses {
  readonly adminResponses: AdminResponse[];
}

export interface StudentResponse extends UserResponse {
  readonly studentId: number;
}

export interface StudentResponses {
  readonly studentResponses: StudentResponse[];
}

export interface TeacherResponse extends UserResponse {
  readonly teacherId: number;
}

export interface TeacherResponses {
  readonly teacherResponses: TeacherResponse[];
}

export enum Role {
  ADMIN = "ADMIN",
  TEACHER = "TEACHER",
  STUDENT = "STUDENT",
  NOROLE = "NOROLE"
}
