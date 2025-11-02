import React, { useState } from 'react'
import chatIcon from "../assets/chat.png"
import toast from 'react-hot-toast'
import { useNavigate } from 'react-router'
import { createRoomApi,joinChatApi } from '../RoomServiceapi'
import useChatContext from '../context/ChatContext'
const Home = () => {

  const [detail,setDetail]=useState({
    roomId:'',
    userName:''
  })

  const { roomId, userName, setRoomId, setCurrentUser, setConnected } = useChatContext();
  const navigate = useNavigate();

  function handleFormInputChange(event) {
    setDetail({
      ...detail,
      [event.target.name]: event.target.value,
    });
  }

  async function joinChat(){
    if (validateForm()) {
      //join chat
      try {
        const room = await joinChatApi(detail.roomId);
        toast.success("joined the chat..");
        setCurrentUser(detail.userName);
        setRoomId(room.roomId);
        setConnected(true);
        navigate("/chat");
      } 
      catch (error) {
        if(error.status==400) {
          toast.error(error.response.data);
        } 
        else{
          toast.error("Error in joining room");
        }
        console.log(error);
      }
    }
  }
  async function createRoom(){
    console.log(detail)
    if (validateForm()) {
      //call api to create room on backend
      try {
        const requestData={
          roomId:detail.roomId.trim(),
          userName: detail.userName.trim()
        }
        const response = await createRoomApi(requestData);
        console.log(response);
        toast.success("Room Created Successfully !!");
        //join the room
        setCurrentUser(requestData.userName);
        setRoomId(response.roomId);
        setConnected(true);

        //forward to chat page...
        navigate("/chat");
      } 
      catch (error) {
        console.log("Error creating room:",error);
        if (error.response?.status == 409) {
          toast.error("Room  already exists !!");
        } 
        else if(error.response?.status == 400){
          toast.error("Invalid data. Check fields:",error.response.data);
        }
        else {
          toast("An unknown error occurred while creating the room.");
        }
      }
    }
  }

  function validateForm(){
    if (detail.roomId.trim() === "" || detail.userName.trim() === "") {
      toast.error("Name and Room ID cannot be empty !!");
      return false;
    }
    return true;
  }

  return (
    <div className='min-h-screen flex items-center justify-center'>
      <div className='p-8 flex flex-col gap-8 w-full max-w-md rounded bg-blue-200 shadow border border-blue-500'>

        <div>
            <img src={chatIcon} alt="" className='w-14 mx-auto' />
        </div>

        <h1 className='text-2xl font-medium text-center'>Join/Create Room</h1>
        <div className='m-3'>

            <label htmlFor="name" className='block font-medium mb-2 font-xl'>Your Name :</label>
            <input 
            onChange={handleFormInputChange}
            value={detail.userName}
            name='userName'
            placeholder='Enter your name'
            type="text" id="name" className='w-full bg-gray-300 rounded-lg px-4 py-2 border-2 border-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-400 mb-5'/>

            <label htmlFor="room-id" className='block font-medium mb-2 font-xl'>Room ID :</label>
            <input 
            onChange={handleFormInputChange}
            value={detail.roomId}
            name='roomId'
            placeholder='Enter Room Id'
            type="text" id="room-id" className='w-full bg-gray-300 rounded-lg px-4 py-2 border-2 border-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-400 mb-5'/>

            <div className='flex justify-center gap-10'>
                <button 
                onClick={joinChat}
                className='px-3 py-2 bg-blue-500 hover:bg-blue-400 rounded cursor-pointer'>Join Now</button>
                <button 
                onClick={createRoom}
                className='px-3 py-2 bg-blue-800 hover:bg-blue-600 rounded cursor-pointer text-blue-200'>Create Room</button>
            </div>
        </div>
      </div>
    </div>
  )
}

export default Home
