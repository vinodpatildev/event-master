
import firebaseAdmin from "./firebaseAdmin";
import NotificationModel from '../models/notification';
import mongoose from "mongoose";

export function sendNotificationToStudent(title:string,body:string) {
    if(!title || !body){
        throw Error("Expected 'title' & 'body' to be defined, but received " + title + " and " + body);
    }
    const topic = 'event_master_notifications_student';
    const message = {
        data: {
            title: title,
            body: body,
        },
        topic: topic
    };
    firebaseAdmin.messaging().send(message)
        .then(async (response) => {
            console.log('Successfully sent message:', response);
            const authenticatedAdminId =  new mongoose.Types.ObjectId('640a9456694fe88c894cdcf6')
            const eventId = "NA";
            const type = "NA";
            const priority = "high";
            const channel =  "NA";
            
            const newNotification = await NotificationModel.create({
                adminId: authenticatedAdminId,
                title,
                body,
                eventId,
                type,
                priority,
                channel,
              });
        })
        .catch((error) => {
            console.log('Error sending message:', error);
        });
}