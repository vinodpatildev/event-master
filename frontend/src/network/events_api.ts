import { UnauthorizedError, ConflictError } from '../errors/http_errors';
import { Response } from 'express';
import { Event } from "../models/event";
import { Admin } from "../models/admin";

async function fetchData(input: RequestInfo, init?: RequestInit){
    const response = await fetch(input, init);
    if(response.ok) {
        return response;
    } else{
        const errorBody = await response.json();
        const errorMessage = errorBody.error;
        if(response.status === 401){
            throw new UnauthorizedError(errorMessage);
        } else if(response.status === 409){
            throw new ConflictError(errorMessage);
        }else{
            throw Error("Request failed with status " + response.status + " message : " + errorMessage);
        }
        
    }
}

export async function getLoggedInAdmin() : Promise<Admin> {
    const response = await fetchData("/api/admins",{method:"GET"});
    return response.json();
}

export interface SignUpCredentials {
    username:string,
    email:string,
    password:string
}

export async function signUp(credentials: SignUpCredentials) : Promise<Admin> {
    const response = await fetchData("/api/admins/signup",
    {
        method:"POST",
        headers: {
            "Content-Type":"application/json",
        },
        body: JSON.stringify(credentials),
    });
    return response.json();
}

export interface LoginCredentials {
    username:string,
    password:string,
}

export async function login(credentials: LoginCredentials) : Promise<Admin> {
    const response = await fetchData("/api/admins/login",
    {
        method:"POST",
        headers:{
            "Content-Type":"application/json",
        },
        body: JSON.stringify(credentials),
    });
    return response.json();
}

export async function logout(){
    await fetchData("/api/admins/logout",{ method:"POST" });
}

export async function fetchEvents(): Promise<Event[]> {
    const response = await fetchData("/api/events/admin",{method:"GET"});
    return response.json();
}

export interface EventInput{
    title: string,
    text?: string,
    start: Date,
    end: Date
}

export async function createEvent(event:EventInput) : Promise<Event>{
    const response = await fetchData("/api/events",{
        method:"POST",
        headers:{
            "Content-Type": "application/json",
        },
        body: JSON.stringify(event),
    });
    return response.json();
}

export async function updateEvent(eventId:string, event: EventInput) : Promise<Event> {
    const response = await fetchData("/api/events/"+eventId,{
        method:"PATCH",
        headers:{
            "Content-Type":"application/json",
        },
        body: JSON.stringify(event),
    });
    return response.json();
}

export async function deleteEvent(eventId:string){
    await fetchData("/api/events/"+eventId, {method:"DELETE",})
}