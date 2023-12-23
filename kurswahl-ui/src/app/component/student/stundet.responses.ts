export interface TapeClassResponse {
  readonly tapeId: number;
  readonly name: string;
  readonly classResponses: ClassResponse[];
  readonly lessonResponses: LessonResponse[];
}

export interface ChoiceResponse {
  readonly choiceId: number;
  readonly choiceNumber: number;
  readonly classChoiceResponses: ClassChoiceResponse[];
}

export interface ClassChoiceResponse {
  readonly classId: number;
  readonly name: string;
  readonly tapeId: number;
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

export interface SubjectTapeResponse {
  readonly name: string;
  readonly tapeResponses: TapeResponse[];
}

export interface TapeResponse {
  readonly name: string;
}

export interface TapeResponses {
  readonly tapeClassResponses: TapeClassResponse[];
  readonly subjectTapeResponses: SubjectTapeResponse[];
}
