import {ClassChoiceResponse, TapeResponse} from "../../admin.responses";

export class ChoiceTable {
  tape: TapeResponse;
  firstChoice?: ClassChoiceResponse;
  secondChoice?: ClassChoiceResponse;

  constructor(tape: TapeResponse) {
    this.tape = tape;
  }
}
