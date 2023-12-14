import {ChoiceResponse, TapeClassResponse} from "../stundet.responses";

export class LessonForTable {
  tapeClass: TapeClassResponse | null;
  choice: ChoiceResponse | null;

  constructor() {
    this.tapeClass = null;
    this.choice = null;
  }
}

export interface LessonTable {
  hour: number;
  monday: LessonForTable;
  tuesday: LessonForTable;
  wednesday: LessonForTable;
  thursday: LessonForTable;
  friday: LessonForTable;
}
