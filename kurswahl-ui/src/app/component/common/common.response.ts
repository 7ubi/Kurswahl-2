export interface MessageResponse {
  readonly messageId: number;
  readonly title: string;
  readonly message: string;
  readonly senderResponse: UserMessageResponse;
  readonly addresseeResponses: UserMessageResponse[];
  date: Date;
  readonly readMessage: boolean;
}

export interface UserMessageResponse {
  readonly userId: number;
  readonly username: string;
  readonly firstname: string;
  readonly surname: string;
}
