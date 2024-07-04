import { validatePassword } from '../Authentication.js';

describe('validatePassword', () => {
    it('returns true for a valid password', () => {
        const password = 'Password123!';
        const result = validatePassword(password);
        expect(result).toBe(true);
    });

    it('returns false for a password without a capital letter', () => {
        const password = 'password123!';
        const result = validatePassword(password);
        expect(result).toBe(false);
    });

    it('returns false for a password without a special character', () => {
        const password = 'Password123';
        const result = validatePassword(password);
        expect(result).toBe(false);
    });

    it('returns false for a password with less than 8 characters', () => {
        const password = 'Pass12!';
        const result = validatePassword(password);
        expect(result).toBe(false);
    });
});