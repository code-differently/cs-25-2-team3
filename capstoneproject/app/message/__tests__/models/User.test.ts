/**
 * __tests__/models/User.test.ts
 * Jest tests for the User model
 */

import { User, UserData } from '../../models/User';

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

  // Constructor Tests
  describe('Constructor', () => {
    // Constructor initializes all fields correctly with complete data
    it('should initialize all fields correctly with complete UserData', () => {
      const user = new User(mockUserData);
      
      expect(user.id).toBe(1);
      expect(user.username).toBe('testuser');
      expect(user.email).toBe('test@example.com');
      expect(user.createdAt).toBe('2024-01-01T00:00:00Z');
      expect(user.isActive).toBe(true);
    });

    // Constructor defaults isActive to true when omitted
    it('should default isActive to true when not provided', () => {
      const user = new User(mockUserDataMinimal);
      
      expect(user.id).toBe(2);
      expect(user.username).toBe('minimaluser');
      expect(user.email).toBe('minimal@example.com');
      expect(user.createdAt).toBe('2024-01-02T00:00:00Z');
      expect(user.isActive).toBe(true);
    });
  });

  // Serialization Tests
  describe('toObject()', () => {
    // toObject returns plain object matching UserData interface
    it('should return plain object via toObject method', () => {
      const user = new User(mockUserData);
      const result = user.toObject();
      
      expect(result).toEqual(mockUserData);
      expect(typeof result).toBe('object');
      expect(result.constructor).toBe(Object);
    });

    // toObject preserves all field values correctly
    it('should preserve all field values in serialized object', () => {
      const user = new User(mockUserDataMinimal);
      const result = user.toObject();
      
      expect(result.id).toBe(2);
      expect(result.username).toBe('minimaluser');
      expect(result.email).toBe('minimal@example.com');
      expect(result.isActive).toBe(true);
    });
  });
});
