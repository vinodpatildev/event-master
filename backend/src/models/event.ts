import { InferSchemaType, model, Schema } from "mongoose";

export enum EventState {
    UPCOMING = 'upcoming_events',
    LIVE = 'live_events',
    FINISHED = 'finished_events'
}

const eventSchema = new Schema({
    adminId: { type: Schema.Types.ObjectId, required: true },
    title: {type: String, required: true},
    text: {type: String, required: true},
    start: { type: Date, required: true },
    end: { type: Date, required: true },
    state: {
        type: String,
        enum: Object.values(EventState),
        default: EventState.UPCOMING,
        required: true,
  },
},{
    timestamps : true
});

type Event = InferSchemaType<typeof eventSchema>;

export default  model<Event>('Event', eventSchema);


