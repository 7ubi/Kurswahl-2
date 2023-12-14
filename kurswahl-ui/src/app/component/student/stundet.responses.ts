export interface TapeClassResponse {
  readonly tapeId: number;
  readonly name: string;
  readonly classResponses: ClassResponse[];
  readonly lessonResponses: LessonResponse[];
}

export interface ChoiceResponse {
  readonly choiceNumber: number;
  readonly classResponses: ClassResponse[];
}

export interface ClassResponse {
  readonly classId: number;
  readonly name: string;
  readonly teacherResponse: TeacherResponse;
}

export interface LessonResponse {
  readonly day: number;
  readonly hour: number;
}

export interface TeacherResponse {
  readonly abbreviation: string;
}
