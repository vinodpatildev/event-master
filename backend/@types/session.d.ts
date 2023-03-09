import mongoose from "mongoose";

declare module "express-session" {
    interface SessionData {
        userSessionId: mongoose.Types.ObjectId;
    }
}