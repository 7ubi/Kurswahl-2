export interface TapeClassResponse {
  readonly tapeId: number;
  readonly name: string;
  readonly classResponses: ClassResponse[];
  readonly lessonResponses: LessonResponse[];
}

export interface ClassResponse {
  readonly classId: number;
  readonly name: string;
}

export interface LessonResponse {
  day: number;
  hour: number;
}
