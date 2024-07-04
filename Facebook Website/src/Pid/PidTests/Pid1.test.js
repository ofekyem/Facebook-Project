import React from 'react';
import { render, screen } from '@testing-library/react';
import { MemoryRouter } from "react-router-dom";
import Pid from "../Pid";
const userLoggedIn = {displayName: 'Test User', photo: null};
const postList = [{id : 1,
        fullname: "testuser",
        icon: "/faviconPerson1.ico",
        initialText: "wow look at this lake",
        pictures: "/Lake.png",
        time: "1/05/2023 13:05",
        likes: 300,
        commentsNumber: 2,
        comments: [
        {
            id: 1,
            fullname: "Or Shmuel",
            icon: "/faviconPerson2.ico",
            text: "yess wow!!"
        }]
    }];
describe('Pid', () => {
    test('renders NaviBar component', () => {
        render(
            <MemoryRouter>
                <Pid userLoggedIn={userLoggedIn} postList={postList}/>
            </MemoryRouter>
        );

        // Check if NaviBar is in the document
        expect(screen.getByRole('navigation')).toBeInTheDocument();
    });
});