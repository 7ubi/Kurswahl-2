export interface MessageResponse {
  readonly message: string;
}

export interface ResultResponse {
  readonly errorMessages: MessageResponse[];
}

export interface LoginResponse {
  readonly token: string;
  readonly type: string;
  readonly id: string;
  readonly username: string;
}
