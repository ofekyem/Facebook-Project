import React from 'react';
import { render, fireEvent, act } from '@testing-library/react';
import Form_Create from '../Form_Create';

jest.mock('react-router-dom', () => ({
    useNavigate: () => jest.fn(),
}));

describe('Form_Create', () => {
    it('alerts with "need to fill all the information" when the sign up button is clicked without filling any information', async () => {
        const mockSetUserList = jest.fn();
        const { getByRole } = render(<Form_Create userList={[]} setuserList={mockSetUserList} />);
        
        window.alert = jest.fn();

        // Wrap the button click inside an act call
        await act(async () => {
            fireEvent.click(getByRole('button', { name: /Sign Up/i }));
        });

        // Check the alert
        expect(window.alert).toHaveBeenCalledWith('need to fill all the information');
    });
});