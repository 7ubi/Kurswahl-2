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
  readonly userId: number;
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

export interface AdminResultResponse extends ResultResponse {
  readonly adminResponse: AdminResponse;
}

export interface StudentResponse extends UserResponse {
  readonly studentId: number;
  readonly studentClassResponse: StudentClassResponse;
}

export interface StudentResultResponse extends ResultResponse {
  readonly studentResponse: StudentResponse;
}

export interface StudentResponses {
  readonly studentResponses: StudentResponse[];
}

export interface TeacherResponse extends UserResponse {
  readonly teacherId: number;
  readonly abbreviation: string;
}

export interface TeacherResponses {
  readonly teacherResponses: TeacherResponse[];
}

export interface TeacherResultResponse extends ResultResponse {
  readonly teacherResponse: TeacherResponse;
}

export interface SubjectAreaResponse {
  readonly subjectAreaId: number;
  readonly name: string;
}

export interface SubjectAreaResponses {
  readonly subjectAreaResponses: SubjectAreaResponse[];
}

export interface SubjectAreaResultResponse extends ResultResponse {
  readonly subjectAreaResponse: SubjectAreaResponse;
}

export interface SubjectResponse {
  readonly name: string;
  readonly subjectId: number;
  readonly subjectAreaResponse: SubjectAreaResponse;
}

export interface SubjectResponses {
  readonly subjectResponses: SubjectResponse[];
}

export interface SubjectResultResponse extends ResultResponse {
  readonly subjectResponse: SubjectResponse;
}

export interface StudentClassResponse {
  readonly studentClassId: number;
  readonly name: string;
  readonly students: StudentResponse[];
  readonly teacher: TeacherResponse;
  readonly year: number;
  readonly releaseYear: number;
}

export interface StudentClassResponses {
  readonly studentClassResponses: StudentClassResponse[];
}

export interface StudentClassResultResponse extends ResultResponse {
  readonly studentClassResponse: StudentClassResponse;
}

export enum Role {
  ADMIN = "ADMIN",
  TEACHER = "TEACHER",
  STUDENT = "STUDENT",
  NOROLE = "NOROLE"
}
