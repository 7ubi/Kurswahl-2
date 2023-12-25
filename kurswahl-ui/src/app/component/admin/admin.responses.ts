export interface LoginResponse {
  readonly token: string;
  readonly type: string;
  readonly id: string;
  readonly username: string;
  readonly role: Role;
  readonly name: string;
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

export interface StudentResponse extends UserResponse {
  readonly studentId: number;
  readonly studentClassResponse: StudentClassResponse;
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

export interface SubjectAreaResponse {
  readonly subjectAreaId: number;
  readonly name: string;
}

export interface SubjectAreaResponses {
  readonly subjectAreaResponses: SubjectAreaResponse[];
}

export interface SubjectResponse {
  readonly name: string;
  readonly subjectId: number;
  readonly subjectAreaResponse: SubjectAreaResponse;
}

export interface SubjectResponses {
  readonly subjectResponses: SubjectResponse[];
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

export interface LessonResponse {
  readonly lessonId: number;
  readonly day: number;
  readonly hour: number;
}

export interface TapeResponse {
  readonly tapeId: number;
  readonly name: string;
  readonly lk: boolean;
  readonly year: number;
  readonly releaseYear: number;
  readonly lessonResponses: LessonResponse[];
}

export interface TapeResponses {
  readonly tapeResponses: TapeResponse[];
}

export interface ClassResponse {
  readonly classId: number;
  readonly name: string;
  readonly teacherResponse: TeacherResponse;
  readonly subjectResponse: SubjectResponse;
  readonly tapeResponse: TapeResponse;
}

export interface ClassResponses {
  readonly classResponses: ClassResponse[];
}

export enum Role {
  ADMIN = "ADMIN",
  TEACHER = "TEACHER",
  STUDENT = "STUDENT",
  NOROLE = "NOROLE"
}
