import {ChoiceTapeResponse, ClassChoiceResponse} from "../../admin.responses";

export class ChoiceTable {
  tape: ChoiceTapeResponse;
  firstChoice?: ClassChoiceResponse;
  secondChoice?: ClassChoiceResponse;
  alternativeChoice?: ClassChoiceResponse;

  constructor(tape: ChoiceTapeResponse) {
    this.tape = tape;
  }
}
