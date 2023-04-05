import { InferSchemaType, model, Schema } from "mongoose";

const notificationSchema = new Schema({
    adminId: { type: Schema.Types.ObjectId, required: true },
    title: { type: String, required: true },
    body: { type: String },
    type: {type:String},
    eventId: { type: String },
    timestamp: { type: Date, default: Date.now, required: true },
    priority: { type: String, enum: ["high", "normal", "low"] },
    channel: { type: String },
},{
    timestamps : true
});

type Notification = InferSchemaType<typeof notificationSchema>;

export default  model<Notification>('Notification', notificationSchema);
