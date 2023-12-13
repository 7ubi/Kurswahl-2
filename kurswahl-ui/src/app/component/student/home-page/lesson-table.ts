import {TapeClassResponse} from "../stundet.responses";

export interface LessonTable {
  hour: number;
  monday?: TapeClassResponse | null;
  tuesday?: TapeClassResponse | null;
  wednesday?: TapeClassResponse | null;
  thursday?: TapeClassResponse | null;
  friday?: TapeClassResponse | null;
}
