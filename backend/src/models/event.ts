import { InferSchemaType, model, Schema } from "mongoose";

export enum EventState {
    UPCOMING = 'upcoming_events',
    LIVE = 'live_events',
    FINISHED = 'finished_events'
}

const eventSchema = new Schema({
    adminId: { type: Schema.Types.ObjectId, required: true },
    title: { type: String, required: true },
    department: { type: String },
    organizer: { type: String },
    description: { type: String },
    type: {type: String},
    event_link: {type: String},
    start: { type: Date, required: true },
    end: { type: Date, required: true },
    location :{type:String},
    longitude:{type:String},
    latitude:{type:String},
    state: {
        type: String,
        enum: Object.values(EventState),
        default: EventState.UPCOMING,
        required: true,
    },
    attendees: [
        {
          student: {
            type: Schema.Types.ObjectId,
            ref: "student",
            required: true,
          },
          present: { type: Boolean, default: false },
        },
      ],
},{
    timestamps : true
});

eventSchema.set("toJSON", {
    transform: function (doc, ret) {
      delete ret.attendees;
      return ret;
    },
  });

type Event = InferSchemaType<typeof eventSchema>;

export default  model<Event>('Event', eventSchema);


