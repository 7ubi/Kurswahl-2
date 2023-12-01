import {TapeResponse} from "../../../../app.responses";

export interface LessonsTable {
  hour: number;
  monday?: TapeResponse | null;
  tuesday?: TapeResponse | null;
  wednesday?: TapeResponse | null;
  thursday?: TapeResponse | null;
  friday?: TapeResponse | null;
}
