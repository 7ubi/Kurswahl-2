import {TapeResponse} from "../../../admin.responses";

export interface LessonsTable {
  hour: number;
  monday?: TapeResponse | null;
  tuesday?: TapeResponse | null;
  wednesday?: TapeResponse | null;
  thursday?: TapeResponse | null;
  friday?: TapeResponse | null;
}

export interface Lesson {
  day: number,
  hour: number
}
