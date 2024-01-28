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

export interface TeacherResponse extends UserResponse {
  readonly teacherId: number;
  readonly abbreviation: string;
}

export interface SubjectAreaResponse {
  readonly subjectAreaId: number;
  readonly name: string;
}

export interface SubjectResponse {
  readonly name: string;
  readonly subjectId: number;
  readonly subjectAreaResponse: SubjectAreaResponse;
}

export interface StudentClassResponse {
  readonly studentClassId: number;
  readonly name: string;
  readonly students: StudentResponse[];
  readonly teacher: TeacherResponse;
  readonly year: number;
  readonly releaseYear: number;
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

export interface ClassResponse {
  readonly classId: number;
  readonly name: string;
  readonly teacherResponse: TeacherResponse;
  readonly subjectResponse: SubjectResponse;
  readonly tapeResponse: TapeResponse;
}

export interface RuleResponse {
  readonly ruleId: number;
  readonly name: string;
  readonly subjectResponses: SubjectResponse[];
  readonly year: number;
}

export interface ChoiceSurveillanceResponse {
  readonly studentSurveillanceResponse: StudentSurveillanceResponse;
  readonly chosen: boolean;
  readonly fulfilledRules: boolean;
}

export interface StudentSurveillanceResponse extends UserResponse {
  readonly studentClassId: number;
  readonly studentId: number;
  readonly name: string;
}

export interface ClassStudentsResponse {
  readonly name: string;
  readonly teacherResponse: TeacherResponse;
  readonly studentSurveillanceResponses: StudentSurveillanceResponse[];
  readonly tapeName: string;
}

export interface StudentChoiceResponse extends StudentSurveillanceResponse {
  readonly choiceResponses: ChoiceResponse[];
}

export interface ChoiceResponse {
  readonly choiceNumber: number;
  readonly classChoiceResponses: ClassChoiceResponse[];
}

export interface ClassChoiceResponse {
  readonly classId: number;
  readonly name: string;
  readonly tapeId: number;
  readonly teacherResponse: TeacherResponse;
  readonly selected: boolean;
  readonly choiceClassId: number;
}

export enum Role {
  ADMIN = "ADMIN",
  TEACHER = "TEACHER",
  STUDENT = "STUDENT",
  NOROLE = "NOROLE"
}
