import React from 'react';
import { render, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import AddPost from '../AddPost';

describe('AddPost', () => {
    it('adds a new post when the form is submitted', () => {
    const mockSetPosts = jest.fn();
    const mockPosts = [
        { id: 1, fullname: 'Test User', initialText: 'Test Post', icon: null, pictures: null, time: new Date().toLocaleString(), likes: 0, commentsNumber: 0, comments: [] }
    ];
    const mockUserLoggedIn = { displayName: 'Test User', photo: null };
    // Mock window.alert
    window.alert = jest.fn();
    const { getByPlaceholderText, getByText } = render(<AddPost setPosts={mockSetPosts} posts={mockPosts} userLoggedIn={mockUserLoggedIn} />);
    userEvent.type(getByPlaceholderText(`${mockUserLoggedIn.displayName}, what do u think?`), 'New Post');
    fireEvent.click(getByText('Post'));
    expect(mockSetPosts).toHaveBeenCalledWith([...mockPosts, expect.objectContaining({ initialText: 'New Post' })]);
    expect(window.alert).toHaveBeenCalledWith('Post added successfully');
    });
});