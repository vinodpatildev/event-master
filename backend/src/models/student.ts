import { InferSchemaType, model, Schema } from "mongoose";

const studentSchema = new Schema({
    username: { type : String, required: true, unique: true },
    email: { type : String, required: true, unique: true },
    password: { type : String, required: true, select: false },
    registration_no: { type: String },
    name: { type: String },
    profile_image_url: {type:String},
    dob: { type: String },
    mobile: {type: String },
    year: {type:String },
    department: {type: String },
    division: {type: String },
    passing_year: {type: String },
    otp:{ type:String, default:'', select:false },
    present: { type: Boolean, default: false },
    events_registered: [
        {
          event: {
            type: Schema.Types.ObjectId,
            ref: "event",
            required: true,
          }
        },
      ],
      events_attended: [
        {
          event: {
            type: Schema.Types.ObjectId,
            ref: "event",
            required: true,
          }
        },
      ],
});
studentSchema.set("toJSON", {
    transform: function (doc, ret) {
      delete ret.events_registered;
      delete ret.events_attended;
      return ret;
    },
  });

type Student = InferSchemaType<typeof studentSchema>;

export default model<Student>("Student", studentSchema);

