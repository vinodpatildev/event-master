import createHttpError from 'http-errors';
import { RequestHandler } from "express";

export const requiresAuth : RequestHandler = (req, res, next) => {
    if(req.session.userSessionId){
        next();
    }else{
        next(createHttpError(401, String(req.session.userSessionId) + " : User not authenticated."));
    }
}