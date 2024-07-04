# Page 3 - Run the Facebook Website.

## About the Website 
The webstie app was developed in vs code software and it is connected to a Node Js server and TCP server so make sure you already run those servers
before you open this app because it won't work without it!(read pages 1 and 2) if you already done it continue... 

This website was developed in React and we used HTML CSS and javaScript to make it. In part 2 of the project we did the front side of the website (we designed it and made some temporary functionality),

In part 3 we connected it into the node js server

And In part 4 we connected it into the TCP server and bloom filter.

we will explain more about the website after the running. 

We again suggest you to also read the ReadMe.md file of this folder ("Facebook Website" folder) in addition to this page. 

## Run the Website

You can start with donwload a zip of the folder "Facebook Website" and extract it.

Now, open the cloned folder in vs-code 

When you already in the folder, you need to run this command;

npm install

When its finished, run this command: 

npm start

And the website will open and the first page you will see is the login page.

## Here's again a video that shows what our website can do

https://www.youtube.com/watch?v=7xheojkxw5M

## first of all you need to register a user

New users can create an account by navigating to the Sign Up page by clicking "Sign Up". They will need to provide a username, password (your password must contains at least 8 characters capital letter and a sign ), display name (the name that you and your friends will see in posts, comments and your profile page) , and a profile picture. When you are chosing a picture from the computer files make sure you pick a photo in PNG or JPEG format and in size up to 2mb. You must fill all the inputs to create a user. if you want to see what is the password that you have written put your mouse on the eye icon. After you are ready press Sign-Up.

## now you can login to your account!

Please enter the username and password of the account you have created and press Sign-in and next page you will see is the feed page:

## The Navigation bar 

In the top of the feed and the other pages you will see there is a navigation bar that can help you to navigate from page to another: 

* If you press the facebook-Icon on the left, you will move into the feed page.
* If you press the Home-Icon on the middle, you will also move into the feed page.
* In the right side of the navigaition bar you can see the name you have chose and your profile picture, by clicking on this you will navigate into your profile page.
* If you press the Bell-Icon you will you should see list of notifications about friend requests, for each notification you will see the   name and the profile pic of the user that sends you the friend request and you can choose to approve his request or decline it, if you are choosing to approve him with approve icon, this friend will add into your friends list. if you decline him with decline icon, you will not be his friend. either way, after you click the approve or decline icon, the notification will be deleted from the list. 
You can notice that if you have some notifications you will see a red circle with the number of notifications beside the bell icon.
* If you press the Menu-Icon you will have 3 options:
  1. darkmode/lightmode - change your app view into darkmode/lightmode depends on what you are right now.
  2. Edit User - navigates you into the Edit User page.
  3. Log Out - navigates you back to the login page.

## The Left side of the website 

In the left side of the website you can see two sections
    
  1. The top section contains some buttons that are simillar to the real facebook website, you can click on the button "your profile" to navigates into your profile.
    
  2. the bottom section contains your user "friends list" if your user have some friend, you can see the list of them, each one of them will include his name and profile pic. By clicking on their profile picture, you can navigate into their profile page.

## The Right side of the website 

In the right side of the website you can also see two sections: 
    
  1. The top section contains some current events. currently, you can see that there are programming advanced lessons sceduled.
    
  2. The bottom section contains 2 nice ads like the real facebook website. 

## What can you do in the Feed page?

When you log in into the website you will see in the center the Feed page, the feed page contains 2 Sections: 

### Add a Post 

Here you can add a post to the server posts list, first of all you can see your name and your pfoile pic in the left circle. also in the text box you can see that your name is in it and it will asks you to write what do you think. you can write there the text of the post you want to upload, also you can add a picture by clicking on the "Photo/Video" button. you will have the option to upload a file from your computer, When you are chosing a picture from the computer files make sure you pick a photo in PNG or JPEG format and in size up to 2mb. After you choose your pic you will see a preview of it and a button for option to delete this pic if you regret. Now when your post is ready you can click on the "post" button you will see your post in the posts list under this section!, now the text box will initialized and you can add another post if you want. 

*notice* that if you want to post some url in your text the url must be valid, meaning that it must not be inside the bloom filter. the invalid links are in the env file of the node js server you can look there and see which url is invalid. if you will try to upload a post with invalid urls your post will not be uploaded!

### Post List 

