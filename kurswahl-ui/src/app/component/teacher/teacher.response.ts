export interface TeacherClassStudentResponse {
  readonly firstname: string;
  readonly surname: string;
  readonly studentClassName: string;
}

export interface TeacherClassResponse {
  readonly name: string;
  readonly teacherClassStudentResponses: TeacherClassStudentResponse[];
}
