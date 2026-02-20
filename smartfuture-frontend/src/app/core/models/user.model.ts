export enum UserRole {
  VISITOR = 'VISITOR',
  LEARNER = 'LEARNER',
  CONFIRMED_LEARNER = 'CONFIRMED_LEARNER',
  TRAINER = 'TRAINER',
  COMPANY = 'COMPANY',
  ADMIN = 'ADMIN'
}

export interface User {
  id: number;
  email: string;
  role: UserRole;
  firstName?: string;
  lastName?: string;
  emailVerified: boolean;
}
