export class StudentCsvRequest {
  csv: string;

  year: number;

  constructor(csv: string, year: number) {
    this.csv = csv;
    this.year = year;
  }
}
