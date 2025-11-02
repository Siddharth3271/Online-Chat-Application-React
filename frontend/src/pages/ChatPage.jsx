import { File, Send } from 'lucide-react'
import React, {useEffect, useRef } from 'react'
import { useState } from 'react'
import useChatContext from '../context/ChatContext';
import { useNavigate } from 'react-router';
import SockJS from 'sockjs-client';
import { baseURL } from '../config/AxiosHelper';
import { Stomp } from "@stomp/stompjs";
import toast from 'react-hot-toast';
import { getMessages } from '../RoomServiceapi'
import { timeAgo } from '../config/timer';

const ChatPage = () => {
const {roomId,currentUser, connected,setConnected,setRoomId,setCurrentUser}=useChatContext();
// console.log(roomId);
// console.log(currentUser);
// console.log(connected);

    const navigate=useNavigate();

    useEffect(() => {
        if(!connected) {
            navigate("/");
        }
    }, [connected, roomId, currentUser]);

    useEffect(() => {
        if(roomId && connected){
            loadMessages();
        }
    }, [roomId]);


    const [messages,setMessages]=useState([
        // {
        //     content:"Hello",
        //     sender:"Siddharth",
        // },
        // {
        //     content:"Hello",
        //     sender:"Ramesh",
        // },
        // {
        //     content:"Hello",
        //     sender:"Ramesh",
        // }
    ]);
    const [input,setInput]=useState("");
    const chatBoxRef=useRef(null);
    const [stompClient,setStompClient]=useState(null);

    //page init
    //puraney messages ko load karna hai
    async function loadMessages(){
        try{
            const responseHistory=await getMessages(roomId,20,0);
            console.log(messages);
            setMessages((prevMessages) => {
                // Prepend the history to the new messages
                return [...responseHistory, ...prevMessages];
            });
        }
        catch(error){
            console.log("Error in loading messages",error);
        }
    }

    //scroll to bottom
    useEffect(()=>{
        if(chatBoxRef.current){
            chatBoxRef.current.scroll(
                {
                    top: chatBoxRef.current.scrollHeight,
                    behavior:"smooth"
                }
            )
        }
    },[messages])


    //stomp client ko connect karna hai
    //stomp client ko subscribe karna hai   
    useEffect(()=>{
        //load messages function
        const connectWebSocket=()=>{
            //sockjs object
            const socket=new SockJS(`${baseURL}/api/v1.0/chat`);
            const client=Stomp.over(socket);

            client.connect({},()=>{
                setStompClient(client);
                toast.success("WebSocket connected");

                client.subscribe(`/topic/room/${roomId}`,(message)=>{
                    console.log(message);
                    const newMessage=JSON.parse(message.body);
                    setMessages((prevMessages)=>[...prevMessages,newMessage]);

                })
            });
        };

        if(connected){
            connectWebSocket();
        }

        

    },[roomId]);

    //send message function
    const sendMessage=async ()=>{
        if(stompClient && connected && input.trim()!==""){
            console.log(input)
            stompClient.send(`/app/chat/${roomId}`, {}, JSON.stringify({
                sender: currentUser,
                content: input,
                roomId: roomId
            }));
            setInput("");
        }
    }

    //handle logout
    const handleLogout=()=>{ 
        stompClient.disconnect();
        setConnected(false);
        setRoomId("");
        setCurrentUser("");
        toast.success("Left the room successfully");
        navigate("/");                           
    }


  return (
    <div>
      <header className=' bg-gray-400 py-5 flex justify-around items-center fixed w-full'>
        {/* Room Name */}
        <div>
            <h1 className='text-xl font-medium '>
                Room : <span>{roomId}</span>
            </h1>
        </div>
        <div>
            <h1 className='text-xl font-medium '>
                User : <span>{currentUser}</span>
            </h1>
        </div>
        <div>
            <button 
            onClick={handleLogout}
            className='bg-red-500 hover:bg-red-400 px-3 py-2 rounded text-white cursor-pointer border-2 border-red-900'>Leave Room</button>
        </div>
      </header>

        <main ref={chatBoxRef} className='px-10 py-20 border w-2/3 bg-slate-800 mx-auto h-screen overflow-auto text-white'>
           {messages.map((message,index)=>(
            <div key={index} className={`flex ${message.sender===currentUser ?'justify-end':'justify-start'}`}>
                <div className={`my-2 ${message.sender===currentUser ? 'bg-green-600': 'bg-gray-500'} p-2 rounded max-w-xs`}>
                {/* contains message */}
                <div className='flex flex-row gap-2'>
                    <img src={"https://avataaars.io/?avatarStyle=Circle&topType=LongHairStraight&accessoriesType=Blank&hairColor=BrownDark&facialHairType=Blank&clotheType=BlazerShirt&eyeType=Default&eyebrowType=Default&mouthType=Default&skinColor=Light"} alt="" className='h-10 w-10'/>
                    <div className='flex flex-col gap-1'>
                        <p className='text-sm font-bold'>{message.sender}</p>
                        <p>{message.content}</p>
                        <span className='text-xs text-green-200'>{timeAgo(new Date(message.timeStamp))}</span>
                    </div>
                </div>
            </div>
            </div>
           ))}
        </main>

      <div className='fixed bottom-4 w-full h-12'>
        <div className='h-full pr-3 gap-3 flex items-center justify-between rounded border w-1/2 mx-auto bg-gray-300'>
            <input 
            value={input}
            onChange={(e)=>{setInput(e.target.value);}} 
            type="text" placeholder='Type your message here....' className=' border-gray-600 px-5 py-2 rounded h-full w-full focus:outline-none'/>

            <div className='flex gap-3'>
                <button 
                onClick={sendMessage}
                className='bg-gray-800 text-white rounded h-8 w-14 flex justify-center items-center hover:bg-gray-600 cursor-pointer'><Send size={20}/></button>
                <button 
                onClick={sendMessage}
                className='bg-blue-900 text-white rounded h-8 w-14 flex justify-center items-center hover:bg-blue-600 cursor-pointer'><File size={20}/></button>
            </div>
        </div>
      </div>
    </div>
  )
}

export default ChatPage
