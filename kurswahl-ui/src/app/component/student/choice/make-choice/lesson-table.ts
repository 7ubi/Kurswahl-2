import {ClassChoiceResponse, TapeClassResponse} from "../../stundet.responses";

export class LessonForTable {
  tapeClass: TapeClassResponse | null;
  choice: ClassChoiceResponse | null;

  constructor() {
    this.tapeClass = null;
    this.choice = null;
  }
}

export interface LessonTable {
  hour: number;
  days: LessonForTable[];
}