Here you can see the list of posts that contains the posts that are right now in the server and have been uploaded by users from the server (if there are no posts you won't see anything there). You can notice that if you have more than 25 posts, 20 posts that shown up will be of your friends/yours and the other 5 of users that are not your friend. Every post contains the name of the user uploaded, his profile picture in circle, the date, some text content and optionally a picture for the post. 

if the post is written by your user you will also see a Three-Dots icon that can let you delete the post from the server, edit the text of the post or delete/add pic:
* If you choose to edit your post text, a text box that looks exactly like the add post section will open, you will se your old text is written and you can change it. By clicking the approve icon your text of the post will be edited and you will see your edited text in the post, if you changed your mind about editing click on the garbage icon and text will remain the same. 
* If you already had a picture in your post you will have the option of "delete picture" that will deletes your picture from your post.
* If you don't have a picture in your post you will have the option of "add picture" where you can add a picture for your post from your computer files. After you chose your photo, the updated post will be shown with the new picture.

*notice* that if you want to edit a post and add some urls in your text the url must be valid, meaning that it must not be inside the bloom filter. the invalid links are in the env file of the node js server repo you can look there and see which url is invalid. if you will try to edit a post and put in it invalid urls your post will not be edited!

You can also notice that the post contains in the bottom of it a like counter that indicates how many likes the post have and a comments counter that indicates how many comments there are. 
You can also click on the like icon button to do/undo like for the post, you can see it will add/decrease from the counter.
If you click on the share icon it will open up for you a little menu with options of share. 

### Comments
If you click on the comments icon of the post:
* If there are already comments for this post in the server you will see them in a list. every comment have the name of the uploader his picture and the content text. if there are no comments you won't see anything there. 
* You can add a comment yourself by clicking on the comment icon and it opens for you a text box. please write what you want just don't leave it blank. and you can click on the arrow button to add it and then you will see your comment in the list. 
* If you are *the writer of the posts where these comments are in*, you also can see a garbage icon in every comment of this post and this icon will let you delete any comment you like from the post (even if its not yours).
* if you are *the writer of the comment* you can see 2 icons: the garbage that can let you delete it and a pencil icon that let you edit the comment. When you choose to edit the comment you will see a text box that will let you change what you have written and when you are ready click on approve icon or garbage icon if you changed your mind.

You can also click on the Profile Picture of one of the posts writers to navigate into his Profile Page. 


## What can you do in the Profile Page?

If you are here you must have clicked on some user icon or you clicked on your profile icon, here you can see the profile page of the user. You can see in the top section the user name and his profile picture and you can see the number of friends he has.

near the Profile Picture there are several options that can be shown:
1. if you are in the profile page of a user that is your friend - you will see a "delete friend" button that will let you delete him from your friends.
2. If you are in the profile page of a user that is not your friend but he has send you a request to be his friend - you will see "approve" button that will let you accept his request and this user will add into your friends list.
3. If you are in the profile page of a user that is not your friend, but you sent him a request - you will see a text " Friend request sent" and it will changed when the user approved or decline your friend request.
4. If you are in the profile page of a user that is not your friend, and you both didn't send any request you will see an Add friend button that when you click it, it will send the user request for friend and the button will change into number 3. option.
5. If you are in the profile page of your own - you won't see anything there.

In the center section you can see the friends list, here you can see the list of friends this user has. you can see their names and their profile pics. If you click on someone profile pic you will navigate into his profile page. 
 
in the bottom section there are several options that can be shown: 
1. If you are in the profile page of a user that is yourself/your friend - you will see yours/your friend posts that only you/he has written.
2. If you are in the profile page of a user that is not your friend, but he has send you a request to be his friend - you won't see his posts ad you will se a text "You need to be his friend to see his posts" but if you click "approve" you will suddenly see the posts because now you are friends.
3. If you are in the profile page of a user that is not your friend/you sent him a request - you won't see his posts and you will also see the text: "You need to be his friend to see his posts" because you are still not friends, sorry....
4. In case the user has no posts you will see the text: "No posts available".


## What can you do in the Edit User page? 

If you are looking to change your user details this is the right place! Here you can change your username/password/display-name/picture if you want.
When you are chosing a picture from the computer files make sure you pick a photo in PNG or JPEG format and in size up to 2mb.
When your details are ready you can click on submit and you will go back to the feed page and you will see the changes that you just did! 
If you changed your mind and don't want to edit the user details, press on the cancel button and you will back to the feed page without any change.
You can also delete your user by clicking the "delete user" button, if you pressed it you will be navigated back to the login page and your user will be deleted from the server.

# Now 
Enjoy this website! 
You can run the android app too if you like to, go to page 4!
