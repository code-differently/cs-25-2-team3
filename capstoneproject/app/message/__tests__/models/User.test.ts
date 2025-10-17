/**
 * __tests__/models/User.test.ts
 * Jest tests for the User model
 */

import { UserData } from '../../models/User';

describe('User Model', () => {
  // Test data setup
  const mockUserData: UserData = {
    id: 1,
    username: 'testuser',
    email: 'test@example.com',
    createdAt: '2024-01-01T00:00:00Z',
    isActive: true
  };

  const mockUserDataMinimal: UserData = {
    id: 2,
    username: 'minimaluser',
    email: 'minimal@example.com',
    createdAt: '2024-01-02T00:00:00Z'
    // isActive omitted - should default to true
  };
});
