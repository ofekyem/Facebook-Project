import { isUsernameExists } from '../Authentication';

describe('isUsernameExists', () => {
    it('returns true if the username exists in the user list', () => {
        const userList = [{ username: 'user1' }, { username: 'user2' }];
        const username = 'user1';
        const result = isUsernameExists(username, userList);
        expect(result).toBe(true);
    });

    it('returns false if the username does not exist in the user list', () => {
        const userList = [{ username: 'user1' }, { username: 'user2' }];
        const username = 'user3';
        const result = isUsernameExists(username, userList);
        expect(result).toBe(false);
    });
});