import { InferSchemaType, model, Schema } from "mongoose";


const eventSchema = new Schema({
    adminId: { type: Schema.Types.ObjectId, required: true },
    title: {type: String, required: true},
    text: {type: String}
},{
    timestamps : true
});

type Event = InferSchemaType<typeof eventSchema>;

export default model<Event>("Event", eventSchema);